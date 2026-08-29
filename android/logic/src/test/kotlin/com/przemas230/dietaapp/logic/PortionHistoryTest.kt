package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.EatenDay
import com.przemas230.dietaapp.data.EatenEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/** FR-107: reading a habit back out of the portions already recorded. */
class PortionHistoryTest {

    private fun days(vararg entries: Pair<String, Pair<String, Double>>): Map<String, EatenDay> =
        entries.associate { (date, dish) ->
            val (name, portion) = dish
            date to EatenDay(entries = mapOf("obiady" to EatenEntry(true, 500, name, portion)))
        }

    @Test
    fun `one occasion is not a habit`() {
        assertNull(PortionHistory.usualPortion(days("2026-08-01" to ("Zupa" to 0.5)), "Zupa"))
    }

    @Test
    fun `two matching half portions make it the usual`() {
        val history = days("2026-08-01" to ("Zupa" to 0.5), "2026-08-02" to ("Zupa" to 0.5))
        assertEquals(0.5, PortionHistory.usualPortion(history, "Zupa"))
    }

    @Test
    fun `always finishing a dish reports nothing -- that is already the default`() {
        val history = days("2026-08-01" to ("Zupa" to 1.0), "2026-08-02" to ("Zupa" to 1.0))
        assertNull(PortionHistory.usualPortion(history, "Zupa"))
    }

    @Test
    fun `a tie goes to whichever was eaten most recently`() {
        val history = days(
            "2026-08-01" to ("Zupa" to 0.5),
            "2026-08-02" to ("Zupa" to 0.25),
            "2026-08-03" to ("Zupa" to 0.5),
            "2026-08-04" to ("Zupa" to 0.25),
        )
        assertEquals(0.25, PortionHistory.usualPortion(history, "Zupa"))
    }

    @Test
    fun `other dishes do not count towards this one`() {
        val history = days(
            "2026-08-01" to ("Zupa" to 0.5),
            "2026-08-02" to ("Ryż" to 0.5),
        )
        assertNull(PortionHistory.usualPortion(history, "Zupa"))
    }

    @Test
    fun `entries marked not-eaten are ignored`() {
        val history = mapOf(
            "2026-08-01" to EatenDay(entries = mapOf("obiady" to EatenEntry(true, 500, "Zupa", 0.5))),
            "2026-08-02" to EatenDay(entries = mapOf("obiady" to EatenEntry(false, 500, "Zupa", 0.5))),
        )
        assertNull(PortionHistory.usualPortion(history, "Zupa"))
    }

    @Test
    fun `the hint names round fractions in words and falls back to a percentage`() {
        val half = days("2026-08-01" to ("Zupa" to 0.5), "2026-08-02" to ("Zupa" to 0.5))
        assertEquals("Zwykle zjadasz ½ porcji tego dania", PortionHistory.usualPortionHint(half, "Zupa"))

        val odd = days("2026-08-01" to ("Zupa" to 0.35), "2026-08-02" to ("Zupa" to 0.35))
        assertEquals("Zwykle zjadasz 35% tego dania", PortionHistory.usualPortionHint(odd, "Zupa"))

        assertNull(PortionHistory.usualPortionHint(emptyMap(), "Zupa"))
    }

    @Test
    fun `a blank dish name never matches anything`() {
        val history = mapOf(
            "2026-08-01" to EatenDay(entries = mapOf("obiady" to EatenEntry(true, 500, null, 0.5))),
            "2026-08-02" to EatenDay(entries = mapOf("obiady" to EatenEntry(true, 500, null, 0.5))),
        )
        assertNull(PortionHistory.usualPortion(history, ""))
    }
}
