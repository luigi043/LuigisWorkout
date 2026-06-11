package com.luigisworkout.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luigisworkout.LuigisWorkoutApp
import com.luigisworkout.data.AppContent
import com.luigisworkout.data.WorkoutDay
import com.luigisworkout.data.database.entities.*
import com.luigisworkout.data.repository.AppRepository
import com.luigisworkout.util.AppClock
import com.luigisworkout.util.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

// ─── FACTORY ──────────────────────────────────────────────────────────────────
//
// One factory for every ViewModel in the app. Built once from the application
// singletons so each VM gets the shared [AppRepository] and the shared
// "today" LiveData. Uses the modern `viewModelFactory` DSL — no deprecated
// `create(Class<T>)` overload, no reflection.
//
fun appViewModelFactory(app: LuigisWorkoutApp): ViewModelProvider.Factory = viewModelFactory {
    initializer { HomeViewModel(app.repository, app.todayDate) }
    initializer { MealPlanViewModel(app.repository, app.todayDate) }
    initializer {
        WorkoutViewModel(
            app.repository,
            app.todayDate,
            app.getSharedPreferences("workout_state", Context.MODE_PRIVATE)
        )
    }
    initializer { ProgressViewModel(app.repository) }
    initializer { LogMealViewModel(app.repository) }
    initializer { LogWeightViewModel(app.repository) }
}

// ─── HOME VIEWMODEL ───────────────────────────────────────────────────────────

class HomeViewModel(
    private val repository: AppRepository,
    todayDate: LiveData<String>
) : ViewModel() {

    // Each "today" query re-issues when the device crosses midnight, so the
    // counters can't get stuck on yesterday's data.
    val todayMeals: LiveData<List<MealLog>> = todayDate.switchMap { repository.getMealsByDate(it) }
    val todayCalories: LiveData<Int?> = todayDate.switchMap { repository.getCaloriesByDate(it) }
    val todayGoal: LiveData<DailyGoal?> = todayDate.switchMap { repository.getGoalByDate(it) }
    val todayWorkout: LiveData<WorkoutLog?> = todayDate.switchMap { repository.getWorkoutByDate(it) }

    val latestWeight: LiveData<WeightLog?> = repository.getLatestWeight()
    val completedWorkouts: LiveData<Int> = repository.getCompletedWorkoutsCount()
    val recentWeekWorkouts: LiveData<List<WorkoutLog>> = repository.getRecentWeekWorkouts()

    private val _macros = MutableLiveData<Triple<Float, Float, Float>>()
    val macros: LiveData<Triple<Float, Float, Float>> = _macros

    fun refreshMacros() = viewModelScope.launch {
        _macros.value = repository.getMacrosByDate(AppClock.todayString())
    }

    fun addWater() = viewModelScope.launch { repository.adjustWaterCount(+1) }
    fun removeWater() = viewModelScope.launch { repository.adjustWaterCount(-1) }
}

// ─── MEAL PLAN VIEWMODEL ──────────────────────────────────────────────────────

class MealPlanViewModel(
    private val repository: AppRepository,
    todayDate: LiveData<String>
) : ViewModel() {

    val breakfastRecipes = AppContent.recipes.filter { it.mealType == "breakfast" }
    val lunchRecipes = AppContent.recipes.filter { it.mealType == "lunch" }
    val dinnerRecipes = AppContent.recipes.filter { it.mealType == "dinner" }
    val snackRecipes = AppContent.recipes.filter { it.mealType == "snack" }
    val drinkRecipes = AppContent.recipes.filter { it.mealType == "drink" }

    /** Daily meal schedule — exposed for UI hints (which slot are we in right now?). */
    val mealSchedule = AppContent.mealSchedule

    val foodDatabase = AppContent.foodDatabase

    val todayMeals: LiveData<List<MealLog>> = todayDate.switchMap { repository.getMealsByDate(it) }
    val todayCalories: LiveData<Int?> = todayDate.switchMap { repository.getCaloriesByDate(it) }
    val recentMeals: LiveData<List<MealLog>> = repository.getRecentMeals()

    fun deleteMeal(log: MealLog) = viewModelScope.launch { repository.deleteMeal(log) }

    suspend fun getRecentFoodNames() = repository.getRecentFoodNames()

    // Shopping list
    val shoppingItems: LiveData<List<ShoppingItem>> = repository.getAllShoppingItems()

    fun addShoppingItem(name: String, quantity: String = "", supermarket: String = "Any") =
        viewModelScope.launch {
            repository.insertShoppingItem(
                ShoppingItem(name = name, quantity = quantity, supermarket = supermarket)
            )
        }

    fun toggleShoppingItem(item: ShoppingItem) = viewModelScope.launch {
        repository.updateShoppingItem(item.copy(isChecked = !item.isChecked))
    }

    fun deleteShoppingItem(item: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(item)
    }

    fun clearCheckedItems() = viewModelScope.launch {
        repository.clearCheckedShoppingItems()
    }
}

// ─── LOG MEAL VIEWMODEL ───────────────────────────────────────────────────────

class LogMealViewModel(
    private val repository: AppRepository
) : ViewModel() {

    val foodDatabase = AppContent.foodDatabase

    /** One-shot: emits exactly once per save, then re-arms. Won't replay on rotate. */
    val saved = SingleLiveEvent<Boolean>()

    fun saveMeal(log: MealLog) = viewModelScope.launch {
        repository.insertMeal(log)
        saved.value = true
    }
}

// ─── WORKOUT VIEWMODEL ────────────────────────────────────────────────────────

class WorkoutViewModel(
    private val repository: AppRepository,
    todayDate: LiveData<String>,
    private val prefs: SharedPreferences
) : ViewModel() {

    val allWorkouts: LiveData<List<WorkoutLog>> = repository.getAllWorkouts()
    val completedCount: LiveData<Int> = repository.getCompletedWorkoutsCount()
    val recentWeek: LiveData<List<WorkoutLog>> = repository.getRecentWeekWorkouts()
    val todayWorkout: LiveData<WorkoutLog?> = todayDate.switchMap { repository.getWorkoutByDate(it) }
    val userProfile: LiveData<UserProfile?> = repository.getUserProfile()

    val fullPlan = AppContent.workoutPlan

    // ─── Reactive selection (drives "is this day already done?" lookups) ─────

    private val _selectedWeek = MutableLiveData(1)
    val selectedWeek: LiveData<Int> = _selectedWeek
    private val _selectedDay = MutableLiveData(1)

    fun selectWeek(week: Int) {
        if (_selectedWeek.value != week) _selectedWeek.value = week
    }

    fun selectDay(day: Int) {
        if (_selectedDay.value != day) _selectedDay.value = day
    }

    /** Distinct dayNumbers already completed at least once for the selected week. */
    val completedDaysForSelectedWeek: LiveData<List<Int>> =
        _selectedWeek.switchMap { repository.getCompletedDayNumbersForWeek(it) }

    /** Most recent log for the currently selected (week, day). Null if never done. */
    val selectedDayLog: LiveData<WorkoutLog?> = MediatorLiveData<WorkoutLog?>().also { mediator ->
        var source: LiveData<WorkoutLog?>? = null
        fun rebind() {
            val w = _selectedWeek.value ?: return
            val d = _selectedDay.value ?: return
            source?.let(mediator::removeSource)
            val next = repository.getLatestLogForWeekDay(w, d)
            source = next
            mediator.addSource(next) { mediator.value = it }
        }
        mediator.addSource(_selectedWeek) { rebind() }
        mediator.addSource(_selectedDay) { rebind() }
    }

    /** Days completed / days planned for the user's *current* week (drives Home card). */
    val currentWeekProgress: LiveData<Pair<Int, Int>> = MediatorLiveData<Pair<Int, Int>>().also { mediator ->
        var doneSource: LiveData<List<Int>>? = null
        fun rebind() {
            val week = userProfile.value?.currentWeekNumber?.coerceIn(1, 12) ?: return
            val total = repository.totalDaysInWeek(week)
            doneSource?.let(mediator::removeSource)
            val next = repository.getCompletedDayNumbersForWeek(week)
            doneSource = next
            mediator.addSource(next) { done ->
                mediator.value = (done?.size ?: 0) to total
            }
        }
        mediator.addSource(userProfile) { rebind() }
    }

    /** Fired exactly once when logWorkout pushes the user to a new week. */
    val weekAdvanced = SingleLiveEvent<Int>()

    // ─── Timer + in-progress check state ─────────────────────────────────────
    //
    // Survives Fragment view recreation (lives in the ViewModel) AND app
    // close/process death: every mutation is mirrored to SharedPreferences and
    // restored in init. Elapsed time is wall-clock based, so a running timer
    // keeps counting while the app is in the background or closed.
    // Paused time is excluded via accumulatedMillis + segmentStart.

    var isWorkoutActive: Boolean = prefs.getBoolean(KEY_ACTIVE, false)
        private set
    var workoutStartTime: Long = prefs.getLong(KEY_START_TIME, 0L)
        private set

    /** Milliseconds accumulated across finished run segments (pauses excluded). */
    private var accumulatedMillis: Long = prefs.getLong(KEY_ACCUM_MS, 0L)

    /** Wall-clock start of the current running segment; only meaningful while active. */
    private var segmentStart: Long = prefs.getLong(KEY_SEGMENT_MS, 0L)

    /** The (week, day) that [completedExercises] belongs to. -1 = none yet. */
    var progressWeek: Int = prefs.getInt(KEY_WEEK, -1)
        private set
    var progressDay: Int = prefs.getInt(KEY_DAY, -1)
        private set

    /** Indices of exercises ticked for (progressWeek, progressDay). */
    val completedExercises: MutableSet<Int> =
        (prefs.getString(KEY_COMPLETED, "") ?: "")
            .split(',')
            .mapNotNull { it.toIntOrNull() }
            .toMutableSet()

    /** True when there is restorable workout state (running, paused, or ticked boxes). */
    val hasInProgressState: Boolean
        get() = workoutStartTime != 0L || completedExercises.isNotEmpty()

    private var timerJob: Job? = null

    private val _elapsedTime = MutableLiveData(formatElapsed(0L))
    val elapsedTime: LiveData<String> = _elapsedTime

    init {
        // Pick the timer back up if the app was closed/killed mid-workout.
        if (isWorkoutActive) startTimer() else _elapsedTime.value = formatElapsed(elapsedMillis())
    }

    fun elapsedMillis(): Long =
        accumulatedMillis + if (isWorkoutActive) System.currentTimeMillis() - segmentStart else 0L

    fun startWorkout() {
        if (isWorkoutActive) return
        isWorkoutActive = true
        segmentStart = System.currentTimeMillis()
        if (workoutStartTime == 0L) workoutStartTime = segmentStart
        persistTimer()
        startTimer()
    }

    fun pauseWorkout() {
        if (!isWorkoutActive) return
        accumulatedMillis += System.currentTimeMillis() - segmentStart
        isWorkoutActive = false
        timerJob?.cancel()
        persistTimer()
    }

    fun resumeWorkout() = startWorkout()

    fun resetWorkout() {
        isWorkoutActive = false
        workoutStartTime = 0L
        accumulatedMillis = 0L
        segmentStart = 0L
        timerJob?.cancel()
        _elapsedTime.value = formatElapsed(0L)
        completedExercises.clear()
        prefs.edit().clear().apply()
    }

    /**
     * Point the in-progress state at (week, day). When this differs from the day
     * the saved state belongs to, checks are cleared and any non-running timer is
     * dropped. Returns true when the saved state matches (i.e. it was restored).
     */
    fun bindDay(week: Int, day: Int): Boolean {
        if (week == progressWeek && day == progressDay) return true
        progressWeek = week
        progressDay = day
        completedExercises.clear()
        if (!isWorkoutActive) {
            // A paused/unstarted timer belonged to the previous day — drop it.
            workoutStartTime = 0L
            accumulatedMillis = 0L
            segmentStart = 0L
            _elapsedTime.value = formatElapsed(0L)
        }
        persistAll()
        return false
    }

    fun setExerciseCompleted(index: Int, completed: Boolean) {
        if (completed) completedExercises.add(index) else completedExercises.remove(index)
        persistChecks()
    }

    fun markAllCompleted(count: Int) {
        completedExercises.clear()
        completedExercises.addAll(0 until count)
        persistChecks()
    }

    fun clearCompleted() {
        completedExercises.clear()
        persistChecks()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            // viewModelScope runs on Main, so `value =` is safe and avoids the
            // small postValue() coalescing delay that made the timer visibly jitter.
            while (isWorkoutActive) {
                _elapsedTime.value = formatElapsed(elapsedMillis())
                delay(1000)
            }
        }
    }

    private fun formatElapsed(ms: Long): String {
        val totalSeconds = ms / 1000
        return String.format(Locale.getDefault(), "⏱ %02d:%02d", totalSeconds / 60, totalSeconds % 60)
    }

    private fun persistTimer() {
        prefs.edit()
            .putBoolean(KEY_ACTIVE, isWorkoutActive)
            .putLong(KEY_START_TIME, workoutStartTime)
            .putLong(KEY_ACCUM_MS, accumulatedMillis)
            .putLong(KEY_SEGMENT_MS, segmentStart)
            .apply()
    }

    private fun persistChecks() {
        prefs.edit().putString(KEY_COMPLETED, completedExercises.joinToString(",")).apply()
    }

    private fun persistAll() {
        prefs.edit()
            .putBoolean(KEY_ACTIVE, isWorkoutActive)
            .putLong(KEY_START_TIME, workoutStartTime)
            .putLong(KEY_ACCUM_MS, accumulatedMillis)
            .putLong(KEY_SEGMENT_MS, segmentStart)
            .putInt(KEY_WEEK, progressWeek)
            .putInt(KEY_DAY, progressDay)
            .putString(KEY_COMPLETED, completedExercises.joinToString(","))
            .apply()
    }

    private companion object {
        const val KEY_ACTIVE = "active"
        const val KEY_START_TIME = "startTimeMs"
        const val KEY_ACCUM_MS = "accumulatedMs"
        const val KEY_SEGMENT_MS = "segmentStartMs"
        const val KEY_WEEK = "progressWeek"
        const val KEY_DAY = "progressDay"
        const val KEY_COMPLETED = "completedIndices"
    }

    fun getWorkoutsByWeek(week: Int): List<WorkoutDay> = fullPlan.filter { it.weekNumber == week }

    fun logWorkout(
        workoutDay: WorkoutDay,
        completedExercises: Int,
        minutes: Int
    ) = viewModelScope.launch {
        // Float math: previous int division would drop calories to 0 when the
        // user finished much faster than the estimated duration.
        val estimated = workoutDay.estimatedMinutes.coerceAtLeast(1)
        val calories = (workoutDay.caloriesBurned.toFloat() * minutes / estimated).toInt()
        val log = WorkoutLog(
            date = AppClock.todayString(),
            workoutName = workoutDay.name,
            weekNumber = workoutDay.weekNumber,
            dayNumber = workoutDay.dayNumber,
            totalMinutes = minutes,
            exercisesCompleted = completedExercises,
            totalExercises = workoutDay.exercises.size,
            caloriesBurned = calories,
            completed = completedExercises == workoutDay.exercises.size
        )
        repository.insertWorkout(log)
        // After saving, see whether this completes the user's current week.
        // The repo bumps UserProfile.currentWeekNumber atomically and tells us
        // the new week so the UI can celebrate.
        repository.advanceWeekIfComplete()?.let { newWeek ->
            weekAdvanced.value = newWeek
        }
    }

    fun logExercise(exerciseName: String, weightKg: Float, reps: Int, sets: Int) = viewModelScope.launch {
        repository.insertExerciseLog(
            ExerciseLog(
                date = AppClock.todayString(),
                exerciseName = exerciseName,
                weightKg = weightKg,
                reps = reps,
                sets = sets
            )
        )
    }

    fun getExerciseHistory(exerciseName: String): LiveData<List<ExerciseLog>> =
        repository.getExerciseHistory(exerciseName)

    suspend fun getLatestExerciseLog(exerciseName: String) =
        repository.getLatestExerciseLog(exerciseName)
}

// ─── PROGRESS VIEWMODEL ───────────────────────────────────────────────────────

class ProgressViewModel(
    private val repository: AppRepository
) : ViewModel() {

    val allWeights: LiveData<List<WeightLog>> = repository.getAllWeights()
    val weightLast30Days: LiveData<List<WeightLog>> =
        repository.getWeightByDateRange(repository.dateXDaysAgo(30))
    val caloriesLast30Days = repository.getCaloriesByDateRange(repository.dateXDaysAgo(30))
    val completedWorkouts: LiveData<Int> = repository.getCompletedWorkoutsCount()
    val userProfile: LiveData<UserProfile?> = repository.getUserProfile()

    private val _totalCaloriesBurned = MutableLiveData(0)
    val totalCaloriesBurned: LiveData<Int> = _totalCaloriesBurned

    fun insertWeight(kg: Float, heightCm: Float, note: String = "") = viewModelScope.launch {
        val safeHeight = heightCm.takeIf { it > 0f } ?: 0f
        val bmi = if (safeHeight > 0f) kg / ((safeHeight / 100f) * (safeHeight / 100f)) else 0f
        repository.insertWeight(
            WeightLog(
                date = AppClock.todayString(),
                weightKg = kg,
                bmi = bmi,
                note = note
            )
        )
    }

    fun deleteWeight(log: WeightLog) = viewModelScope.launch { repository.deleteWeight(log) }

    fun refreshStats() = viewModelScope.launch {
        _totalCaloriesBurned.value = repository.getTotalCaloriesBurnedLast30Days()
    }
}

// ─── LOG WEIGHT VIEWMODEL ────────────────────────────────────────────────────

class LogWeightViewModel(
    private val repository: AppRepository
) : ViewModel() {

    val saved = SingleLiveEvent<Boolean>()

    fun saveWeight(kg: Float, note: String = "") = viewModelScope.launch {
        val profile = repository.getUserProfileSync()
        val heightCm = profile?.heightCm ?: 170f
        val heightM = heightCm / 100f
        val bmi = if (heightM > 0f) kg / (heightM * heightM) else 0f
        repository.insertWeight(
            WeightLog(
                date = AppClock.todayString(),
                weightKg = kg,
                bmi = bmi,
                note = note
            )
        )
        saved.value = true
    }
}

// (Removed currentWorkoutDay — its day-of-week-modulo math drifted out of sync
// with the actual workoutPlan shape and the function had no live callers.)
