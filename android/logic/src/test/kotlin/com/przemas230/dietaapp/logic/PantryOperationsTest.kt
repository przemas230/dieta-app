package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.SpiceLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PantryOperationsTest {
    @Test
    fun `addProduct adds a new product entry`() {
        val items = PantryOperations.addProduct(emptyMap(), "Mleko", PantryCategory.NABIAL, 1.0, "l")
        val product = items["Mleko"] as PantryItem.Product
        assertEquals(1.0, product.quantity)
        assertEquals("l", product.unit)
        assertEquals(PantryCategory.NABIAL, product.category)
    }

    @Test
    fun `addProduct ignores blank name or non-positive quantity`() {
        assertTrue(PantryOperations.addProduct(emptyMap(), "", PantryCategory.INNE, 1.0, "szt.").isEmpty())
        assertTrue(PantryOperations.addProduct(emptyMap(), "Cukier", PantryCategory.INNE, 0.0, "kg").isEmpty())
        assertTrue(PantryOperations.addProduct(emptyMap(), "Cukier", PantryCategory.INNE, -1.0, "kg").isEmpty())
    }

    @Test
    fun `addSpice defaults to the given level`() {
        val items = PantryOperations.addSpice(emptyMap(), "Sól", PantryCategory.PRZYPRAWY, SpiceLevel.WYSTARCZY)
        val spice = items["Sól"] as PantryItem.Spice
        assertEquals(SpiceLevel.WYSTARCZY, spice.level)
    }

    @Test
    fun `adjustProductQuantity increases and decreases quantity`() {
        var items = PantryOperations.addProduct(emptyMap(), "Jajka", PantryCategory.NABIAL, 6.0, "szt.")
        items = PantryOperations.adjustProductQuantity(items, "Jajka", 2.0)
        assertEquals(8.0, (items["Jajka"] as PantryItem.Product).quantity)

        items = PantryOperations.adjustProductQuantity(items, "Jajka", -3.0)
        assertEquals(5.0, (items["Jajka"] as PantryItem.Product).quantity)
    }

    @Test
    fun `adjustProductQuantity removes the item once quantity drops to zero or below`() {
        var items = PantryOperations.addProduct(emptyMap(), "Masło", PantryCategory.NABIAL, 1.0, "kostka")
        items = PantryOperations.adjustProductQuantity(items, "Masło", -1.0)
        assertFalse(items.containsKey("Masło"))
    }

    @Test
    fun `adjustProductQuantity on a missing or non-product item is a no-op`() {
        val spiceItems = PantryOperations.addSpice(emptyMap(), "Pieprz", PantryCategory.PRZYPRAWY, SpiceLevel.MALO)
        val unchanged = PantryOperations.adjustProductQuantity(spiceItems, "Pieprz", 1.0)
        assertEquals(spiceItems, unchanged)

        val stillEmpty = PantryOperations.adjustProductQuantity(emptyMap(), "Nieistniejące", 1.0)
        assertTrue(stillEmpty.isEmpty())
    }

    @Test
    fun `cycleSpiceLevel goes Wystarczy to Brak to Malo and back`() {
        var items = PantryOperations.addSpice(emptyMap(), "Bazylia", PantryCategory.PRZYPRAWY, SpiceLevel.WYSTARCZY)
        items = PantryOperations.cycleSpiceLevel(items, "Bazylia")
        assertEquals(SpiceLevel.BRAK, (items["Bazylia"] as PantryItem.Spice).level)

        items = PantryOperations.cycleSpiceLevel(items, "Bazylia")
        assertEquals(SpiceLevel.MALO, (items["Bazylia"] as PantryItem.Spice).level)

        items = PantryOperations.cycleSpiceLevel(items, "Bazylia")
        assertEquals(SpiceLevel.WYSTARCZY, (items["Bazylia"] as PantryItem.Spice).level)
    }

    @Test
    fun `removeItem drops the entry regardless of type`() {
        var items = PantryOperations.addProduct(emptyMap(), "Ser", PantryCategory.NABIAL, 1.0, "kg")
        items = PantryOperations.removeItem(items, "Ser")
        assertTrue(items.isEmpty())
    }

    @Test
    fun `changeCategory moves a product to another category, keeping quantity and unit`() {
        var items = PantryOperations.addProduct(emptyMap(), "Mąka", PantryCategory.ZBOZOWE, 500.0, "g")
        items = PantryOperations.changeCategory(items, "Mąka", PantryCategory.INNE)

        val product = items["Mąka"] as PantryItem.Product
        assertEquals(PantryCategory.INNE, product.category)
        assertEquals(500.0, product.quantity)
        assertEquals("g", product.unit)
    }

    @Test
    fun `changeCategory moves a spice to another category, keeping its level`() {
        var items = PantryOperations.addSpice(emptyMap(), "Sól", PantryCategory.PRZYPRAWY, SpiceLevel.MALO)
        items = PantryOperations.changeCategory(items, "Sól", PantryCategory.INNE)

        val spice = items["Sól"] as PantryItem.Spice
        assertEquals(PantryCategory.INNE, spice.category)
        assertEquals(SpiceLevel.MALO, spice.level)
    }

    @Test
    fun `changeCategory on a missing item is a no-op`() {
        assertTrue(PantryOperations.changeCategory(emptyMap(), "Nieistniejące", PantryCategory.INNE).isEmpty())
    }

    @Test
    fun `categoryForCanon maps known labels and falls back to Inne`() {
        assertEquals(PantryCategory.NABIAL, PantryOperations.categoryForCanon("Nabiał"))
        assertEquals(PantryCategory.PRZYPRAWY, PantryOperations.categoryForCanon("Przyprawy"))
        assertEquals(PantryCategory.INNE, PantryOperations.categoryForCanon("Strączki i orzechy"))
        assertEquals(PantryCategory.INNE, PantryOperations.categoryForCanon("nieznana-kategoria"))
    }

    @Test
    fun `toggleHaveIngredient adds a spice at Wystarczy for Przyprawy`() {
        val items = PantryOperations.toggleHaveIngredient(emptyMap(), "sól", PantryCategory.PRZYPRAWY, "count")
        assertEquals(SpiceLevel.WYSTARCZY, (items["sól"] as PantryItem.Spice).level)
    }

    @Test
    fun `toggleHaveIngredient adds a default-step product for non-spice categories`() {
        val weight = PantryOperations.toggleHaveIngredient(emptyMap(), "mąka", PantryCategory.ZBOZOWE, "weight")
        assertEquals(100.0, (weight["mąka"] as PantryItem.Product).quantity)
        assertEquals("g", (weight["mąka"] as PantryItem.Product).unit)

        val count = PantryOperations.toggleHaveIngredient(emptyMap(), "jajka", PantryCategory.NABIAL, "count")
        assertEquals(1.0, (count["jajka"] as PantryItem.Product).quantity)
        assertEquals("szt.", (count["jajka"] as PantryItem.Product).unit)
    }

    @Test
    fun `toggleHaveIngredient removes an existing entry regardless of type`() {
        val withProduct = PantryOperations.addProduct(emptyMap(), "mąka", PantryCategory.ZBOZOWE, 500.0, "g")
        assertTrue(PantryOperations.toggleHaveIngredient(withProduct, "mąka", PantryCategory.ZBOZOWE, "weight").isEmpty())

        val withSpice = PantryOperations.addSpice(emptyMap(), "sól", PantryCategory.PRZYPRAWY, SpiceLevel.MALO)
        assertTrue(PantryOperations.toggleHaveIngredient(withSpice, "sól", PantryCategory.PRZYPRAWY, "count").isEmpty())
    }
}
