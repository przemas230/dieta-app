package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private fun recipeWithIngredients(id: String, vararg ingredients: String) = Recipe(
    id = id,
    cat = "obiady",
    name = "Test $id",
    time = "10 min",
    kcal = 100,
    ingredients = ingredients.toList(),
    method = "",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
)

class PantryTilesTest {

    @Test
    fun `tileStep is 100 for weight and volume, 1 for everything else`() {
        assertEquals(100.0, PantryTiles.tileStep("weight"))
        assertEquals(100.0, PantryTiles.tileStep("volume"))
        assertEquals(1.0, PantryTiles.tileStep("count"))
        assertEquals(1.0, PantryTiles.tileStep("unknown"))
    }

    @Test
    fun `unitCatToUnit maps the 3 simplified buckets to concrete units`() {
        assertEquals("g", PantryTiles.unitCatToUnit("weight"))
        assertEquals("ml", PantryTiles.unitCatToUnit("volume"))
        assertEquals("szt.", PantryTiles.unitCatToUnit("count"))
    }

    @Test
    fun `buildTileNames returns the deduplicated canonical ingredient set across all recipes`() {
        val recipes = listOf(
            recipeWithIngredients("r1", "2 jajka", "150g piersi z kurczaka"),
            recipeWithIngredients("r2", "jajko na twardo", "1 cebula"),
        )
        val names = PantryTiles.buildTileNames(recipes)
        assertTrue(names.contains("jajka"))
        assertTrue(names.contains("kurczak (pierś)"))
        assertTrue(names.contains("cebula"))
        // "2 jajka" and "jajko na twardo" both canonicalize to "jajka" -- deduplicated, not counted twice.
        assertEquals(3, names.size)
    }

    @Test
    fun `computeTileUnitCats picks the majority unit category per ingredient`() {
        val recipes = listOf(
            recipeWithIngredients("r1", "150g piersi z kurczaka"),
            recipeWithIngredients("r2", "200g piersi z kurczaka"),
            recipeWithIngredients("r3", "1 pierś z kurczaka"),
        )
        val unitCats = PantryTiles.computeTileUnitCats(recipes)
        assertEquals("weight", unitCats["kurczak (pierś)"])
    }

    @Test
    fun `categoryAndEmoji looks up a known canonical name`() {
        val (category, emoji) = PantryTiles.categoryAndEmoji("jajka")
        assertEquals(PantryCategory.MIESO, category)
        assertEquals("🥚", emoji)
    }

    @Test
    fun `categoryAndEmoji falls back to Inne and a generic emoji for an unknown name`() {
        val (category, emoji) = PantryTiles.categoryAndEmoji("nieznany-skladnik-xyz")
        assertEquals(PantryCategory.INNE, category)
        assertEquals("🍽️", emoji)
    }

    @Test
    fun `CATEGORY_ORDER has all 8 categories, Inne last`() {
        assertEquals(8, PantryTiles.CATEGORY_ORDER.size)
        assertEquals(PantryCategory.INNE, PantryTiles.CATEGORY_ORDER.last())
        assertEquals(PantryCategory.entries.toSet(), PantryTiles.CATEGORY_ORDER.toSet())
    }
}
