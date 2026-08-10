package com.przemas230.dietaapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

/**
 * FR-39: schedules/cancels the single one-shot alarm that fires the next
 * water reminder. Deliberately inexact (`setAndAllowWhileIdle`, not
 * `setExactAndAllowWhileIdle`) -- a reminder landing a few minutes late in
 * Doze is an acceptable trade for not requiring the user to separately grant
 * "Alarms & reminders" (SCHEDULE_EXACT_ALARM, API 31+), matching the web
 * version's own imprecision (30s polling interval while a tab happens to be open).
 * [WaterReminderAlarmReceiver] re-schedules the next one every time this fires
 * or a snooze/skip action runs -- there's no repeating alarm, since the
 * interval/active-window math (WaterReminderScheduling) needs to run fresh each time.
 */
object AlarmScheduler {
    private const val REQUEST_CODE = 42

    private fun pendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, WaterReminderAlarmReceiver::class.java)
            .setAction(WaterNotificationActions.ACTION_FIRE_REMINDER)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    fun schedule(context: Context, atMillis: Long) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, atMillis, pendingIntent(context))
    }

    fun cancel(context: Context) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        manager.cancel(pendingIntent(context))
    }
}
