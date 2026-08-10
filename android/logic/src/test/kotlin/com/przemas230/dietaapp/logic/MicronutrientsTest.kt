package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeCalcItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

private fun calcItem(label: String, qty: Double) = RecipeCalcItem(
    label = label,
    unit = "1 szt.",
    qty = qty,
    baseKcal = 100,
    baseP = 0.0,
    baseC = 0.0,
    baseF = 0.0,
    kcal = 100,
    p = 0.0,
    c = 0.0,
    f = 0.0,
)

private fun recipeWithCalc(calc: List<RecipeCalcItem>) = Recipe(
    id = "x",
    cat = "obiady",
    name = "Test",
    time = "10 min",
    kcal = calc.sumOf { it.kcal },
    ingredients = emptyList(),
    method = "",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
    calc = calc,
)

class MicronutrientsTest {
    @Test
    fun `estimate is null for a recipe with no calc data`() {
        assertNull(Micronutrients.estimate(recipeWithCalc(emptyList())))
    }

    @Test
    fun `estimate is null when no ingredient is on the recognized list`() {
        val recipe = recipeWithCalc(listOf(calcItem("sól", 1.0), calcItem("cukier", 1.0)))
        assertNull(Micronutrients.estimate(recipe))
    }

    @Test
    fun `estimate scales with qty exactly like baseKcal does`() {
        val recipe = recipeWithCalc(listOf(calcItem("2 jajka", 2.0)))
        val result = Micronutrients.estimate(recipe)
        assertEquals(50, result?.ca) // 25 * 2
        assertEquals(2.2, result?.vitD) // 1.1 * 2
        assertEquals(0.9, result?.b12) // 0.45 * 2
    }

    @Test
    fun `estimate sums across every recognized ingredient and ignores unrecognized ones`() {
        val recipe = recipeWithCalc(
            listOf(calcItem("2 jajka", 2.0), calcItem("100 g szpinaku", 1.0), calcItem("sól", 1.0)),
        )
        val result = Micronutrients.estimate(recipe)
        assertEquals(77, result?.ca) // 25*2 + 27*1, sól ignored
    }
}
