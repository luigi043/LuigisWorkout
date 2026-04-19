package com.luigisworkout.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.luigisworkout.data.AppContent
import com.luigisworkout.data.WorkoutDay
import com.luigisworkout.data.database.entities.*
import com.luigisworkout.data.repository.AppRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// ─── SHARED APP VIEWMODEL ──────────────────────────────────────────────────────

class AppViewModel(application: Application) : AndroidViewModel(application) {

    val repository = AppRepository(application)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun today() = dateFormat.format(Date())

    // User Profile
    val userProfile: LiveData<UserProfile?> = repository.getUserProfile()

    fun saveProfile(profile: UserProfile) = viewModelScope.launch {
        repository.saveUserProfile(profile)
    }

    // Today's calories
    val todayCalories: LiveData<Int?> = repository.getTodayCalories()

    // Today's meals
    fun getTodayMeals() = repository.getMealsByDate(today())

    // Water tracking
    val todayGoal: LiveData<DailyGoal?> = repository.getTodayGoal()

    fun addWaterGlass() = viewModelScope.launch {
        val current = todayGoal.value?.waterGlasses ?: 0
        repository.updateWaterCount(current + 1)
    }

    fun removeWaterGlass() = viewModelScope.launch {
        val current = todayGoal.value?.waterGlasses ?: 0
        if (current > 0) repository.updateWaterCount(current - 1)
    }

    // Today's workout
    val todayWorkout: LiveData<WorkoutLog?> = repository.getTodayWorkout()

    // Weight
    val latestWeight: LiveData<WeightLog?> = repository.getLatestWeight()

    // Motivational tip
    val dailyTip: String get() {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        return AppContent.motivationalTips[dayOfYear % AppContent.motivationalTips.size]
    }

    // Current workout plan day
    fun getCurrentWorkoutDay(profile: UserProfile?): WorkoutDay? {
        if (profile == null) return null
        val weekNum = profile.currentWeekNumber.coerceIn(1, 12)
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) // 1=Sunday
        // Map day of week to workout day (1-5 depending on week)
        val workoutDayNum = when (weekNum) {
            in 1..4 -> ((dayOfWeek - 2).mod(7) % 3) + 1
            in 5..8 -> ((dayOfWeek - 2).mod(7) % 4) + 1
            else -> ((dayOfWeek - 2).mod(7) % 5) + 1
        }
        return AppContent.workoutPlan.firstOrNull {
            it.weekNumber == weekNum && it.dayNumber == workoutDayNum
        }
    }
}

// ─── HOME VIEWMODEL ───────────────────────────────────────────────────────────

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    val todayCalories = repository.getTodayCalories()
    val todayMeals = repository.getMealsByDate(repository.today())
    val latestWeight = repository.getLatestWeight()
    val todayGoal = repository.getTodayGoal()
    val completedWorkouts = repository.getCompletedWorkoutsCount()
    val todayWorkout = repository.getTodayWorkout()

    private val _macros = MutableLiveData<Triple<Float, Float, Float>>()
    val macros: LiveData<Triple<Float, Float, Float>> = _macros

    fun refreshMacros() = viewModelScope.launch {
        _macros.value = repository.getTodayMacros()
    }

    fun addWater() = viewModelScope.launch {
        val current = todayGoal.value?.waterGlasses ?: 0
        repository.updateWaterCount(current + 1)
    }

    fun removeWater() = viewModelScope.launch {
        val current = todayGoal.value?.waterGlasses ?: 0
        if (current > 0) repository.updateWaterCount(current - 1)
    }
}

// ─── MEAL PLAN VIEWMODEL ──────────────────────────────────────────────────────

class MealPlanViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    val breakfastRecipes = AppContent.recipes.filter { it.mealType == "breakfast" }
    val lunchRecipes = AppContent.recipes.filter { it.mealType == "lunch" }
    val dinnerRecipes = AppContent.recipes.filter { it.mealType == "dinner" }
    val snackRecipes = AppContent.recipes.filter { it.mealType == "snack" }

    val todayMeals = repository.getMealsByDate(repository.today())
    val todayCalories = repository.getTodayCalories()
    val recentMeals = repository.getRecentMeals()

    val foodDatabase = AppContent.foodDatabase

    fun deleteMeal(log: MealLog) = viewModelScope.launch { repository.deleteMeal(log) }

    suspend fun getRecentFoodNames() = repository.getRecentFoodNames()

    // Shopping List
    val shoppingItems = repository.getAllShoppingItems()

    fun addShoppingItem(name: String, quantity: String = "", supermarket: String = "Any") = viewModelScope.launch {
        repository.insertShoppingItem(ShoppingItem(name = name, quantity = quantity, supermarket = supermarket))
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

class LogMealViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    val foodDatabase = AppContent.foodDatabase

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

    fun saveMeal(log: MealLog) = viewModelScope.launch {
        repository.insertMeal(log)
        _saved.value = true
    }
}

// ─── WORKOUT VIEWMODEL ────────────────────────────────────────────────────────

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    val allWorkouts = repository.getAllWorkouts()
    val completedCount = repository.getCompletedWorkoutsCount()
    val recentWeek = repository.getRecentWeekWorkouts()
    val todayWorkout = repository.getTodayWorkout()

    val fullPlan = AppContent.workoutPlan

    fun getWorkoutsByWeek(week: Int) = fullPlan.filter { it.weekNumber == week }

    fun logWorkout(
        workoutDay: com.luigisworkout.data.WorkoutDay,
        completedExercises: Int,
        minutes: Int
    ) = viewModelScope.launch {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())
        val log = WorkoutLog(
            date = today,
            workoutName = workoutDay.name,
            weekNumber = workoutDay.weekNumber,
            dayNumber = workoutDay.dayNumber,
            totalMinutes = minutes,
            exercisesCompleted = completedExercises,
            totalExercises = workoutDay.exercises.size,
            caloriesBurned = workoutDay.caloriesBurned * minutes / workoutDay.estimatedMinutes,
            completed = completedExercises == workoutDay.exercises.size
        )
        repository.insertWorkout(log)
    }
}

// ─── PROGRESS VIEWMODEL ───────────────────────────────────────────────────────

class ProgressViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    val allWeights = repository.getAllWeights()
    val weightLast30Days = repository.getWeightByDateRange(repository.dateXDaysAgo(30))
    val caloriesLast30Days = repository.getCaloriesByDateRange(repository.dateXDaysAgo(30))
    val completedWorkouts = repository.getCompletedWorkoutsCount()
    val userProfile = repository.getUserProfile()

    fun insertWeight(kg: Float, heightCm: Float, note: String = "") = viewModelScope.launch {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())
        val bmi = if (heightCm > 0) kg / ((heightCm / 100f) * (heightCm / 100f)) else 0f
        repository.insertWeight(WeightLog(
            date = today,
            weightKg = kg,
            bmi = bmi,
            note = note
        ))
    }

    fun deleteWeight(log: WeightLog) = viewModelScope.launch {
        repository.deleteWeight(log)
    }

    private val _totalCaloriesBurned = MutableLiveData(0)
    val totalCaloriesBurned: LiveData<Int> = _totalCaloriesBurned

    fun refreshStats() = viewModelScope.launch {
        _totalCaloriesBurned.value = repository.getTotalCaloriesBurned()
    }
}

// ─── LOG WEIGHT VIEWMODEL ────────────────────────────────────────────────────

class LogWeightViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

    fun saveWeight(kg: Float, note: String = "") = viewModelScope.launch {
        val profile = repository.getUserProfileSync()
        val heightCm = profile?.heightCm ?: 170f
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())
        val bmi = kg / ((heightCm / 100f) * (heightCm / 100f))
        repository.insertWeight(WeightLog(
            date = today,
            weightKg = kg,
            bmi = bmi,
            note = note
        ))
        _saved.value = true
    }
}

// ─── VIEWMODEL FACTORY ────────────────────────────────────────────────────────

class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel(application) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(application) as T
            modelClass.isAssignableFrom(MealPlanViewModel::class.java) -> MealPlanViewModel(application) as T
            modelClass.isAssignableFrom(WorkoutViewModel::class.java) -> WorkoutViewModel(application) as T
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(application) as T
            modelClass.isAssignableFrom(LogMealViewModel::class.java) -> LogMealViewModel(application) as T
            modelClass.isAssignableFrom(LogWeightViewModel::class.java) -> LogWeightViewModel(application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}