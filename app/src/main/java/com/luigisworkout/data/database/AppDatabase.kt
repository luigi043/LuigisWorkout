package com.luigisworkout.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luigisworkout.data.database.dao.*
import com.luigisworkout.data.database.entities.*

@Database(
    entities = [
        MealLog::class,
        WeightLog::class,
        WorkoutLog::class,
        UserProfile::class,
        DailyGoal::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mealLogDao(): MealLogDao
    abstract fun weightLogDao(): WeightLogDao
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun dailyGoalDao(): DailyGoalDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "luigis_workout_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}