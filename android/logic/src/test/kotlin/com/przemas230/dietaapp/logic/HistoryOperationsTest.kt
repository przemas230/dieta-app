package com.przemas230.dietaapp.logic

import java.time.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HistoryOperationsTest {

    private val today = LocalDate.of(2026, 8, 10)

    @Test
    fun `calcKcalStreak counts consecutive in-range days back from today`() {
        val history = mapOf(
            "2026-08-10" to 1500, // today, in range -- counts
            "2026-08-09" to 1480,
            "2026-08-08" to 1450,
            "2026-08-07" to 500, // breaks streak
        )
        assertEquals(3, HistoryOperations.calcKcalStreak(history, dailyTarget = 1480, today = today))
    }

    @Test
    fun `calcKcalStreak does not break the streak when today has no data yet`() {
        val history = mapOf("2026-08-09" to 1480, "2026-08-08" to 1490)
        assertEquals(2, HistoryOperations.calcKcalStreak(history, dailyTarget = 1480, today = today))
    }

    @Test
    fun `calcKcalStreak is zero when yesterday already breaks it`() {
        val history = mapOf("2026-08-09" to 300)
        assertEquals(0, HistoryOperations.calcKcalStreak(history, dailyTarget = 1480, today = today))
    }

    @Test
    fun `calcKcalStreak rejects overshoot beyond 115 percent`() {
        val history = mapOf("2026-08-09" to 2000)
        assertEquals(0, HistoryOperations.calcKcalStreak(history, dailyTarget = 1480, today = today))
    }

    @Test
    fun `calcWaterStreak counts consecutive days with at least 8 glasses`() {
        val history = mapOf("2026-08-10" to 8, "2026-08-09" to 9, "2026-08-08" to 5)
        assertEquals(2, HistoryOperations.calcWaterStreak(history, today = today))
    }

    @Test
    fun `calcWaterStreak today missing does not break it`() {
        val history = mapOf("2026-08-09" to 8)
        assertEquals(1, HistoryOperations.calcWaterStreak(history, today = today))
    }

    @Test
    fun `weeklyBalance sums the last 7 days including today`() {
        val history = (0..6).associate { today.minusDays(it.toLong()).toString() to 200 }
        val balance = HistoryOperations.weeklyBalance(history, dailyTarget = 1480, today = today)
        assertEquals(1400, balance.totalKcal)
        assertEquals(10360, balance.targetKcal)
        assertEquals(1400 - 10360, balance.diff)
    }

    @Test
    fun `lastNDays returns oldest-to-newest with missing days defaulted to zero`() {
        val history = mapOf("2026-08-10" to 500)
        val result = HistoryOperations.lastNDays(history, today, 3)
        assertEquals(listOf("2026-08-08" to 0, "2026-08-09" to 0, "2026-08-10" to 500), result)
    }
}
