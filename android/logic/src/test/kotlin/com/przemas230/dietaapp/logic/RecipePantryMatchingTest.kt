package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import com.przemas230.dietaapp.data.ShoppingItem
import org.junit.jupiter.api.Assertions.assertTrue

private fun recipeWithIngredients(vararg ingredients: String) = Recipe(
    id = "x",
    cat = "obiady",
    name = "Test",
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

class RecipePantryMatchingTest {
    @Test
    fun `parseIngredient reads a leading weight quantity and canonicalizes the rest`() {
        val parsed = RecipePantryMatching.parseIngredient("500 g mąki")
        assertEquals("mąka", parsed.canonName)
        assertEquals(500.0, parsed.baseQty)
        assertEquals("weight", parsed.unitCat)
    }

    @Test
    fun `parseIngredient with no recognized unit word falls back to count`() {
        val parsed = RecipePantryMatching.parseIngredient("2 jajka")
        assertEquals("jajka", parsed.canonName)
        assertEquals(2.0, parsed.baseQty)
        assertEquals("count", parsed.unitCat)
    }

    @Test
    fun `subtractForRecipe decrements matching products by unit-converted quantity`() {
        val pantry = mapOf(
            "mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 1000.0, "g"),
            "jajka" to PantryItem.Product("jajka", PantryCategory.NABIAL, 10.0, "szt."),
        )
        val recipe = recipeWithIngredients("500 g mąki", "2 jajka")

        val result = RecipePantryMatching.subtractForRecipe(pantry, recipe)

        assertEquals(500.0, (result["mąka"] as PantryItem.Product).quantity)
        assertEquals(8.0, (result["jajka"] as PantryItem.Product).quantity)
    }

    @Test
    fun `subtractForRecipe floors at zero instead of going negative`() {
        val pantry = mapOf("mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 100.0, "g"))
        val recipe = recipeWithIngredients("500 g mąki")

        val result = RecipePantryMatching.subtractForRecipe(pantry, recipe)

        assertEquals(0.0, (result["mąka"] as PantryItem.Product).quantity)
    }

    @Test
    fun `subtractForRecipe skips ingredients absent from the pantry or with a mismatched unit category`() {
        val pantry = mapOf(
            "mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 100.0, "szt."),
        )
        val recipe = recipeWithIngredients("500 g mąki", "3 cytryny")

        val result = RecipePantryMatching.subtractForRecipe(pantry, recipe)

        assertEquals(pantry, result)
    }

    @Test
    fun `restoreForRecipe reverses subtractForRecipe`() {
        val pantry = mapOf("mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 1000.0, "g"))
        val recipe = recipeWithIngredients("500 g mąki")

        val subtracted = RecipePantryMatching.subtractForRecipe(pantry, recipe)
        val restored = RecipePantryMatching.restoreForRecipe(subtracted, recipe)

        assertEquals(pantry, restored)
        assertFalse(restored === pantry)
    }

    @Test
    fun `missingAfterPantry returns the shortfall when pantry stock is insufficient`() {
        val pantry = PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 300.0, "g")
        assertEquals(200.0, RecipePantryMatching.missingAfterPantry(500.0, "weight", pantry))
    }

    @Test
    fun `missingAfterPantry returns null (fully covered) when pantry stock meets or exceeds the need`() {
        val exact = PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 500.0, "g")
        assertNull(RecipePantryMatching.missingAfterPantry(500.0, "weight", exact))
        val surplus = PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 800.0, "g")
        assertNull(RecipePantryMatching.missingAfterPantry(500.0, "weight", surplus))
    }

    @Test
    fun `missingAfterPantry converts pantry units to the shopping list's base unit`() {
        val pantry = PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 0.3, "kg")
        assertEquals(200.0, RecipePantryMatching.missingAfterPantry(500.0, "weight", pantry))
    }

    @Test
    fun `missingAfterPantry treats a missing pantry entry as zero coverage`() {
        assertEquals(500.0, RecipePantryMatching.missingAfterPantry(500.0, "weight", null))
    }

    @Test
    fun `missingAfterPantry treats a mismatched unit category as zero coverage`() {
        val pantry = PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 10.0, "szt.")
        assertEquals(500.0, RecipePantryMatching.missingAfterPantry(500.0, "weight", pantry))
    }

    @Test
    fun `pantryCoverageRatio is the fraction of ingredients present in the pantry`() {
        val pantry = mapOf(
            "mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 500.0, "g"),
            "jajka" to PantryItem.Product("jajka", PantryCategory.NABIAL, 2.0, "szt."),
        )
        val recipe = recipeWithIngredients("500 g mąki", "2 jajka", "3 cytryny", "1 cebula")
        assertEquals(0.5, RecipePantryMatching.pantryCoverageRatio(recipe, pantry))
    }

    @Test
    fun `pantryCoverageRatio is 0 for a recipe with no ingredients`() {
        assertEquals(0.0, RecipePantryMatching.pantryCoverageRatio(recipeWithIngredients(), emptyMap()))
    }

    @Test
    fun `pantryCoverageRatio is 1 when every ingredient is tracked`() {
        val pantry = mapOf("mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 500.0, "g"))
        assertEquals(1.0, RecipePantryMatching.pantryCoverageRatio(recipeWithIngredients("500 g mąki"), pantry))
    }

    @Test
    fun `uniqueIngredientNames dedupes canon names across recipes`() {
        val recipes = listOf(
            recipeWithIngredients("500 g mąki", "2 jajka"),
            recipeWithIngredients("300 g mąki", "1 cebula"),
        )
        assertEquals(listOf("cebula", "jajka", "mąka"), RecipePantryMatching.uniqueIngredientNames(recipes))
    }

    @Test
    fun `uniqueIngredientNames is empty for no recipes`() {
        assertEquals(emptyList<String>(), RecipePantryMatching.uniqueIngredientNames(emptyList()))
    }

    // ---- FR-106: stocking the pantry after the shopping is done ----

    private fun boughtRecipe() = Recipe(
        id = "R1", cat = "obiady", name = "Test", time = "10 min", kcal = 400,
        ingredients = listOf("200 g ryżu", "2 jajka"), method = "Zrób",
        protein = null, carbs = null, fat = null, fiber = null, gi = null, gl = null,
    )

    @Test
    fun `stocking creates entries for ingredients the pantry never had`() {
        // The whole point: these are exactly the ones you did not have, so
        // restoreForRecipe (which skips unknown ingredients) cannot do this.
        val stocked = RecipePantryMatching.stockFromRecipe(emptyMap(), boughtRecipe())
        val rice = stocked.values.filterIsInstance<PantryItem.Product>().first { it.name.contains("ry") }
        assertEquals(200.0, rice.quantity)
        assertEquals("g", rice.unit)
        assertEquals(2, stocked.size)
    }

    @Test
    fun `stocking tops up an ingredient that was already tracked`() {
        val before = RecipePantryMatching.stockFromRecipe(emptyMap(), boughtRecipe())
        val after = RecipePantryMatching.stockFromRecipe(before, boughtRecipe())
        val rice = after.values.filterIsInstance<PantryItem.Product>().first { it.name.contains("ry") }
        assertEquals(400.0, rice.quantity)
    }

    @Test
    fun `stocking leaves an entry alone when the units cannot be reconciled`() {
        // Pantry counts eggs in "szt.", a recipe asking for grams of the same
        // canonical name must not be guessed at -- same rule as everywhere
        // else in this file.
        val eggsByPiece = RecipePantryMatching.stockFromRecipe(emptyMap(), boughtRecipe())
            .filterValues { it is PantryItem.Product && it.unit == "szt." }
        val name = eggsByPiece.keys.first()
        val gramsRecipe = boughtRecipe().copy(ingredients = listOf("100 g $name"))
        val after = RecipePantryMatching.stockFromRecipe(eggsByPiece, gramsRecipe)
        assertEquals((eggsByPiece[name] as PantryItem.Product).quantity, (after[name] as PantryItem.Product).quantity)
    }
}
