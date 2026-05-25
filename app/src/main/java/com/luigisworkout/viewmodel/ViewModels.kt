package com.luigisworkout.viewmodel

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
    initializer { WorkoutViewModel(app.repository, app.todayDate) }
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
    todayDate: LiveData<String>
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

    // ─── Timer state (survives Fragment view recreation via the ViewModel) ───

    var isWorkoutActive: Boolean = false
        private set
    var workoutStartTime: Long = 0L
        private set

    private var timerJob: Job? = null

    private val _elapsedTime = MutableLiveData("⏱ 00:00")
    val elapsedTime: LiveData<String> = _elapsedTime

    fun startWorkout() {
        if (isWorkoutActive) return
        isWorkoutActive = true
        if (workoutStartTime == 0L) workoutStartTime = System.currentTimeMillis()
        startTimer()
    }

    fun pauseWorkout() {
        isWorkoutActive = false
        timerJob?.cancel()
    }

    fun resumeWorkout() {
        if (isWorkoutActive) return
        isWorkoutActive = true
        startTimer()
    }

    fun resetWorkout() {
        isWorkoutActive = false
        workoutStartTime = 0L
        timerJob?.cancel()
        _elapsedTime.value = "⏱ 00:00"
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            // viewModelScope runs on Main, so `value =` is safe and avoids the
            // small postValue() coalescing delay that made the timer visibly jitter.
            while (isWorkoutActive) {
                val elapsed = (System.currentTimeMillis() - workoutStartTime) / 1000
                val minutes = elapsed / 60
                val seconds = elapsed % 60
                _elapsedTime.value = String.format(Locale.getDefault(), "⏱ %02d:%02d", minutes, seconds)
                delay(1000)
            }
        }
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
