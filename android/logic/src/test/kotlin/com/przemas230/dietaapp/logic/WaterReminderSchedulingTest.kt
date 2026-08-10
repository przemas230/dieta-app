package com.przemas230.dietaapp.logic

import java.util.Calendar
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class WaterReminderSchedulingTest {
    private fun calAt(hour: Int, minute: Int): Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    @Test
    fun parseHM_parsesHoursAndMinutes() {
        assertEquals(8 * 60, WaterReminderScheduling.parseHM("08:00"))
        assertEquals(22 * 60 + 30, WaterReminderScheduling.parseHM("22:30"))
    }

    @Test
    fun parseHM_fallsBackToZeroOnGarbage() {
        assertEquals(0, WaterReminderScheduling.parseHM("nope"))
    }

    @Test
    fun isActiveMinute_normalWindow() {
        val from = 480 // 08:00
        val to = 1320 // 22:00
        assertTrue(WaterReminderScheduling.isActiveMinute(500, from, to))
        assertFalse(WaterReminderScheduling.isActiveMinute(1350, from, to))
        assertFalse(WaterReminderScheduling.isActiveMinute(100, from, to))
    }

    @Test
    fun isActiveMinute_overnightWindowWraps() {
        val from = 1320 // 22:00
        val to = 480 // 08:00
        assertTrue(WaterReminderScheduling.isActiveMinute(1350, from, to)) // 22:30
        assertTrue(WaterReminderScheduling.isActiveMinute(100, from, to)) // 01:40
        assertFalse(WaterReminderScheduling.isActiveMinute(600, from, to)) // 10:00
    }

    @Test
    fun computeNextReminderAt_staysSameDayWhenWithinWindow() {
        val from = calAt(20, 0)
        val next = WaterReminderScheduling.computeNextReminderAt(from.timeInMillis, 90, "08:00", "22:00")
        val result = Calendar.getInstance().apply { timeInMillis = next }
        assertEquals(21, result.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, result.get(Calendar.MINUTE))
        assertEquals(from.get(Calendar.DAY_OF_YEAR), result.get(Calendar.DAY_OF_YEAR))
    }

    @Test
    fun computeNextReminderAt_pulledToNextDayWhenPastActiveWindow() {
        val from = calAt(21, 30)
        val next = WaterReminderScheduling.computeNextReminderAt(from.timeInMillis, 90, "08:00", "22:00")
        val result = Calendar.getInstance().apply { timeInMillis = next }
        assertEquals(8, result.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, result.get(Calendar.MINUTE))
        assertEquals(from.get(Calendar.DAY_OF_YEAR) + 1, result.get(Calendar.DAY_OF_YEAR))
    }

    @Test
    fun computeNextReminderAt_clampsIntervalBelowMinimum() {
        val from = calAt(10, 0)
        val next = WaterReminderScheduling.computeNextReminderAt(from.timeInMillis, 1, "08:00", "22:00")
        val result = Calendar.getInstance().apply { timeInMillis = next }
        assertEquals(10, result.get(Calendar.HOUR_OF_DAY))
        assertEquals(15, result.get(Calendar.MINUTE))
    }
}
