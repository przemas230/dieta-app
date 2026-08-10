package com.przemas230.dietaapp.logic

/**
 * FR-39: pure port of index.html's `parseHM`/`isActiveMinute`/
 * `computeNextReminderAt` -- the recurring "drink water" reminder's
 * scheduling math, kept free of AlarmManager/Context so it's covered by
 * plain JUnit tests instead of only ever being checkable on a real device.
 */
object WaterReminderScheduling {
    const val DEFAULT_INTERVAL_MINUTES = 90
    const val DEFAULT_ACTIVE_FROM = "08:00"
    const val DEFAULT_ACTIVE_TO = "22:00"
    const val MIN_INTERVAL_MINUTES = 15
    const val SNOOZE_MINUTES = 15

    /** "HH:MM" -> minutes since midnight. Same lenient parse as web's parseHM (bad input -> 0). */
    fun parseHM(str: String?): Int {
        val parts = (str ?: DEFAULT_ACTIVE_FROM).split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        return h * 60 + m
    }

    /** Same wrap-around window check as web's isActiveMinute (handles e.g. 22:00 -> 08:00 overnight windows). */
    fun isActiveMinute(minutesOfDay: Int, fromMin: Int, toMin: Int): Boolean {
        if (fromMin == toMin) return true
        return if (fromMin < toMin) minutesOfDay in fromMin until toMin else minutesOfDay >= fromMin || minutesOfDay < toMin
    }

    /**
     * Next reminder timestamp (epoch millis) after [fromMillis], [intervalMinutes] later,
     * pulled forward to the start of the active window if that lands outside it --
     * one-to-one port of web's computeNextReminderAt.
     */
    fun computeNextReminderAt(
        fromMillis: Long,
        intervalMinutes: Int,
        activeFrom: String,
        activeTo: String,
    ): Long {
        val intervalMs = maxOf(MIN_INTERVAL_MINUTES, intervalMinutes) * 60_000L
        var next = fromMillis + intervalMs
        val fromMin = parseHM(activeFrom)
        val toMin = parseHM(activeTo)
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = next
        val minutesOfDay = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
        if (!isActiveMinute(minutesOfDay, fromMin, toMin)) {
            cal.set(java.util.Calendar.HOUR_OF_DAY, fromMin / 60)
            cal.set(java.util.Calendar.MINUTE, fromMin % 60)
            cal.set(java.util.Calendar.SECOND, 0)
            cal.set(java.util.Calendar.MILLISECOND, 0)
            if (cal.timeInMillis <= next) cal.add(java.util.Calendar.DATE, 1)
            next = cal.timeInMillis
        }
        return next
    }
}
