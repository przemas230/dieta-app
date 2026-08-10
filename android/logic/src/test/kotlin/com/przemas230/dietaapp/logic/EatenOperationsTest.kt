package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.Snack
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EatenOperationsTest {
    @Test
    fun `isEaten is false for an untouched category`() {
        assertFalse(EatenOperations.isEaten(emptyMap(), "sniadania"))
    }

    @Test
    fun `setEaten true captures the planned recipe's kcal and name`() {
        val entries = EatenOperations.setEaten(emptyMap(), "sniadania", done = true, plannedKcal = 320, plannedName = "Szakszuka")
        assertTrue(EatenOperations.isEaten(entries, "sniadania"))
        assertEquals(320, entries["sniadania"]?.kcal)
        assertEquals("Szakszuka", entries["sniadania"]?.name)
    }

    @Test
    fun `setEaten false keeps the previously captured kcal and name instead of discarding it`() {
        var entries = EatenOperations.setEaten(emptyMap(), "sniadania", done = true, plannedKcal = 320, plannedName = "Szakszuka")
        entries = EatenOperations.setEaten(entries, "sniadania", done = false, plannedKcal = 999, plannedName = "Coś innego")
        assertFalse(EatenOperations.isEaten(entries, "sniadania"))
        assertEquals(320, entries["sniadania"]?.kcal)
        assertEquals("Szakszuka", entries["sniadania"]?.name)
    }

    @Test
    fun `setEaten true with no planned recipe falls back to zero kcal`() {
        val entries = EatenOperations.setEaten(emptyMap(), "obiady", done = true, plannedKcal = null, plannedName = null)
        assertEquals(0, entries["obiady"]?.kcal)
    }

    @Test
    fun `dailyEatenKcal sums only done entries`() {
        val entries = mapOf(
            "sniadania" to EatenEntry(done = true, kcal = 320),
            "drugie" to EatenEntry(done = false, kcal = 250),
            "obiady" to EatenEntry(done = true, kcal = 500),
        )
        assertEquals(820, EatenOperations.dailyEatenKcal(entries))
    }

    @Test
    fun `dailyEatenKcal is zero for no entries`() {
        assertEquals(0, EatenOperations.dailyEatenKcal(emptyMap()))
    }

    @Test
    fun `snacksKcal sums every logged snack`() {
        val snacks = listOf(Snack("1", "banan", 105), Snack("2", "prince polo", 180))
        assertEquals(285, EatenOperations.snacksKcal(snacks))
    }

    @Test
    fun `snacksKcal is zero for no snacks`() {
        assertEquals(0, EatenOperations.snacksKcal(emptyList()))
    }
}
