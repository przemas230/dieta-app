package com.przemas230.dietaapp.logic

import java.time.LocalDate

/**
 * FR-41/FR-42: pure port of index.html's calcKcalStreak/calcWaterStreak and
 * the "Historia kalorii" chart/weekly-balance math. Both take a plain
 * date-string-keyed history map (see EatenViewModel.kcalHistory/
 * WaterViewModel.history) rather than reaching into a ViewModel, so this is
 * fully unit-testable with an injected `today`.
 */
object HistoryOperations {
    /**
     * Consecutive days (counting back from today) where eaten kcal lands
     * within 15% under / 15% over the daily target -- today itself never
     * BREAKS the streak just for being in progress (not yet enough logged),
     * it's simply skipped until it also qualifies, same as index.html.
     */
    fun calcKcalStreak(kcalHistory: Map<String, Int>, dailyTarget: Int, today: LocalDate, daysBack: Int = 365): Int {
        var streak = 0
        for (i in 0 until daysBack) {
            val ds = today.minusDays(i.toLong()).toString()
            val kcal = kcalHistory[ds] ?: 0
            val inRange = dailyTarget > 0 && kcal >= dailyTarget * 0.7 && kcal <= dailyTarget * 1.15
            if (inRange) {
                streak++
                continue
            }
            if (i == 0) continue
            break
        }
        return streak
    }

    /** Consecutive days with a recorded water count >= 8 glasses, same "today doesn't break it" rule as calcKcalStreak. */
    fun calcWaterStreak(waterHistory: Map<String, Int>, today: LocalDate, daysBack: Int = 365): Int {
        var streak = 0
        for (i in 0 until daysBack) {
            val ds = today.minusDays(i.toLong()).toString()
            val count = waterHistory[ds]
            val metGoal = count != null && count >= 8
            if (metGoal) {
                streak++
                continue
            }
            if (i == 0) continue
            break
        }
        return streak
    }

    data class WeeklyBalance(val totalKcal: Int, val targetKcal: Int) {
        val diff: Int get() = totalKcal - targetKcal
    }

    /** Sum of the last 7 days' (including today) recorded kcal vs. 7x the daily target. */
    fun weeklyBalance(kcalHistory: Map<String, Int>, dailyTarget: Int, today: LocalDate): WeeklyBalance {
        val total = (0..6).sumOf { kcalHistory[today.minusDays(it.toLong()).toString()] ?: 0 }
        return WeeklyBalance(total, dailyTarget * 7)
    }

    /** Oldest-to-newest (date, kcal) pairs for the last [days] days, including today -- chart-ready, missing days default to 0. */
    fun lastNDays(kcalHistory: Map<String, Int>, today: LocalDate, days: Int): List<Pair<String, Int>> =
        (days - 1 downTo 0).map {
            val ds = today.minusDays(it.toLong()).toString()
            ds to (kcalHistory[ds] ?: 0)
        }
}
