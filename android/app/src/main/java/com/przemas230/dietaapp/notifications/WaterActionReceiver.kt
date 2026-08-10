package com.przemas230.dietaapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.przemas230.dietaapp.data.WaterNotificationStore
import com.przemas230.dietaapp.logic.WaterOperations
import com.przemas230.dietaapp.logic.WaterReminderScheduling

/**
 * FR-38/39: handles taps on the notification action buttons -- the water
 * tracker's "+1"/"-1" and the reminder's "Odłóż 15 min"/"Pomiń do następnego".
 * Port of sw.js's `handleWaterTrackerAction`/`notificationclick` handler,
 * minus the add/remove button-order swap (that was working around a
 * documented Chrome-on-Android web-push bug -- native NotificationCompat
 * actions aren't affected, order matches the visible +1/-1 labels directly).
 */
class WaterActionReceiver : BroadcastReceiver() {
    // Same 150ms guard as sw.js's lastWaterActionAt: long enough to swallow a
    // duplicate dispatch of one physical tap, short enough to never eat a
    // deliberate fast second tap.
    private val dedupWindowMs = 150L

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WaterNotificationActions.ACTION_ADD_WATER, WaterNotificationActions.ACTION_REMOVE_WATER ->
                handleTrackerAction(context, intent.action!!)
            WaterNotificationActions.ACTION_SNOOZE_REMINDER -> handleReminderResponse(context, snooze = true)
            WaterNotificationActions.ACTION_SKIP_REMINDER -> handleReminderResponse(context, snooze = false)
        }
    }

    private fun handleTrackerAction(context: Context, action: String) {
        val now = System.currentTimeMillis()
        if (!WaterNotificationStore.claimActionSlot(context, now, dedupWindowMs)) {
            WaterNotificationStore.appendLog(
                context,
                WaterNotificationStore.LogEntry(now, action, "swallowed-duplicate", null, null),
            )
            return
        }
        val countBefore = WaterNotificationStore.readPendingWaterCount(context)
        val delta = if (action == WaterNotificationActions.ACTION_ADD_WATER) 1 else -1
        val countAfter = (countBefore + delta).coerceIn(0, WaterOperations.MAX_LEVEL)
        WaterNotificationStore.writePendingWaterCount(context, countAfter)
        WaterNotificationStore.appendLog(
            context,
            WaterNotificationStore.LogEntry(now, action, "ok", countBefore, countAfter),
        )
        WaterNotifications.showTrackerNotification(context, countAfter)
        WaterNotificationBridge.onWaterCountChanged?.invoke(countAfter)
    }

    private fun handleReminderResponse(context: Context, snooze: Boolean) {
        val config = WaterNotificationStore.readReminderConfig(context)
        val now = System.currentTimeMillis()
        val nextAt = if (snooze) {
            now + WaterReminderScheduling.SNOOZE_MINUTES * 60_000L
        } else {
            WaterReminderScheduling.computeNextReminderAt(now, config.intervalMinutes, config.activeFrom, config.activeTo)
        }
        WaterNotificationStore.writeReminderConfig(context, config.copy(nextAt = nextAt))
        if (config.enabled) AlarmScheduler.schedule(context, nextAt)
        WaterNotifications.cancelReminderNotification(context)
        WaterNotificationBridge.onReminderNextAtChanged?.invoke(nextAt)
    }
}
