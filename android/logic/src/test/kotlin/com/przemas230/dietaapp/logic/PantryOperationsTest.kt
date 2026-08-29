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
    fun `tileTapDelta upper half on an untracked product creates it at tileStep`() {
        val items = PantryOperations.tileTapDelta(emptyMap(), "Mąka", PantryCategory.ZBOZOWE, "weight", dir = 1)
        val product = items["Mąka"] as PantryItem.Product
        assertEquals(100.0, product.quantity)
        assertEquals("g", product.unit)
    }

    @Test
    fun `tileTapDelta lower half on an untracked product is a no-op`() {
        assertTrue(PantryOperations.tileTapDelta(emptyMap(), "Mąka", PantryCategory.ZBOZOWE, "weight", dir = -1).isEmpty())
    }

    @Test
    fun `tileTapDelta increases and decreases a tracked product by its step`() {
        var items = PantryOperations.tileTapDelta(emptyMap(), "Jajka", PantryCategory.NABIAL, "count", dir = 1)
        items = PantryOperations.tileTapDelta(items, "Jajka", PantryCategory.NABIAL, "count", dir = 1)
        assertEquals(2.0, (items["Jajka"] as PantryItem.Product).quantity)

        items = PantryOperations.tileTapDelta(items, "Jajka", PantryCategory.NABIAL, "count", dir = -1)
        assertEquals(1.0, (items["Jajka"] as PantryItem.Product).quantity)
    }

    @Test
    fun `tileTapDelta clamps a tracked product at zero without removing it`() {
        var items = PantryOperations.tileTapDelta(emptyMap(), "Masło", PantryCategory.NABIAL, "count", dir = 1)
        items = PantryOperations.tileTapDelta(items, "Masło", PantryCategory.NABIAL, "count", dir = -1)
        assertEquals(0.0, (items["Masło"] as PantryItem.Product).quantity)

        items = PantryOperations.tileTapDelta(items, "Masło", PantryCategory.NABIAL, "count", dir = -1)
        assertEquals(0.0, (items["Masło"] as PantryItem.Product).quantity)
        assertTrue(items.containsKey("Masło"))
    }

    @Test
    fun `tileTapDelta upper half on an untracked spice creates it at Malo`() {
        val items = PantryOperations.tileTapDelta(emptyMap(), "Sól", PantryCategory.PRZYPRAWY, "count", dir = 1)
        assertEquals(SpiceLevel.MALO, (items["Sól"] as PantryItem.Spice).level)
    }

    @Test
    fun `tileTapDelta lower half on an untracked spice is a no-op`() {
        assertTrue(PantryOperations.tileTapDelta(emptyMap(), "Sól", PantryCategory.PRZYPRAWY, "count", dir = -1).isEmpty())
    }

    @Test
    fun `tileTapDelta clamps a spice between Malo and Duzo without wrapping or removing it`() {
        var items = PantryOperations.tileTapDelta(emptyMap(), "Bazylia", PantryCategory.PRZYPRAWY, "count", dir = 1)
        assertEquals(SpiceLevel.MALO, (items["Bazylia"] as PantryItem.Spice).level)

        items = PantryOperations.tileTapDelta(items, "Bazylia", PantryCategory.PRZYPRAWY, "count", dir = -1)
        assertEquals(SpiceLevel.MALO, (items["Bazylia"] as PantryItem.Spice).level)
        assertTrue(items.containsKey("Bazylia"))

        items = PantryOperations.tileTapDelta(items, "Bazylia", PantryCategory.PRZYPRAWY, "count", dir = 1)
        items = PantryOperations.tileTapDelta(items, "Bazylia", PantryCategory.PRZYPRAWY, "count", dir = 1)
        items = PantryOperations.tileTapDelta(items, "Bazylia", PantryCategory.PRZYPRAWY, "count", dir = 1)
        assertEquals(SpiceLevel.DUZO, (items["Bazylia"] as PantryItem.Spice).level)
    }

    @Test
    fun `removeItem drops the entry regardless of type`() {
        var items: Map<String, PantryItem> = mapOf("Ser" to PantryItem.Product("Ser", PantryCategory.NABIAL, 1.0, "kg"))
        items = PantryOperations.removeItem(items, "Ser")
        assertTrue(items.isEmpty())
    }

    @Test
    fun `changeCategory moves a product to another category, keeping quantity and unit`() {
        var items: Map<String, PantryItem> = mapOf("Mąka" to PantryItem.Product("Mąka", PantryCategory.ZBOZOWE, 500.0, "g"))
        items = PantryOperations.changeCategory(items, "Mąka", PantryCategory.INNE)

        val product = items["Mąka"] as PantryItem.Product
        assertEquals(PantryCategory.INNE, product.category)
        assertEquals(500.0, product.quantity)
        assertEquals("g", product.unit)
    }

    @Test
    fun `changeCategory moves a spice to another category, keeping its level`() {
        var items: Map<String, PantryItem> = mapOf("Sól" to PantryItem.Spice("Sól", PantryCategory.PRZYPRAWY, SpiceLevel.MALO))
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
        assertEquals(PantryCategory.STRACZKI, PantryOperations.categoryForCanon("Strączki i orzechy"))
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
        val withProduct: Map<String, PantryItem> = mapOf("mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 500.0, "g"))
        assertTrue(PantryOperations.toggleHaveIngredient(withProduct, "mąka", PantryCategory.ZBOZOWE, "weight").isEmpty())

        val withSpice: Map<String, PantryItem> = mapOf("sól" to PantryItem.Spice("sól", PantryCategory.PRZYPRAWY, SpiceLevel.MALO))
        assertTrue(PantryOperations.toggleHaveIngredient(withSpice, "sól", PantryCategory.PRZYPRAWY, "count").isEmpty())
    }

    // ---- FR-98: deleting a product from the pantry for good ----

    @Test
    fun `visibleTileNames merges recipe-derived and tracked names, minus hidden ones`() {
        val visible = PantryOperations.visibleTileNames(
            recipeTileNames = listOf("mleko", "jajka", "chleb"),
            trackedNames = listOf("jajka", "kawa"),
            hidden = setOf("chleb"),
        )
        assertEquals(listOf("jajka", "kawa", "mleko"), visible)
    }

    @Test
    fun `hiding a product survives it still being an ingredient of some recipe`() {
        val hidden = PantryOperations.hideForever(emptySet(), "chleb")
        val visible = PantryOperations.visibleTileNames(listOf("chleb", "mleko"), emptyList(), hidden)
        assertEquals(listOf("mleko"), visible)
    }

    @Test
    fun `restoreAllHidden brings every deleted product back`() {
        val hidden = PantryOperations.hideForever(PantryOperations.hideForever(emptySet(), "chleb"), "mleko")
        assertEquals(2, hidden.size)
        val visible = PantryOperations.visibleTileNames(listOf("chleb", "mleko"), emptyList(), PantryOperations.restoreAllHidden())
        assertEquals(listOf("chleb", "mleko"), visible)
    }
}
