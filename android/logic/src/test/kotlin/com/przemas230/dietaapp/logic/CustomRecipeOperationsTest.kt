package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CustomRecipeOperationsTest {

    private fun validInput(overrides: (CustomRecipeOperations.Input) -> CustomRecipeOperations.Input = { it }) =
        overrides(
            CustomRecipeOperations.Input(
                name = "Sałatka testowa",
                cat = "obiady",
                time = "10 min",
                ingredientsText = "cebula\npomidor",
                method = "Wymieszaj.",
                kcalText = "350",
                proteinText = "20",
                carbsText = "",
                fatText = "10,5",
            ),
        )

    @Test
    fun `validate rejects blank name`() {
        val input = validInput { it.copy(name = "  ") }
        assertEquals(CustomRecipeOperations.ValidationError.MissingName, CustomRecipeOperations.validate(input))
    }

    @Test
    fun `validate rejects no ingredients`() {
        val input = validInput { it.copy(ingredientsText = "\n  \n") }
        assertEquals(CustomRecipeOperations.ValidationError.MissingIngredients, CustomRecipeOperations.validate(input))
    }

    @Test
    fun `validate rejects missing or non-positive kcal`() {
        assertEquals(CustomRecipeOperations.ValidationError.InvalidKcal, CustomRecipeOperations.validate(validInput { it.copy(kcalText = "") }))
        assertEquals(CustomRecipeOperations.ValidationError.InvalidKcal, CustomRecipeOperations.validate(validInput { it.copy(kcalText = "0") }))
        assertEquals(CustomRecipeOperations.ValidationError.InvalidKcal, CustomRecipeOperations.validate(validInput { it.copy(kcalText = "abc") }))
    }

    @Test
    fun `validate accepts a well-formed input`() {
        assertNull(CustomRecipeOperations.validate(validInput()))
    }

    @Test
    fun `build produces a custom-sourced recipe with parsed fields`() {
        val recipe = CustomRecipeOperations.build(validInput(), "custom-123")
        assertNotNull(recipe)
        assertEquals("custom-123", recipe!!.id)
        assertEquals("custom", recipe.source)
        assertEquals(listOf("cebula", "pomidor"), recipe.ingredients)
        assertEquals(350, recipe.kcal)
        assertEquals(20.0, recipe.protein)
        assertEquals(null, recipe.carbs)
        assertEquals(10.5, recipe.fat)
    }

    @Test
    fun `build falls back to default time and method when left blank`() {
        val recipe = CustomRecipeOperations.build(validInput { it.copy(time = "  ", method = "") }, "custom-1")
        assertEquals("15 min", recipe?.time)
        assertEquals("Przygotuj składniki i połącz zgodnie z własnym przepisem.", recipe?.method)
    }

    @Test
    fun `build returns null for invalid input`() {
        assertNull(CustomRecipeOperations.build(validInput { it.copy(kcalText = "-5") }, "custom-1"))
    }
}
