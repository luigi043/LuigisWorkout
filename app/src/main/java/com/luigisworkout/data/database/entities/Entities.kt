package com.luigisworkout.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// ─── MEAL LOG ──────────────────────────────────────────────────────────────────

@Entity(tableName = "meal_logs")
data class MealLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,           // "yyyy-MM-dd"
    val mealType: String,       // "breakfast", "lunch", "dinner", "snack"
    val foodName: String,
    val calories: Int,
    val protein: Float = 0f,    // grams
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val quantity: Float = 1f,
    val unit: String = "porção",
    val timestamp: Long = System.currentTimeMillis()
)

// ─── WEIGHT LOG ────────────────────────────────────────────────────────────────

@Entity(tableName = "weight_logs")
data class WeightLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,           // "yyyy-MM-dd"
    val weightKg: Float,
    val bmi: Float = 0f,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

// ─── WORKOUT LOG ───────────────────────────────────────────────────────────────

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,           // "yyyy-MM-dd"
    val workoutName: String,
    val weekNumber: Int,
    val dayNumber: Int,
    val totalMinutes: Int,
    val exercisesCompleted: Int,
    val totalExercises: Int,
    val caloriesBurned: Int,
    val completed: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

// ─── EXERCISE LOG ──────────────────────────────────────────────────────────────

@Entity(tableName = "exercise_logs")
data class ExerciseLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val exerciseName: String,
    val weightKg: Float,
    val reps: Int,
    val sets: Int,
    val timestamp: Long = System.currentTimeMillis()
)

// ─── USER PROFILE ──────────────────────────────────────────────────────────────

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,    // singleton
    val name: String = "Luigi",
    val heightCm: Float = 175f,
    val startWeightKg: Float = 0f,
    val goalWeightKg: Float = 0f,
    val dailyCalorieGoal: Int = 1800,
    val currentWeekNumber: Int = 1,
    val startDate: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

// ─── DAILY GOAL ────────────────────────────────────────────────────────────────

@Entity(tableName = "daily_goals")
data class DailyGoal(
    @PrimaryKey val date: String,   // "yyyy-MM-dd"
    val calorieGoal: Int = 1800,
    val proteinGoal: Float = 120f,
    val waterGlasses: Int = 0,
    val waterGoal: Int = 8,
    val stepsCount: Int = 0,
    val stepsGoal: Int = 8000
)

// ─── SHOPPING LIST ─────────────────────────────────────────────────────────────

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: String = "",
    val supermarket: String = "Any",
    val isChecked: Boolean = false,
    val category: String = "General",
    val timestamp: Long = System.currentTimeMillis()
)