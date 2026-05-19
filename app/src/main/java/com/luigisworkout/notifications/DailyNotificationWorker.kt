package com.luigisworkout.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luigisworkout.LuigisWorkoutApp
import com.luigisworkout.MainActivity
import com.luigisworkout.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Daily meal/workout reminders. Runs as a [CoroutineWorker] so any future suspending
 * work (e.g. checking today's calorie progress before deciding whether to nag) can
 * slot in without a refactor.
 */
class DailyNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_TYPE = "notification_type"
        const val TYPE_BREAKFAST = "breakfast"
        const val TYPE_LUNCH = "lunch"
        const val TYPE_DINNER = "dinner"
        const val TYPE_WORKOUT = "workout"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val type = inputData.getString(KEY_TYPE) ?: TYPE_BREAKFAST
        sendNotification(type)
        Result.success()
    }

    private fun sendNotification(type: String) {
        // On Android 13+ posting silently fails without POST_NOTIFICATIONS. Bail out
        // cleanly so we don't burn battery on a no-op WorkManager job.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val payload = payloadFor(type) ?: return
        val pendingIntent = buildPendingIntent(payload.notifId)

        val notification = NotificationCompat.Builder(applicationContext, payload.channelId)
            .setSmallIcon(R.drawable.ic_workout)
            .setContentTitle(payload.title)
            .setContentText(payload.message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(payload.message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(payload.notifId, notification)
    }

    private fun payloadFor(type: String): NotifData? = when (type) {
        TYPE_BREAKFAST -> NotifData(
            applicationContext.getString(R.string.notif_breakfast_title),
            applicationContext.getString(R.string.notif_breakfast_text),
            LuigisWorkoutApp.MEAL_CHANNEL_ID, 1001
        )
        TYPE_LUNCH -> NotifData(
            applicationContext.getString(R.string.notif_lunch_title),
            applicationContext.getString(R.string.notif_lunch_text),
            LuigisWorkoutApp.MEAL_CHANNEL_ID, 1002
        )
        TYPE_DINNER -> NotifData(
            applicationContext.getString(R.string.notif_dinner_title),
            applicationContext.getString(R.string.notif_dinner_text),
            LuigisWorkoutApp.MEAL_CHANNEL_ID, 1003
        )
        TYPE_WORKOUT -> NotifData(
            applicationContext.getString(R.string.notif_workout_title),
            applicationContext.getString(R.string.notif_workout_text),
            LuigisWorkoutApp.WORKOUT_CHANNEL_ID, 1004
        )
        else -> null
    }

    /**
     * Build a PendingIntent that preserves the user's back stack. The old worker used
     * FLAG_ACTIVITY_NEW_TASK | CLEAR_TASK which wiped the app's back stack every time
     * a reminder fired — frustrating if the user was mid-workout.
     */
    private fun buildPendingIntent(requestCode: Int): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(intent)
            .getPendingIntent(
                requestCode,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ) ?: PendingIntent.getActivity(
                applicationContext, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
    }

    private data class NotifData(
        val title: String,
        val message: String,
        val channelId: String,
        val notifId: Int
    )
}
