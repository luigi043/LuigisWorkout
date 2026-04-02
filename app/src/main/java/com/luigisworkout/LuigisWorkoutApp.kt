package com.luigisworkout

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.*
import com.luigisworkout.notifications.DailyNotificationWorker
import java.util.concurrent.TimeUnit
import java.util.Calendar

class LuigisWorkoutApp : Application() {

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)

            val mealChannel = NotificationChannel(
                MEAL_CHANNEL_ID,
                "Lembretes de Refeições",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Lembretes diários para registar refeições e atingir os teus objetivos"
            }

            val workoutChannel = NotificationChannel(
                WORKOUT_CHANNEL_ID,
                "Lembretes de Treino",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Lembretes diários do teu plano de treino em casa"
            }

            val generalChannel = NotificationChannel(
                GENERAL_CHANNEL_ID,
                "Geral",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificações gerais do Luigi's Workout"
            }

            nm.createNotificationChannels(listOf(mealChannel, workoutChannel, generalChannel))
        }
    }

    private fun scheduleDailyNotifications() {
        val constraints = Constraints.Builder()
            .build()

        // Morning meal reminder at 08:00
        schedulePeriodicNotification(
            "morning_meal_reminder",
            8, 0,
            DailyNotificationWorker.TYPE_BREAKFAST
        )

        // Lunch reminder at 13:00
        schedulePeriodicNotification(
            "lunch_reminder",
            13, 0,
            DailyNotificationWorker.TYPE_LUNCH
        )

        // Workout reminder at 18:00
        schedulePeriodicNotification(
            "workout_reminder",
            18, 0,
            DailyNotificationWorker.TYPE_WORKOUT
        )

        // Dinner reminder at 19:30
        schedulePeriodicNotification(
            "dinner_reminder",
            19, 30,
            DailyNotificationWorker.TYPE_DINNER
        )
    }

    private fun schedulePeriodicNotification(tag: String, hour: Int, minute: Int, type: String) {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
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