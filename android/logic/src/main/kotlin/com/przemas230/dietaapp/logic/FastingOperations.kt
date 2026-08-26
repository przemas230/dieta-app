package com.przemas230.dietaapp.logic

/**
 * Requested 2026-08-26 ("dodaj też przynajmniej 5 nowych funkcji" --
 * intermittent fasting / time-restricted eating is one of the most-requested
 * diet-app features per user reviews): pure port of index.html's
 * renderFastingStatus() window check. windowStart/windowEnd are hours of day
 * (0-23) marking the EATING window; windowStart >= windowEnd wraps past
 * midnight (e.g. 20-4), same convention as WaterReminderScheduling.isActiveMinute.
 */
object FastingOperations {
    fun isInEatingWindow(windowStart: Int, windowEnd: Int, minutesOfDay: Int): Boolean =
        WaterReminderScheduling.isActiveMinute(minutesOfDay, windowStart * 60, windowEnd * 60)
}
