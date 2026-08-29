package com.przemas230.dietaapp.data

import android.content.Context
import com.przemas230.dietaapp.logic.WaterReminderScheduling
import java.time.LocalDate
import java.time.ZoneOffset
import org.json.JSONArray
import org.json.JSONObject
import com.przemas230.dietaapp.logic.AppDates

/**
 * FR-38/39: separate, small SharedPreferences store for the water
 * notification/reminder feature -- deliberately NOT part of
 * [LocalStateStore]'s single JSON document, because [WaterActionReceiver]
 * and [WaterReminderAlarmReceiver] need to read/write this from a
 * BroadcastReceiver (no ViewModel, no guarantee the app process is even
 * showing UI) without racing LocalPersistenceCoordinator's own debounced
 * read-modify-write cycle on the same file. Mirrors index.html's own
 * split -- the water notification buttons use a dedicated "water-kv"
 * IndexedDB store, separate from the main `state` blob in localStorage.
 */
object WaterNotificationStore {
    private const val PREFS_NAME = "water_notifications"
    private const val KEY_NOTIF_ENABLED = "notif_enabled"
    private const val KEY_REMINDER_ENABLED = "reminder_enabled"
    private const val KEY_INTERVAL_MINUTES = "reminder_interval_minutes"
    private const val KEY_ACTIVE_FROM = "reminder_active_from"
    private const val KEY_ACTIVE_TO = "reminder_active_to"
    private const val KEY_NEXT_AT = "reminder_next_at"
    private const val KEY_PENDING_DATE = "pending_water_date"
    private const val KEY_PENDING_COUNT = "pending_water_count"
    private const val KEY_LAST_ACTION_AT = "last_action_at"
    private const val KEY_ACTION_LOG = "action_log"
    private const val MAX_LOG_ENTRIES = 20

    data class ReminderConfig(
        val enabled: Boolean,
        val intervalMinutes: Int,
        val activeFrom: String,
        val activeTo: String,
        val nextAt: Long?,
    )

    data class LogEntry(
        val timestamp: Long,
        val action: String,
        val result: String,
        val countBefore: Int?,
        val countAfter: Int?,
    )

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isNotifEnabled(context: Context): Boolean = prefs(context).getBoolean(KEY_NOTIF_ENABLED, false)

    fun setNotifEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_NOTIF_ENABLED, enabled).apply()
    }

    fun readReminderConfig(context: Context): ReminderConfig {
        val p = prefs(context)
        val nextAt = p.getLong(KEY_NEXT_AT, -1L).let { if (it <= 0) null else it }
        return ReminderConfig(
            enabled = p.getBoolean(KEY_REMINDER_ENABLED, false),
            intervalMinutes = p.getInt(KEY_INTERVAL_MINUTES, WaterReminderScheduling.DEFAULT_INTERVAL_MINUTES),
            activeFrom = p.getString(KEY_ACTIVE_FROM, WaterReminderScheduling.DEFAULT_ACTIVE_FROM)
                ?: WaterReminderScheduling.DEFAULT_ACTIVE_FROM,
            activeTo = p.getString(KEY_ACTIVE_TO, WaterReminderScheduling.DEFAULT_ACTIVE_TO)
                ?: WaterReminderScheduling.DEFAULT_ACTIVE_TO,
            nextAt = nextAt,
        )
    }

    fun writeReminderConfig(context: Context, config: ReminderConfig) {
        prefs(context).edit()
            .putBoolean(KEY_REMINDER_ENABLED, config.enabled)
            .putInt(KEY_INTERVAL_MINUTES, config.intervalMinutes)
            .putString(KEY_ACTIVE_FROM, config.activeFrom)
            .putString(KEY_ACTIVE_TO, config.activeTo)
            .putLong(KEY_NEXT_AT, config.nextAt ?: -1L)
            .apply()
    }

    /** Today's water count as last known by the notification side -- 0 if the stored date isn't today. */
    fun readPendingWaterCount(context: Context): Int {
        val p = prefs(context)
        val today = AppDates.todayKey()
        val storedDate = p.getString(KEY_PENDING_DATE, null)
        return if (storedDate == today) p.getInt(KEY_PENDING_COUNT, 0) else 0
    }

    fun writePendingWaterCount(context: Context, count: Int) {
        val today = AppDates.todayKey()
        prefs(context).edit()
            .putString(KEY_PENDING_DATE, today)
            .putInt(KEY_PENDING_COUNT, count)
            .apply()
    }

    /** Dedup guard against a single physical tap firing two notification-action broadcasts (real Android/Chrome-family quirk, see WaterActionReceiver). */
    fun claimActionSlot(context: Context, now: Long, minGapMs: Long): Boolean {
        val p = prefs(context)
        val last = p.getLong(KEY_LAST_ACTION_AT, 0L)
        if (now - last < minGapMs) return false
        p.edit().putLong(KEY_LAST_ACTION_AT, now).apply()
        return true
    }

    fun appendLog(context: Context, entry: LogEntry) {
        val p = prefs(context)
        val arr = try {
            JSONArray(p.getString(KEY_ACTION_LOG, "[]"))
        } catch (e: Exception) {
            JSONArray()
        }
        val obj = JSONObject().apply {
            put("t", entry.timestamp)
            put("action", entry.action)
            put("result", entry.result)
            entry.countBefore?.let { put("countBefore", it) }
            entry.countAfter?.let { put("countAfter", it) }
        }
        arr.put(obj)
        val trimmed = JSONArray()
        val start = maxOf(0, arr.length() - MAX_LOG_ENTRIES)
        for (i in start until arr.length()) trimmed.put(arr.get(i))
        p.edit().putString(KEY_ACTION_LOG, trimmed.toString()).apply()
    }

    fun readLog(context: Context): List<LogEntry> {
        val raw = prefs(context).getString(KEY_ACTION_LOG, "[]") ?: "[]"
        val arr = try {
            JSONArray(raw)
        } catch (e: Exception) {
            return emptyList()
        }
        return (0 until arr.length()).mapNotNull { i ->
            val obj = arr.optJSONObject(i) ?: return@mapNotNull null
            LogEntry(
                timestamp = obj.optLong("t"),
                action = obj.optString("action"),
                result = obj.optString("result"),
                countBefore = if (obj.has("countBefore")) obj.optInt("countBefore") else null,
                countAfter = if (obj.has("countAfter")) obj.optInt("countAfter") else null,
            )
        }
    }
}
