package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLogEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneOffset

class ActivityLogOperationsTest {

    @Test
    fun `addEntry prepends newest first`() {
        val entries = ActivityLogOperations.addEntry(emptyList(), "pantry_add", "cebula", 1000L)
        val entries2 = ActivityLogOperations.addEntry(entries, "pantry_delete", "burak", 2000L)
        assertEquals(listOf("pantry_delete", "pantry_add"), entries2.map { it.action })
    }

    @Test
    fun `addEntry caps at MAX_ENTRIES`() {
        var entries = emptyList<ActivityLogEntry>()
        repeat(250) { entries = ActivityLogOperations.addEntry(entries, "pantry_add", "x", it.toLong()) }
        assertEquals(ActivityLogOperations.MAX_ENTRIES, entries.size)
        // newest (highest timestamp, added last) stays at the front
        assertEquals(249L, entries.first().tsEpochMillis)
    }

    private fun dateMillis(dateStr: String): Long =
        Instant.parse("${dateStr}T12:00:00Z").toEpochMilli()

    @Test
    fun `filterByDateRange returns everything when both bounds are empty`() {
        val entries = listOf(ActivityLogEntry(dateMillis("2026-08-05"), "pantry_add", "x"))
        assertEquals(entries, ActivityLogOperations.filterByDateRange(entries, null, null))
        assertEquals(entries, ActivityLogOperations.filterByDateRange(entries, "", ""))
    }

    @Test
    fun `filterByDateRange applies inclusive from and to bounds`() {
        val entries = listOf(
            ActivityLogEntry(dateMillis("2026-08-01"), "a", "1"),
            ActivityLogEntry(dateMillis("2026-08-05"), "b", "2"),
            ActivityLogEntry(dateMillis("2026-08-10"), "c", "3"),
        )
        val result = ActivityLogOperations.filterByDateRange(entries, "2026-08-02", "2026-08-09")
        assertEquals(listOf("b"), result.map { it.action })
    }

    @Test
    fun `filterByDateRange boundary dates are inclusive`() {
        val entries = listOf(ActivityLogEntry(dateMillis("2026-08-05"), "a", "1"))
        assertTrue(ActivityLogOperations.filterByDateRange(entries, "2026-08-05", "2026-08-05").isNotEmpty())
    }
}
