package com.luigisworkout

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*
import com.luigisworkout.data.repository.AppRepository
import com.luigisworkout.notifications.DailyNotificationWorker
import com.luigisworkout.util.TodayDateLiveData
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Application singleton. Owns the [AppRepository] and the shared [TodayDateLiveData]
 * — both are constructed lazily and lifetime-bound to the process, so every screen
 * shares one Room handle and one date-rollover broadcast receiver.
 *
 * Pull them via the [luigiApp] extension below to avoid casts at every call site.
 */
class LuigisWorkoutApp : Application() {

    /** Single Room-backed repository instance for the whole app. */
    val repository: AppRepository by lazy { AppRepository(this) }

    /** Re-emits whenever the device crosses midnight. Use as a `switchMap` trigger. */
    val todayDate: TodayDateLiveData by lazy { TodayDateLiveData(this) }

    companion object {
        const val MEAL_CHANNEL_ID = "luigi_meal_channel"
        const val WORKOUT_CHANNEL_ID = "luigi_workout_channel"
        const val GENERAL_CHANNEL_ID = "luigi_general_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        scheduleDailyNotifications()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NotificationManager::class.java) ?: return

        val mealChannel = NotificationChannel(
            MEAL_CHANNEL_ID,
            "Meal Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Daily reminders to log your meals and hit your goals" }

        val workoutChannel = NotificationChannel(
            WORKOUT_CHANNEL_ID,
            "Workout Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Daily reminders for your home workout plan" }

        val generalChannel = NotificationChannel(
            GENERAL_CHANNEL_ID,
            "General",
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = "General notifications for Luigi's Workout" }

        nm.createNotificationChannels(listOf(mealChannel, workoutChannel, generalChannel))
    }

    private fun scheduleDailyNotifications() {
        schedulePeriodicNotification("morning_meal_reminder", 8, 0, DailyNotificationWorker.TYPE_BREAKFAST)
        schedulePeriodicNotification("lunch_reminder", 13, 0, DailyNotificationWorker.TYPE_LUNCH)
        schedulePeriodicNotification("workout_reminder", 18, 0, DailyNotificationWorker.TYPE_WORKOUT)
        schedulePeriodicNotification("dinner_reminder", 19, 30, DailyNotificationWorker.TYPE_DINNER)
    }

    private fun schedulePeriodicNotification(tag: String, hour: Int, minute: Int, type: String) {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (target.before(now)) target.add(Calendar.DAY_OF_YEAR, 1)
        val initialDelay = target.timeInMillis - now.timeInMillis

        val data = Data.Builder()
            .putString(DailyNotificationWorker.KEY_TYPE, type)
            .build()

        val request = PeriodicWorkRequestBuilder<DailyNotificationWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(tag)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}

/** Sugar: get the [LuigisWorkoutApp] from any [Context]. */
val Context.luigiApp: LuigisWorkoutApp
    get() = applicationContext as LuigisWorkoutApp
