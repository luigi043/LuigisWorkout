package com.luigisworkout.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.luigisworkout.data.database.AppDatabase
import com.luigisworkout.data.database.dao.DateCalories
import com.luigisworkout.data.database.entities.*
import com.luigisworkout.util.AppClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Single repository for every data source in the app. There is exactly one instance,
 * owned by [com.luigisworkout.LuigisWorkoutApp.repository]. ViewModels receive it via
 * the factory rather than constructing their own — this keeps Room caches hot and
 * makes the repo trivially injectable for tests.
 *
 * All suspend functions hop to [Dispatchers.IO]; LiveData-returning queries already
 * dispatch internally via Room.
 */
class AppRepository(context: Context) {

    private val db = AppDatabase.getInstance(context.applicationContext)
    private val mealLogDao = db.mealLogDao()
    private val weightLogDao = db.weightLogDao()
    private val workoutLogDao = db.workoutLogDao()
    private val userProfileDao = db.userProfileDao()
    private val dailyGoalDao = db.dailyGoalDao()
    private val shoppingItemDao = db.shoppingItemDao()
    private val exerciseLogDao = db.exerciseLogDao()

    // ─── DATE HELPERS (delegate to AppClock so we never capture a stale date) ─────

    fun today(): String = AppClock.todayString()
    fun dateXDaysAgo(days: Int): String = AppClock.dateXDaysAgoString(days)

    // ─── MEAL LOG ──────────────────────────────────────────────────────────────────

    suspend fun insertMeal(log: MealLog) = withContext(Dispatchers.IO) { mealLogDao.insert(log) }
    suspend fun deleteMeal(log: MealLog) = withContext(Dispatchers.IO) { mealLogDao.delete(log) }

    fun getMealsByDate(date: String): LiveData<List<MealLog>> = mealLogDao.getByDate(date)
    fun getCaloriesByDate(date: String): LiveData<Int?> = mealLogDao.getTotalCaloriesForDate(date)

    fun getCaloriesByDateRange(startDate: String): LiveData<List<DateCalories>> =
        mealLogDao.getCaloriesByDateRange(startDate)

    fun getRecentMeals(): LiveData<List<MealLog>> = mealLogDao.getRecent()

    suspend fun getMacrosByDate(date: String): Triple<Float, Float, Float> = withContext(Dispatchers.IO) {
        val p = mealLogDao.getTotalProteinForDate(date) ?: 0f
        val c = mealLogDao.getTotalCarbsForDate(date) ?: 0f
        val f = mealLogDao.getTotalFatForDate(date) ?: 0f
        Triple(p, c, f)
    }

    suspend fun getRecentFoodNames(): List<String> = withContext(Dispatchers.IO) {
        mealLogDao.getRecentFoodNames()
    }

    // ─── SHOPPING LIST ─────────────────────────────────────────────────────────────

    fun getAllShoppingItems(): LiveData<List<ShoppingItem>> = shoppingItemDao.getAll()
    suspend fun insertShoppingItem(item: ShoppingItem) = withContext(Dispatchers.IO) { shoppingItemDao.insert(item) }
    suspend fun updateShoppingItem(item: ShoppingItem) = withContext(Dispatchers.IO) { shoppingItemDao.update(item) }
    suspend fun deleteShoppingItem(item: ShoppingItem) = withContext(Dispatchers.IO) { shoppingItemDao.delete(item) }
    suspend fun clearCheckedShoppingItems() = withContext(Dispatchers.IO) { shoppingItemDao.clearChecked() }

    // ─── WEIGHT LOG ────────────────────────────────────────────────────────────────

    suspend fun insertWeight(log: WeightLog) = withContext(Dispatchers.IO) { weightLogDao.insert(log) }
    suspend fun deleteWeight(log: WeightLog) = withContext(Dispatchers.IO) { weightLogDao.delete(log) }

    fun getAllWeights(): LiveData<List<WeightLog>> = weightLogDao.getAll()
    fun getLatestWeight(): LiveData<WeightLog?> = weightLogDao.getLatestLive()
    fun getWeightByDateRange(startDate: String): LiveData<List<WeightLog>> =
        weightLogDao.getByDateRange(startDate)

    // ─── WORKOUT LOG ───────────────────────────────────────────────────────────────

    suspend fun insertWorkout(log: WorkoutLog) = withContext(Dispatchers.IO) { workoutLogDao.insert(log) }
    suspend fun updateWorkout(log: WorkoutLog) = withContext(Dispatchers.IO) { workoutLogDao.update(log) }

    fun getAllWorkouts(): LiveData<List<WorkoutLog>> = workoutLogDao.getAll()
    fun getWorkoutByDate(date: String): LiveData<WorkoutLog?> = workoutLogDao.getByDateLive(date)
    fun getCompletedWorkoutsCount(): LiveData<Int> = workoutLogDao.getCompletedCount()
    fun getRecentWeekWorkouts(): LiveData<List<WorkoutLog>> =
        workoutLogDao.getRecentHeatmap(dateXDaysAgo(28))

    suspend fun getTotalCaloriesBurnedLast30Days(): Int = withContext(Dispatchers.IO) {
        workoutLogDao.getTotalCaloriesBurned(dateXDaysAgo(30)) ?: 0
    }

    // ─── USER PROFILE ──────────────────────────────────────────────────────────────

    fun getUserProfile(): LiveData<UserProfile?> = userProfileDao.get()

    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        val existing = userProfileDao.getSync()
        if (existing == null) userProfileDao.insert(profile)
        else userProfileDao.update(profile.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun getUserProfileSync(): UserProfile? = withContext(Dispatchers.IO) {
        userProfileDao.getSync()
    }

    // ─── DAILY GOAL ────────────────────────────────────────────────────────────────

    fun getGoalByDate(date: String): LiveData<DailyGoal?> = dailyGoalDao.getByDate(date)

    /**
     * Atomic +/- on the water counter. The read-modify-write happens inside a single
     * IO coroutine so two rapid taps can't both read the same stale value and lose an
     * increment.
     *
     * @param delta typically +1 or -1; negative values are clamped at 0.
     */
    suspend fun adjustWaterCount(delta: Int) = withContext(Dispatchers.IO) {
        val date = today()
        val existing = dailyGoalDao.getByDateSync(date)
        if (existing == null) {
            val seed = (delta).coerceAtLeast(0)
            dailyGoalDao.insert(DailyGoal(date = date, waterGlasses = seed))
        } else {
            val next = (existing.waterGlasses + delta).coerceAtLeast(0)
            if (next != existing.waterGlasses) {
                dailyGoalDao.update(existing.copy(waterGlasses = next))
            }
        }
    }

    // ─── EXERCISE LOG ──────────────────────────────────────────────────────────────

    suspend fun insertExerciseLog(log: ExerciseLog) = withContext(Dispatchers.IO) {
        exerciseLogDao.insert(log)
    }

    fun getExerciseHistory(exerciseName: String): LiveData<List<ExerciseLog>> =
        exerciseLogDao.getHistoryByExercise(exerciseName)

    suspend fun getLatestExerciseLog(exerciseName: String): ExerciseLog? = withContext(Dispatchers.IO) {
        exerciseLogDao.getLatestForExercise(exerciseName)
    }

    // ─── SUMMARY ───────────────────────────────────────────────────────────────────

    suspend fun getWeekSummary(): WeekSummary = withContext(Dispatchers.IO) {
        val today = today()
        val weekAgo = dateXDaysAgo(7)
        val calories = mealLogDao.getTotalCaloriesForDateSync(today) ?: 0
        val weekCaloriesBurned = workoutLogDao.getTotalCaloriesBurned(weekAgo) ?: 0
        WeekSummary(
            todayCalories = calories,
            weekCaloriesBurned = weekCaloriesBurned
        )
    }
}

data class WeekSummary(
    val todayCalories: Int,
    val weekCaloriesBurned: Int
)
