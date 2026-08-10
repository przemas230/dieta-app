package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PlannedMeal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShoppingDayStripTest {
    @Test
    fun `todayIndex maps JS-style Sunday-first day-of-week onto the Monday-first WeekPlan index`() {
        assertEquals(0, ShoppingDayStrip.todayIndex(1)) // Monday
        assertEquals(5, ShoppingDayStrip.todayIndex(6)) // Saturday
        assertEquals(6, ShoppingDayStrip.todayIndex(0)) // Sunday
    }

    @Test
    fun `an empty day is EMPTY regardless of todayIdx`() {
        val cards = ShoppingDayStrip.buildCards(emptyMap(), { false }, todayIdx = 0)
        assertEquals(DayCardState.EMPTY, cards[3].state)
        assertEquals(0, cards[3].progressPct)
    }

    @Test
    fun `a planned day with nothing on the list yet is TODO`() {
        val plan = mapOf(2 to mapOf("obiady" to PlannedMeal("r1")))
        val cards = ShoppingDayStrip.buildCards(plan, { false }, todayIdx = 0)
        assertEquals(DayCardState.TODO, cards[2].state)
        assertEquals(1, cards[2].planned)
        assertEquals(0, cards[2].onList)
    }

    @Test
    fun `a partially-added day is STARTED with a proportional percentage`() {
        val plan = mapOf(
            2 to mapOf(
                "sniadania" to PlannedMeal("r1"),
                "obiady" to PlannedMeal("r2"),
            ),
        )
        val cards = ShoppingDayStrip.buildCards(plan, { it == "r1" }, todayIdx = 0)
        assertEquals(DayCardState.STARTED, cards[2].state)
        assertEquals(50, cards[2].progressPct)
    }

    @Test
    fun `a fully-added day is DONE at 100 percent`() {
        val plan = mapOf(2 to mapOf("obiady" to PlannedMeal("r1")))
        val cards = ShoppingDayStrip.buildCards(plan, { it == "r1" }, todayIdx = 0)
        assertEquals(DayCardState.DONE, cards[2].state)
        assertEquals(100, cards[2].progressPct)
    }

    @Test
    fun `labels mark today, tomorrow and the day after relative to todayIdx, wrapping across the week boundary`() {
        val cards = ShoppingDayStrip.buildCards(emptyMap(), { false }, todayIdx = 6) // Niedziela is "today"
        assertEquals("Dziś", cards[6].label)
        assertEquals("Jutro", cards[0].label)
        assertEquals("Pojutrze", cards[1].label)
        assertEquals("Śro", cards[2].label)
    }

    @Test
    fun `clickDayLabel passes special labels through but uses the full day name otherwise`() {
        val cards = ShoppingDayStrip.buildCards(emptyMap(), { false }, todayIdx = 0)
        assertEquals("Dziś", ShoppingDayStrip.clickDayLabel(cards[0]))
        assertEquals("Jutro", ShoppingDayStrip.clickDayLabel(cards[1]))
        assertEquals("Pojutrze", ShoppingDayStrip.clickDayLabel(cards[2]))
        assertEquals("Czwartek", ShoppingDayStrip.clickDayLabel(cards[3]))
    }

    @Test
    fun `addResultMessage covers all four outcomes index_html's toast distinguishes`() {
        assertEquals("Brak zaplanowanych dań na dziś w Planerze", ShoppingDayStrip.addResultMessage("Dziś", added = 0, already = 0))
        assertEquals("Dania na dziś są już na liście", ShoppingDayStrip.addResultMessage("Dziś", added = 0, already = 2))
        assertEquals("Dodano składniki z 2 dań na dziś", ShoppingDayStrip.addResultMessage("Dziś", added = 2, already = 0))
        assertEquals(
            "Dodano składniki z 1 dań na wtorek (2 już było na liście)",
            ShoppingDayStrip.addResultMessage("wtorek", added = 1, already = 2),
        )
    }
}
