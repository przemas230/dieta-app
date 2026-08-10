package com.przemas230.dietaapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.przemas230.dietaapp.R

/** Broadcast actions + notification/channel ids shared between [WaterNotifications], [WaterActionReceiver] and [WaterReminderAlarmReceiver]. */
object WaterNotificationActions {
    const val ACTION_ADD_WATER = "com.przemas230.dietaapp.action.ADD_WATER"
    const val ACTION_REMOVE_WATER = "com.przemas230.dietaapp.action.REMOVE_WATER"
    const val ACTION_FIRE_REMINDER = "com.przemas230.dietaapp.action.FIRE_REMINDER"
    const val ACTION_SNOOZE_REMINDER = "com.przemas230.dietaapp.action.SNOOZE_REMINDER"
    const val ACTION_SKIP_REMINDER = "com.przemas230.dietaapp.action.SKIP_REMINDER"

    const val CHANNEL_TRACKER = "water_tracker"
    const val CHANNEL_REMINDER = "water_reminder"
    const val NOTIF_ID_TRACKER = 1001
    const val NOTIF_ID_REMINDER = 1002
}

/**
 * FR-38/39: builds and shows the two water notifications -- the persistent
 * "+1/-1" tracker (index.html's `refreshWaterNotification`/`pushWaterNotification`)
 * and the periodic "time to drink" reminder with snooze/skip
 * (`fireWaterReminderNotification`/`pushWaterReminderNotification`). Actions
 * route to [WaterActionReceiver], not an Activity -- same "don't open the
 * app just to tap a button" intent as the web version's service-worker
 * notification actions.
 */
object WaterNotifications {
    private fun pendingIntentFlags() =
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        manager.createNotificationChannel(
            NotificationChannel(
                WaterNotificationActions.CHANNEL_TRACKER,
                "Licznik nawodnienia",
                NotificationManager.IMPORTANCE_LOW,
            ).apply { description = "Stałe powiadomienie z przyciskami +1/-1 do liczenia wypitych szklanek wody." },
        )
        manager.createNotificationChannel(
            NotificationChannel(
                WaterNotificationActions.CHANNEL_REMINDER,
                "Przypomnienie o piciu wody",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply { description = "Cykliczne przypomnienie o wypiciu szklanki wody." },
        )
    }

    private fun waterDropletsText(count: Int): String =
        (0 until 8).joinToString("") { if (it < count) "💧" else "⚪" }

    private fun actionBroadcast(context: Context, action: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, WaterActionReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(context, requestCode, intent, pendingIntentFlags())
    }

    fun showTrackerNotification(context: Context, count: Int) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return
        ensureChannels(context)
        val notification = NotificationCompat.Builder(context, WaterNotificationActions.CHANNEL_TRACKER)
            .setSmallIcon(R.drawable.ic_notification_water)
            .setContentTitle("💧 Nawodnienie")
            .setContentText("${waterDropletsText(count)}  $count / 8 szklanek")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSilent(true)
            .setOngoing(false)
            .setAutoCancel(false)
            .addAction(0, "+1 💧", actionBroadcast(context, WaterNotificationActions.ACTION_ADD_WATER, 1))
            .addAction(0, "-1 ↩️", actionBroadcast(context, WaterNotificationActions.ACTION_REMOVE_WATER, 2))
            .build()
        NotificationManagerCompat.from(context).notify(WaterNotificationActions.NOTIF_ID_TRACKER, notification)
    }

    fun cancelTrackerNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(WaterNotificationActions.NOTIF_ID_TRACKER)
    }

    fun showReminderNotification(context: Context) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return
        ensureChannels(context)
        val notification = NotificationCompat.Builder(context, WaterNotificationActions.CHANNEL_REMINDER)
            .setSmallIcon(R.drawable.ic_notification_water)
            .setContentTitle("💧 Czas się napić wody!")
            .setContentText("Krótkie przypomnienie o nawodnieniu.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(0, "Odłóż 15 min", actionBroadcast(context, WaterNotificationActions.ACTION_SNOOZE_REMINDER, 3))
            .addAction(0, "Pomiń do następnego", actionBroadcast(context, WaterNotificationActions.ACTION_SKIP_REMINDER, 4))
            .build()
        NotificationManagerCompat.from(context).notify(WaterNotificationActions.NOTIF_ID_REMINDER, notification)
    }

    fun cancelReminderNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(WaterNotificationActions.NOTIF_ID_REMINDER)
    }
}
