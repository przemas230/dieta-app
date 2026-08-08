package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ShoppingOperationsTest {
    @Test
    fun `addItem adds a new entry`() {
        val items = ShoppingOperations.addItem(emptyMap(), "Chleb", 1.0, "szt.")
        assertEquals(1.0, items["Chleb"]?.quantity)
        assertFalse(items["Chleb"]!!.checked)
    }

    @Test
    fun `addItem ignores blank name or non-positive quantity`() {
        assertTrue(ShoppingOperations.addItem(emptyMap(), "", 1.0, "szt.").isEmpty())
        assertTrue(ShoppingOperations.addItem(emptyMap(), "Woda", 0.0, "l").isEmpty())
    }

    @Test
    fun `toggleChecked flips the checked flag`() {
        var items = ShoppingOperations.addItem(emptyMap(), "Masło", 1.0, "kostka")
        items = ShoppingOperations.toggleChecked(items, "Masło")
        assertTrue(items["Masło"]!!.checked)

        items = ShoppingOperations.toggleChecked(items, "Masło")
        assertFalse(items["Masło"]!!.checked)
    }

    @Test
    fun `toggleChecked on a missing item is a no-op`() {
        val result = ShoppingOperations.toggleChecked(emptyMap(), "Nieistniejące")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `removeItem drops the entry`() {
        var items = ShoppingOperations.addItem(emptyMap(), "Cukier", 1.0, "kg")
        items = ShoppingOperations.removeItem(items, "Cukier")
        assertTrue(items.isEmpty())
    }

    @Test
    fun `clearChecked keeps only unchecked items`() {
        var items = ShoppingOperations.addItem(emptyMap(), "Mleko", 1.0, "l")
        items = ShoppingOperations.addItem(items, "Jajka", 6.0, "szt.")
        items = ShoppingOperations.toggleChecked(items, "Mleko")

        items = ShoppingOperations.clearChecked(items)

        assertEquals(setOf("Jajka"), items.keys)
    }
}
