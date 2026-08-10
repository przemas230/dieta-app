package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private fun recipe(id: String, vararg ingredients: String) = Recipe(
    id = id,
    cat = "obiady",
    name = "Danie $id",
    time = "10 min",
    kcal = 400,
    ingredients = ingredients.toList(),
    method = "",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
)

class ShoppingOperationsTest {
    @Test
    fun `addRecipe adds every ingredient keyed by canon name and unit category`() {
        val items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "500 g mąki", "2 jajka"))

        assertEquals(500.0, items["mąka|weight"]?.quantity)
        assertEquals(2.0, items["jajka|count"]?.quantity)
    }

    @Test
    fun `the same ingredient from two recipes sums into one entry`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "500 g mąki"))
        items = ShoppingOperations.addRecipe(items, recipe("r2", "300 g mąki"))

        assertEquals(800.0, items["mąka|weight"]?.quantity)
        assertEquals(500.0, items["mąka|weight"]?.contributions?.get("r1"))
        assertEquals(300.0, items["mąka|weight"]?.contributions?.get("r2"))
    }

    @Test
    fun `removeRecipe subtracts only that recipe's share, leaving the other recipe's contribution intact`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "500 g mąki"))
        items = ShoppingOperations.addRecipe(items, recipe("r2", "300 g mąki"))

        items = ShoppingOperations.removeRecipe(items, recipe("r1", "500 g mąki"))

        assertEquals(300.0, items["mąka|weight"]?.quantity)
        assertNull(items["mąka|weight"]?.contributions?.get("r1"))
        assertEquals(300.0, items["mąka|weight"]?.contributions?.get("r2"))
    }

    @Test
    fun `removeRecipe deletes the entry once its last contribution is gone`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "500 g mąki"))
        items = ShoppingOperations.removeRecipe(items, recipe("r1", "500 g mąki"))

        assertTrue(items.isEmpty())
    }

    @Test
    fun `isRecipeAdded reflects whether any entry still carries that recipe's contribution`() {
        val items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "500 g mąki"))
        assertTrue(ShoppingOperations.isRecipeAdded(items, "r1"))
        assertFalse(ShoppingOperations.isRecipeAdded(items, "r2"))

        val removed = ShoppingOperations.removeRecipe(items, recipe("r1", "500 g mąki"))
        assertFalse(ShoppingOperations.isRecipeAdded(removed, "r1"))
    }

    @Test
    fun `addSingleIngredient accumulates under its own source key without touching a recipe's own contribution`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "2 jajka"))
        items = ShoppingOperations.addSingleIngredient(items, "3 jajka", "single:r2:jajka")

        assertEquals(5.0, items["jajka|count"]?.quantity)
        assertEquals(2.0, items["jajka|count"]?.contributions?.get("r1"))
        assertEquals(3.0, items["jajka|count"]?.contributions?.get("single:r2:jajka"))
    }

    @Test
    fun `toggleChecked flips the checked flag for the composite key`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "2 jajka"))
        items = ShoppingOperations.toggleChecked(items, "jajka|count")
        assertTrue(items["jajka|count"]!!.checked)

        items = ShoppingOperations.toggleChecked(items, "jajka|count")
        assertFalse(items["jajka|count"]!!.checked)
    }

    @Test
    fun `toggleChecked on a missing key is a no-op`() {
        assertTrue(ShoppingOperations.toggleChecked(emptyMap(), "nieistniejące|count").isEmpty())
    }

    @Test
    fun `removeItem drops the entry regardless of contributions`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "2 jajka"))
        items = ShoppingOperations.removeItem(items, "jajka|count")
        assertTrue(items.isEmpty())
    }

    @Test
    fun `addDayPlan adds every planned meal's scaled ingredients`() {
        val recipesById = mapOf("r1" to recipe("r1", "2 jajka"), "r2" to recipe("r2", "500 g mąki"))
        val dayMeals = mapOf(
            "sniadania" to PlannedMeal("r1", scale = 1.5),
            "obiady" to PlannedMeal("r2", scale = 1.0),
        )

        val items = ShoppingOperations.addDayPlan(emptyMap(), dayMeals, recipesById)

        assertEquals(3.0, items["jajka|count"]?.quantity) // 2 * 1.5, rounded by scaleIngredientText to "3 jajka"
        assertEquals(500.0, items["mąka|weight"]?.quantity)
    }

    @Test
    fun `addDayPlan skips a recipe id that's already on the list`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "2 jajka"))
        val dayMeals = mapOf("sniadania" to PlannedMeal("r1", scale = 1.0))

        items = ShoppingOperations.addDayPlan(items, dayMeals, mapOf("r1" to recipe("r1", "2 jajka")))

        assertEquals(2.0, items["jajka|count"]?.quantity) // unchanged, not doubled
    }

    @Test
    fun `addDayPlanWithSummary reports added and already counts alongside the mutated items`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "2 jajka"))
        val dayMeals = mapOf(
            "sniadania" to PlannedMeal("r1", scale = 1.0),
            "obiady" to PlannedMeal("r2", scale = 1.0),
        )
        val recipesById = mapOf("r1" to recipe("r1", "2 jajka"), "r2" to recipe("r2", "500 g mąki"))

        val result = ShoppingOperations.addDayPlanWithSummary(items, dayMeals, recipesById)

        assertEquals(1, result.added)
        assertEquals(1, result.already)
        assertEquals(500.0, result.items["mąka|weight"]?.quantity)
    }

    @Test
    fun `addWeekPlan adds across every day and still dedups a repeated recipe id`() {
        val recipesById = mapOf("r1" to recipe("r1", "2 jajka"), "r2" to recipe("r2", "500 g mąki"))
        val plan = mapOf(
            0 to mapOf("sniadania" to PlannedMeal("r1", scale = 1.0)),
            1 to mapOf("sniadania" to PlannedMeal("r1", scale = 1.0), "obiady" to PlannedMeal("r2", scale = 1.0)),
        )

        val items = ShoppingOperations.addWeekPlan(emptyMap(), plan, recipesById)

        assertEquals(2.0, items["jajka|count"]?.quantity) // r1 only added once across the week
        assertEquals(500.0, items["mąka|weight"]?.quantity)
    }

    @Test
    fun `clearChecked keeps only unchecked items`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "1 l mleka", "6 jajka"))
        items = ShoppingOperations.toggleChecked(items, "mleko|volume")

        items = ShoppingOperations.clearChecked(items)

        assertEquals(setOf("jajka|count"), items.keys)
    }

    @Test
    fun `clearAll drops every item regardless of checked state`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "1 l mleka", "6 jajka"))
        items = ShoppingOperations.toggleChecked(items, "mleko|volume")

        items = ShoppingOperations.clearAll(items)

        assertTrue(items.isEmpty())
    }

    @Test
    fun `buildShareText reports an empty list when there is nothing unchecked`() {
        assertEquals("Lista zakupów jest pusta 🎉", ShoppingOperations.buildShareText(emptyMap()))

        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "6 jajka"))
        items = ShoppingOperations.toggleChecked(items, "jajka|count")
        assertEquals("Lista zakupów jest pusta 🎉", ShoppingOperations.buildShareText(items))
    }

    @Test
    fun `buildShareText lists unchecked items and skips checked ones`() {
        var items = ShoppingOperations.addRecipe(emptyMap(), recipe("r1", "1 l mleka", "6 jajka"))
        items = ShoppingOperations.toggleChecked(items, "mleko|volume")

        val text = ShoppingOperations.buildShareText(items)

        assertTrue(text.startsWith("🛒 Lista zakupów:"))
        assertTrue(text.contains("jaj"))
        assertFalse(text.contains("mlek"))
    }
}
