package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.przemas230.dietaapp.data.WaterNotificationStore
import com.przemas230.dietaapp.logic.WaterReminderScheduling
import com.przemas230.dietaapp.notifications.AlarmScheduler
import com.przemas230.dietaapp.notifications.WaterNotifications
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReminderUiState(
    val enabled: Boolean = false,
    val intervalMinutes: Int = WaterReminderScheduling.DEFAULT_INTERVAL_MINUTES,
    val activeFrom: String = WaterReminderScheduling.DEFAULT_ACTIVE_FROM,
    val activeTo: String = WaterReminderScheduling.DEFAULT_ACTIVE_TO,
    val nextAt: Long? = null,
)

/**
 * FR-38/39: UI-facing state for the "Przypomnienia" settings tab -- a thin
 * layer over [WaterNotificationStore] (the actual source of truth, also read
 * by the BroadcastReceivers that don't have a ViewModel). Notification
 * permission itself (API 33+) is requested from the Composable via
 * `rememberLauncherForActivityResult`, same pattern as Google sign-in in
 * SettingsScreen.kt -- this ViewModel assumes permission is already granted
 * by the time its enable functions are called.
 */
class WaterNotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val _trackerEnabled = MutableStateFlow(WaterNotificationStore.isNotifEnabled(application))
    val trackerEnabled: StateFlow<Boolean> = _trackerEnabled.asStateFlow()

    private val _reminder = MutableStateFlow(loadReminderUiState())
    val reminder: StateFlow<ReminderUiState> = _reminder.asStateFlow()

    private val _actionLog = MutableStateFlow(WaterNotificationStore.readLog(application))
    val actionLog: StateFlow<List<WaterNotificationStore.LogEntry>> = _actionLog.asStateFlow()

    private fun loadReminderUiState(): ReminderUiState {
        val config = WaterNotificationStore.readReminderConfig(getApplication())
        return ReminderUiState(config.enabled, config.intervalMinutes, config.activeFrom, config.activeTo, config.nextAt)
    }

    fun refreshActionLog() {
        _actionLog.value = WaterNotificationStore.readLog(getApplication())
    }

    /** Called after WaterNotificationBridge reports a change made from the notification while this ViewModel is alive. */
    fun onExternalNextAtChanged(nextAt: Long?) {
        _reminder.value = _reminder.value.copy(nextAt = nextAt)
        refreshActionLog()
    }

    fun setTrackerEnabled(enabled: Boolean, currentWaterCount: Int) {
        val context = getApplication<Application>()
        WaterNotificationStore.setNotifEnabled(context, enabled)
        _trackerEnabled.value = enabled
        if (enabled) {
            WaterNotificationStore.writePendingWaterCount(context, currentWaterCount)
            WaterNotifications.showTrackerNotification(context, currentWaterCount)
        } else {
            WaterNotifications.cancelTrackerNotification(context)
        }
    }

    /** Keeps the persistent tracker notification's count in sync whenever water is logged elsewhere in the app (header droplets, Postęp tab). */
    fun syncTrackerNotification(waterCount: Int) {
        if (!_trackerEnabled.value) return
        val context = getApplication<Application>()
        WaterNotificationStore.writePendingWaterCount(context, waterCount)
        WaterNotifications.showTrackerNotification(context, waterCount)
    }

    fun setReminderEnabled(enabled: Boolean) {
        val context = getApplication<Application>()
        val current = _reminder.value
        if (enabled) {
            val nextAt = WaterReminderScheduling.computeNextReminderAt(
                System.currentTimeMillis(), current.intervalMinutes, current.activeFrom, current.activeTo,
            )
            WaterNotificationStore.writeReminderConfig(
                context,
                WaterNotificationStore.ReminderConfig(true, current.intervalMinutes, current.activeFrom, current.activeTo, nextAt),
            )
            AlarmScheduler.schedule(context, nextAt)
            _reminder.value = current.copy(enabled = true, nextAt = nextAt)
        } else {
            WaterNotificationStore.writeReminderConfig(
                context,
                WaterNotificationStore.ReminderConfig(false, current.intervalMinutes, current.activeFrom, current.activeTo, null),
            )
            AlarmScheduler.cancel(context)
            WaterNotifications.cancelReminderNotification(context)
            _reminder.value = current.copy(enabled = false, nextAt = null)
        }
    }

    fun updateReminderConfig(intervalMinutes: Int, activeFrom: String, activeTo: String) {
        val context = getApplication<Application>()
        val current = _reminder.value
        val clampedInterval = maxOf(WaterReminderScheduling.MIN_INTERVAL_MINUTES, intervalMinutes)
        val nextAt = if (current.enabled) {
            WaterReminderScheduling.computeNextReminderAt(System.currentTimeMillis(), clampedInterval, activeFrom, activeTo)
        } else {
            null
        }
        WaterNotificationStore.writeReminderConfig(
            context,
            WaterNotificationStore.ReminderConfig(current.enabled, clampedInterval, activeFrom, activeTo, nextAt),
        )
        if (current.enabled && nextAt != null) AlarmScheduler.schedule(context, nextAt)
        _reminder.value = current.copy(intervalMinutes = clampedInterval, activeFrom = activeFrom, activeTo = activeTo, nextAt = nextAt)
    }
}
