package com.luigisworkout.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.luigisworkout.data.database.AppDatabase
import com.luigisworkout.data.database.dao.DateCalories
import com.luigisworkout.data.database.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AppRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val mealLogDao = db.mealLogDao()
    private val weightLogDao = db.weightLogDao()
    private val workoutLogDao = db.workoutLogDao()
    private val userProfileDao = db.userProfileDao()
    private val dailyGoalDao = db.dailyGoalDao()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun today(): String = dateFormat.format(Date())
    fun dateXDaysAgo(days: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        return dateFormat.format(cal.time)
    }

    // ─── MEAL LOG ──────────────────────────────────────────────────────────────

    suspend fun insertMeal(log: MealLog) = withContext(Dispatchers.IO) { mealLogDao.insert(log) }
    suspend fun deleteMeal(log: MealLog) = withContext(Dispatchers.IO) { mealLogDao.delete(log) }
    fun getMealsByDate(date: String): LiveData<List<MealLog>> = mealLogDao.getByDate(date)
    fun getTodayCalories(): LiveData<Int?> = mealLogDao.getTotalCaloriesForDate(today())
    fun getCaloriesByDateRange(startDate: String): LiveData<List<DateCalories>> =
        mealLogDao.getCaloriesByDateRange(startDate)

    suspend fun getTodayMacros(): Triple<Float, Float, Float> = withContext(Dispatchers.IO) {
        val today = today()
        val p = mealLogDao.getTotalProteinForDate(today) ?: 0f
        val c = mealLogDao.getTotalCarbsForDate(today) ?: 0f
        val f = mealLogDao.getTotalFatForDate(today) ?: 0f
        Triple(p, c, f)
    }

    // ─── SHOPPING LIST ─────────────────────────────────────────────────────────

    private val shoppingItemDao = db.shoppingItemDao()

    fun getAllShoppingItems(): LiveData<List<ShoppingItem>> = shoppingItemDao.getAll()
    suspend fun insertShoppingItem(item: ShoppingItem) = withContext(Dispatchers.IO) { shoppingItemDao.insert(item) }
    suspend fun updateShoppingItem(item: ShoppingItem) = withContext(Dispatchers.IO) { shoppingItemDao.update(item) }
    suspend fun deleteShoppingItem(item: ShoppingItem) = withContext(Dispatchers.IO) { shoppingItemDao.delete(item) }
    suspend fun clearCheckedShoppingItems() = withContext(Dispatchers.IO) { shoppingItemDao.clearChecked() }

    suspend fun getRecentFoodNames(): List<String> = withContext(Dispatchers.IO) {
        mealLogDao.getRecentFoodNames()
    }

    fun getRecentMeals(): LiveData<List<MealLog>> = mealLogDao.getRecent()

    // ─── WEIGHT LOG ────────────────────────────────────────────────────────────

    suspend fun insertWeight(log: WeightLog) = withContext(Dispatchers.IO) { weightLogDao.insert(log) }
    suspend fun deleteWeight(log: WeightLog) = withContext(Dispatchers.IO) { weightLogDao.delete(log) }
    fun getAllWeights(): LiveData<List<WeightLog>> = weightLogDao.getAll()
    fun getLatestWeight(): LiveData<WeightLog?> = weightLogDao.getLatestLive()
    fun getWeightByDateRange(startDate: String): LiveData<List<WeightLog>> =
        weightLogDao.getByDateRange(startDate)

    // ─── WORKOUT LOG ───────────────────────────────────────────────────────────

    suspend fun insertWorkout(log: WorkoutLog) = withContext(Dispatchers.IO) { workoutLogDao.insert(log) }
    suspend fun updateWorkout(log: WorkoutLog) = withContext(Dispatchers.IO) { workoutLogDao.update(log) }
    fun getAllWorkouts(): LiveData<List<WorkoutLog>> = workoutLogDao.getAll()
    fun getTodayWorkout(): LiveData<WorkoutLog?> = workoutLogDao.getByDateLive(today())
    fun getCompletedWorkoutsCount(): LiveData<Int> = workoutLogDao.getCompletedCount()
    fun getRecentWeekWorkouts(): LiveData<List<WorkoutLog>> = workoutLogDao.getRecentWeek()

    suspend fun getTotalCaloriesBurned(): Int = withContext(Dispatchers.IO) {
        workoutLogDao.getTotalCaloriesBurned(dateXDaysAgo(30)) ?: 0
    }

    // ─── USER PROFILE ──────────────────────────────────────────────────────────

    fun getUserProfile(): LiveData<UserProfile?> = userProfileDao.get()

    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        val existing = userProfileDao.getSync()
        if (existing == null) userProfileDao.insert(profile)
        else userProfileDao.update(profile.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun getUserProfileSync(): UserProfile? = withContext(Dispatchers.IO) {
        userProfileDao.getSync()
    }

    // ─── DAILY GOAL ────────────────────────────────────────────────────────────

    fun getTodayGoal(): LiveData<DailyGoal?> = dailyGoalDao.getByDate(today())

    suspend fun updateWaterCount(glasses: Int) = withContext(Dispatchers.IO) {
        val date = today()
        val existing = dailyGoalDao.getByDateSync(date)
        if (existing == null) {
            dailyGoalDao.insert(DailyGoal(date = date, waterGlasses = glasses))
        } else {
            dailyGoalDao.update(existing.copy(waterGlasses = glasses))
        }
    }

    // ─── SUMMARY DATA ──────────────────────────────────────────────────────────

    suspend fun getWeekSummary(): WeekSummary = withContext(Dispatchers.IO) {
        val today = today()
        val weekAgo = dateXDaysAgo(7)
        val calories = mealLogDao.getTotalCaloriesForDateSync(today) ?: 0
        val workoutsCompleted = workoutLogDao.getTotalCaloriesBurned(weekAgo) ?: 0
        WeekSummary(
            todayCalories = calories,
            weekCaloriesBurned = workoutsCompleted
        )
    }
}

data class WeekSummary(
    val todayCalories: Int,
    val weekCaloriesBurned: Int
)