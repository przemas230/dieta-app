package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.WeightEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AdaptiveTdeeTest {
    @Test
    fun `returns null with fewer than two weight entries`() {
        val today = LocalDate.of(2026, 8, 30)
        assertNull(AdaptiveTdee.compute(listOf(WeightEntry("2026-08-30", 80.0)), emptyMap(), today))
        assertNull(AdaptiveTdee.compute(emptyList(), emptyMap(), today))
    }

    @Test
    fun `returns null when the weight span is too short`() {
        val today = LocalDate.of(2026, 8, 30)
        val weights = listOf(WeightEntry("2026-08-25", 80.0), WeightEntry("2026-08-30", 79.5))
        assertNull(AdaptiveTdee.compute(weights, emptyMap(), today))
    }

    @Test
    fun `returns null when too few days were actually logged`() {
        val today = LocalDate.of(2026, 8, 30)
        val start = today.minusDays(13)
        val weights = listOf(WeightEntry(start.toString(), 80.0), WeightEntry(today.toString(), 79.0))
        // Only 3 logged days -- below MIN_LOGGED_DAYS (6).
        val kcalHistory = (0..2).associate { i -> start.plusDays(i.toLong()).toString() to 2000 }
        assertNull(AdaptiveTdee.compute(weights, kcalHistory, today))
    }

    @Test
    fun `estimates a real TDEE above the logged average when weight is trending down`() {
        val today = LocalDate.of(2026, 8, 30)
        val start = today.minusDays(13) // 14-day span
        val weights = (0..13).map { i ->
            val kg = 80.0 - i * (1.0 / 13.0)
            WeightEntry(start.plusDays(i.toLong()).toString(), Math.round(kg * 10) / 10.0)
        }
        val kcalHistory = (0..13).associate { i -> start.plusDays(i.toLong()).toString() to 2000 }
        val result = AdaptiveTdee.compute(weights, kcalHistory, today)
        requireNotNull(result)
        assertEquals(14, result.loggedDays)
        assertEquals(13, result.spanDays)
        assertEquals(2000, result.avgKcal)
        // Losing weight on 2000 kcal/day means the real TDEE is HIGHER than 2000.
        assert(result.estimatedTdee > 2000) { "expected estimatedTdee > 2000, got ${result.estimatedTdee}" }
        assert(result.weeklyKgChange < 0) { "expected a negative weekly change, got ${result.weeklyKgChange}" }
    }

    @Test
    fun `a day missing from kcalHistory is excluded from the average rather than counted as zero`() {
        val today = LocalDate.of(2026, 8, 30)
        val start = today.minusDays(13)
        val weights = listOf(WeightEntry(start.toString(), 80.0), WeightEntry(today.toString(), 80.0))
        // 6 logged days out of 14 (meets MIN_LOGGED_DAYS exactly), each 2500 kcal;
        // the other 8 days have no entry at all in kcalHistory.
        val kcalHistory = (0..5).associate { i -> start.plusDays(i.toLong()).toString() to 2500 }
        val result = AdaptiveTdee.compute(weights, kcalHistory, today)
        requireNotNull(result)
        assertEquals(6, result.loggedDays)
        assertEquals(2500, result.avgKcal) // not diluted by the 8 missing days reading as 0
    }
}
