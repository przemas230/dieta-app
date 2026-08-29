package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.CookEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CookHistoryOperationsTest {
    @Test
    fun `addToday appends a new unrated entry for the recipe`() {
        var entries = CookHistoryOperations.addToday(emptyMap(), "r1", 1000L)
        assertEquals(listOf(CookEntry(1000L)), entries["r1"])

        entries = CookHistoryOperations.addToday(entries, "r1", 2000L)
        assertEquals(listOf(CookEntry(1000L), CookEntry(2000L)), entries["r1"])
    }

    @Test
    fun `setRating sets a rating and tapping the same star again clears it`() {
        var entries = CookHistoryOperations.addToday(emptyMap(), "r1", 1000L)
        entries = CookHistoryOperations.setRating(entries, "r1", 0, 4)
        assertEquals(4, entries["r1"]!![0].rating)

        entries = CookHistoryOperations.setRating(entries, "r1", 0, 4)
        assertNull(entries["r1"]!![0].rating)
    }

    @Test
    fun `setRating on an out-of-range index or unknown recipe is a no-op`() {
        val entries = CookHistoryOperations.addToday(emptyMap(), "r1", 1000L)
        assertEquals(entries, CookHistoryOperations.setRating(entries, "r1", 5, 3))
        assertEquals(entries, CookHistoryOperations.setRating(entries, "unknown", 0, 3))
    }

    @Test
    fun `removeEntry drops the entry and removes the recipe key once the list is empty`() {
        var entries = CookHistoryOperations.addToday(emptyMap(), "r1", 1000L)
        entries = CookHistoryOperations.addToday(entries, "r1", 2000L)

        entries = CookHistoryOperations.removeEntry(entries, "r1", 0)
        assertEquals(listOf(CookEntry(2000L)), entries["r1"])

        entries = CookHistoryOperations.removeEntry(entries, "r1", 0)
        assertTrue(entries.isEmpty())
    }

    @Test
    fun `removeEntry on an out-of-range index or unknown recipe is a no-op`() {
        val entries = CookHistoryOperations.addToday(emptyMap(), "r1", 1000L)
        assertEquals(entries, CookHistoryOperations.removeEntry(entries, "r1", 5))
        assertEquals(entries, CookHistoryOperations.removeEntry(entries, "unknown", 0))
    }

    // ---- FR-103: "was this cooked today" for the Planer swipe ----

    @Test
    fun `cookedTodayIndex finds only entries from the same UTC day`() {
        val now = 1_756_000_000_000L
        val yesterday = now - 86_400_000L
        val entries = mapOf("r1" to listOf(CookEntry(yesterday), CookEntry(now)))
        assertEquals(1, CookHistoryOperations.cookedTodayIndex(entries, "r1", now))
        assertEquals(0, CookHistoryOperations.cookedTodayIndex(entries, "r1", yesterday))
    }

    @Test
    fun `cookedTodayIndex returns -1 for an unknown recipe or a stale-only history`() {
        val now = 1_756_000_000_000L
        val entries = mapOf("r1" to listOf(CookEntry(now - 5 * 86_400_000L)))
        assertEquals(-1, CookHistoryOperations.cookedTodayIndex(entries, "r1", now))
        assertEquals(-1, CookHistoryOperations.cookedTodayIndex(entries, "nieznany", now))
    }

    @Test
    fun `cookedTodayIndex points at the LAST of several entries on the same day`() {
        val now = 1_756_000_000_000L
        val entries = mapOf("r1" to listOf(CookEntry(now - 3_600_000L), CookEntry(now - 60_000L)))
        assertEquals(1, CookHistoryOperations.cookedTodayIndex(entries, "r1", now))
    }
}
