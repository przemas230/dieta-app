package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

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
}
