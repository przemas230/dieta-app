package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.WeightEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class WeightOperationsTest {

    @Test
    fun `addWeight rejects out-of-range values`() {
        assertNull(WeightOperations.addWeight(emptyList(), "2026-08-10", 29.9))
        assertNull(WeightOperations.addWeight(emptyList(), "2026-08-10", 250.1))
    }

    @Test
    fun `addWeight appends a new day`() {
        val result = WeightOperations.addWeight(listOf(WeightEntry("2026-08-09", 70.0)), "2026-08-10", 69.5)
        assertEquals(listOf(WeightEntry("2026-08-09", 70.0), WeightEntry("2026-08-10", 69.5)), result)
    }

    @Test
    fun `addWeight replaces a same-day entry instead of duplicating`() {
        val result = WeightOperations.addWeight(listOf(WeightEntry("2026-08-10", 70.0)), "2026-08-10", 69.5)
        assertEquals(listOf(WeightEntry("2026-08-10", 69.5)), result)
    }

    @Test
    fun `sortedByDate sorts lexicographically by the YYYY-MM-DD string`() {
        val entries = listOf(WeightEntry("2026-08-10", 1.0), WeightEntry("2026-08-01", 2.0), WeightEntry("2026-07-15", 3.0))
        assertEquals(listOf("2026-07-15", "2026-08-01", "2026-08-10"), WeightOperations.sortedByDate(entries).map { it.dateStr })
    }

    @Test
    fun `kgToGo compares the most recent entry against the target`() {
        val entries = listOf(WeightEntry("2026-08-01", 72.0), WeightEntry("2026-08-10", 69.5))
        assertEquals(7.5, WeightOperations.kgToGo(entries, 62.0))
    }

    @Test
    fun `kgToGo is null with no entries`() {
        assertNull(WeightOperations.kgToGo(emptyList(), 62.0))
    }
}
