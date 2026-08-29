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

    // ---- FR-103/FR-104: "was this cooked on that day" for the Planer swipe ----

    @Test
    fun `cookedOnDateIndex finds only entries from the requested LOCAL day`() {
        val today = AppDates.today()
        val yesterday = today.minusDays(1)
        val entries = mapOf(
            "r1" to listOf(
                CookEntry(AppDates.noonEpochMillis(yesterday)),
                CookEntry(AppDates.noonEpochMillis(today)),
            ),
        )
        assertEquals(1, CookHistoryOperations.cookedOnDateIndex(entries, "r1", today.toString()))
        assertEquals(0, CookHistoryOperations.cookedOnDateIndex(entries, "r1", yesterday.toString()))
    }

    @Test
    fun `cookedOnDateIndex returns -1 for an unknown recipe or a day with nothing logged`() {
        val today = AppDates.today()
        val entries = mapOf("r1" to listOf(CookEntry(AppDates.noonEpochMillis(today.minusDays(5)))))
        assertEquals(-1, CookHistoryOperations.cookedOnDateIndex(entries, "r1", today.toString()))
        assertEquals(-1, CookHistoryOperations.cookedOnDateIndex(entries, "nieznany", today.toString()))
    }

    @Test
    fun `cookedOnDateIndex points at the LAST of several entries on the same day`() {
        val today = AppDates.today()
        val noon = AppDates.noonEpochMillis(today)
        val entries = mapOf("r1" to listOf(CookEntry(noon - 3_600_000L), CookEntry(noon + 3_600_000L)))
        assertEquals(1, CookHistoryOperations.cookedOnDateIndex(entries, "r1", today.toString()))
    }

    @Test
    fun `FR-104 addOnDate logs against a day that is not today, and is found there`() {
        val target = AppDates.today().minusDays(3)
        val entries = CookHistoryOperations.addOnDate(emptyMap(), "r1", target)
        assertEquals(0, CookHistoryOperations.cookedOnDateIndex(entries, "r1", target.toString()))
        assertEquals(-1, CookHistoryOperations.cookedOnDateIndex(entries, "r1", AppDates.today().toString()))
    }

    @Test
    fun `FR-101 a cook entry logged at local noon reads back as that same local day`() {
        // The regression this guards: comparing the raw epoch (a UTC day)
        // instead of the local calendar day put entries logged in the first
        // hour or two of a Polish day under the previous one.
        val day = AppDates.today()
        assertEquals(day.toString(), AppDates.dateKey(AppDates.noonEpochMillis(day)))
    }
}
