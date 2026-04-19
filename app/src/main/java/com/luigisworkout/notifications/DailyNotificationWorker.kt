package com.luigisworkout.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.luigisworkout.LuigisWorkoutApp
import com.luigisworkout.MainActivity
import com.luigisworkout.R

class DailyNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        const val KEY_TYPE = "notification_type"
        const val TYPE_BREAKFAST = "breakfast"
        const val TYPE_LUNCH = "lunch"
        const val TYPE_DINNER = "dinner"
        const val TYPE_WORKOUT = "workout"
    }

    override fun doWork(): Result {
        val type = inputData.getString(KEY_TYPE) ?: TYPE_BREAKFAST
        sendNotification(type)
        return Result.success()
    }

    private fun sendNotification(type: String) {
        val (title, message, channelId, notifId) = when (type) {
            TYPE_BREAKFAST -> NotifData(
                applicationContext.getString(R.string.notif_breakfast_title),
                applicationContext.getString(R.string.notif_breakfast_text),
                LuigisWorkoutApp.MEAL_CHANNEL_ID,
                1001
            )
            TYPE_LUNCH -> NotifData(
                applicationContext.getString(R.string.notif_lunch_title),
                applicationContext.getString(R.string.notif_lunch_text),
                LuigisWorkoutApp.MEAL_CHANNEL_ID,
                1002
            )
            TYPE_DINNER -> NotifData(
                applicationContext.getString(R.string.notif_dinner_title),
                applicationContext.getString(R.string.notif_dinner_text),
                LuigisWorkoutApp.MEAL_CHANNEL_ID,
                1003
            )
            TYPE_WORKOUT -> NotifData(
                applicationContext.getString(R.string.notif_workout_title),
                applicationContext.getString(R.string.notif_workout_text),
                LuigisWorkoutApp.WORKOUT_CHANNEL_ID,
                1004
            )
            else -> return
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, notifId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(notifId, notification)
    }

    private data class NotifData(
        val title: String,
        val message: String,
        val channelId: String,
        val notifId: Int
    )
}
