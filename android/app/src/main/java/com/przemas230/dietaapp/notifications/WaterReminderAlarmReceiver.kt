package com.przemas230.dietaapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.przemas230.dietaapp.data.WaterNotificationStore
import com.przemas230.dietaapp.logic.WaterReminderScheduling

/**
 * FR-39: fires when the scheduled reminder alarm goes off, and re-arms the
 * next one -- there's no repeating AlarmManager alarm (see AlarmScheduler),
 * so every fire is responsible for scheduling its own successor, same as
 * web's self-rescheduling `checkWaterReminderSchedule`. Also reschedules on
 * device boot, since AlarmManager alarms don't survive a reboot on their own.
 */
class WaterReminderAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WaterNotificationActions.ACTION_FIRE_REMINDER -> onAlarmFired(context)
            Intent.ACTION_BOOT_COMPLETED -> onBoot(context)
        }
    }

    private fun onAlarmFired(context: Context) {
        val config = WaterNotificationStore.readReminderConfig(context)
        if (!config.enabled) return
        WaterNotifications.showReminderNotification(context)
        val nextAt = WaterReminderScheduling.computeNextReminderAt(
            System.currentTimeMillis(),
            config.intervalMinutes,
            config.activeFrom,
            config.activeTo,
        )
        WaterNotificationStore.writeReminderConfig(context, config.copy(nextAt = nextAt))
        AlarmScheduler.schedule(context, nextAt)
        WaterNotificationBridge.onReminderNextAtChanged?.invoke(nextAt)
    }

    private fun onBoot(context: Context) {
        val config = WaterNotificationStore.readReminderConfig(context)
        if (!config.enabled) return
        val now = System.currentTimeMillis()
        val nextAt = config.nextAt?.takeIf { it > now }
            ?: WaterReminderScheduling.computeNextReminderAt(now, config.intervalMinutes, config.activeFrom, config.activeTo)
        WaterNotificationStore.writeReminderConfig(context, config.copy(nextAt = nextAt))
        AlarmScheduler.schedule(context, nextAt)
    }
}
