package com.luigisworkout.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.luigisworkout.data.database.entities.*

// ─── MEAL LOG DAO ──────────────────────────────────────────────────────────────

@Dao
interface MealLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: MealLog): Long

    @Delete
    suspend fun delete(log: MealLog)

    @Update
    suspend fun update(log: MealLog)

    @Query("SELECT * FROM meal_logs WHERE date = :date ORDER BY timestamp ASC")
    fun getByDate(date: String): LiveData<List<MealLog>>

    @Query("SELECT * FROM meal_logs WHERE date = :date ORDER BY timestamp ASC")
    suspend fun getByDateSync(date: String): List<MealLog>

    @Query("SELECT SUM(calories) FROM meal_logs WHERE date = :date")
    fun getTotalCaloriesForDate(date: String): LiveData<Int?>

    @Query("SELECT SUM(calories) FROM meal_logs WHERE date = :date")
    suspend fun getTotalCaloriesForDateSync(date: String): Int?

    @Query("SELECT SUM(protein) FROM meal_logs WHERE date = :date")
    suspend fun getTotalProteinForDate(date: String): Float?

    @Query("SELECT SUM(carbs) FROM meal_logs WHERE date = :date")
    suspend fun getTotalCarbsForDate(date: String): Float?

    @Query("SELECT SUM(fat) FROM meal_logs WHERE date = :date")
    suspend fun getTotalFatForDate(date: String): Float?

    @Query("SELECT date, SUM(calories) as calories FROM meal_logs WHERE date >= :startDate GROUP BY date ORDER BY date ASC")
    fun getCaloriesByDateRange(startDate: String): LiveData<List<DateCalories>>

    @Query("SELECT * FROM meal_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecent(): LiveData<List<MealLog>>

    @Query("SELECT DISTINCT foodName FROM meal_logs ORDER BY timestamp DESC LIMIT 30")
    suspend fun getRecentFoodNames(): List<String>
}

data class DateCalories(val date: String, val calories: Int)

// ─── WEIGHT LOG DAO ────────────────────────────────────────────────────────────

@Dao
interface WeightLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WeightLog): Long

    @Delete
    suspend fun delete(log: WeightLog)

    @Query("SELECT * FROM weight_logs ORDER BY date DESC")
    fun getAll(): LiveData<List<WeightLog>>

    @Query("SELECT * FROM weight_logs ORDER BY date DESC LIMIT 1")
    suspend fun getLatest(): WeightLog?

    @Query("SELECT * FROM weight_logs ORDER BY date DESC LIMIT 1")
    fun getLatestLive(): LiveData<WeightLog?>

    @Query("SELECT * FROM weight_logs WHERE date >= :startDate ORDER BY date ASC")
    fun getByDateRange(startDate: String): LiveData<List<WeightLog>>

    @Query("SELECT COUNT(*) FROM weight_logs")
    suspend fun getCount(): Int
}

// ─── WORKOUT LOG DAO ───────────────────────────────────────────────────────────

@Dao
interface WorkoutLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WorkoutLog): Long

    @Update
    suspend fun update(log: WorkoutLog)

    @Delete
    suspend fun delete(log: WorkoutLog)

    @Query("SELECT * FROM workout_logs ORDER BY date DESC")
    fun getAll(): LiveData<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE date = :date")
    suspend fun getByDate(date: String): WorkoutLog?

    @Query("SELECT * FROM workout_logs WHERE date = :date")
    fun getByDateLive(date: String): LiveData<WorkoutLog?>

    @Query("SELECT COUNT(*) FROM workout_logs WHERE completed = 1")
    fun getCompletedCount(): LiveData<Int>

    @Query("SELECT * FROM workout_logs WHERE date >= :startDate ORDER BY date ASC")
    fun getByDateRange(startDate: String): LiveData<List<WorkoutLog>>

    @Query("SELECT SUM(caloriesBurned) FROM workout_logs WHERE date >= :startDate")
    suspend fun getTotalCaloriesBurned(startDate: String): Int?

    @Query("SELECT * FROM workout_logs ORDER BY date DESC LIMIT 7")
    fun getRecentWeek(): LiveData<List<WorkoutLog>>
}

// ─── USER PROFILE DAO ──────────────────────────────────────────────────────────

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfile)

    @Update
    suspend fun update(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun get(): LiveData<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getSync(): UserProfile?
}

// ─── DAILY GOAL DAO ────────────────────────────────────────────────────────────

@Dao
interface DailyGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: DailyGoal)

    @Update
    suspend fun update(goal: DailyGoal)

    @Query("SELECT * FROM daily_goals WHERE date = :date")
    fun getByDate(date: String): LiveData<DailyGoal?>

    @Query("SELECT * FROM daily_goals WHERE date = :date")
    suspend fun getByDateSync(date: String): DailyGoal?
}

// ─── SHOPPING ITEM DAO ─────────────────────────────────────────────────────────

@Dao
interface ShoppingItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem): Long

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("SELECT * FROM shopping_items ORDER BY isChecked ASC, timestamp DESC")
    fun getAll(): LiveData<List<ShoppingItem>>

    @Query("DELETE FROM shopping_items WHERE isChecked = 1")
    suspend fun clearChecked()

    @Query("SELECT * FROM shopping_items WHERE supermarket = :supermarket ORDER BY timestamp DESC")
    fun getBySupermarket(supermarket: String): LiveData<List<ShoppingItem>>
}