package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private fun recipe(id: String, kcal: Int, cat: String = "obiady") = Recipe(
    id = id,
    cat = cat,
    name = "Test $id",
    time = "10 min",
    kcal = kcal,
    ingredients = emptyList(),
    method = "",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
)

class PlannerOperationsTest {
    @Test
    fun `setMeal fills a slot without touching other days or categories`() {
        var plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r1"))
        plan = PlannerOperations.setMeal(plan, 0, "kolacje", PlannedMeal("r2"))
        plan = PlannerOperations.setMeal(plan, 1, "obiady", PlannedMeal("r3"))

        assertEquals("r1", plan[0]?.get("obiady")?.recipeId)
        assertEquals("r2", plan[0]?.get("kolacje")?.recipeId)
        assertEquals("r3", plan[1]?.get("obiady")?.recipeId)
    }

    @Test
    fun `clearSlot removes only the targeted day-category pair`() {
        var plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r1"))
        plan = PlannerOperations.setMeal(plan, 0, "kolacje", PlannedMeal("r2"))

        plan = PlannerOperations.clearSlot(plan, 0, "obiady")

        assertNull(plan[0]?.get("obiady"))
        assertEquals("r2", plan[0]?.get("kolacje")?.recipeId)
    }

    @Test
    fun `setScale updates only the scale, keeping recipeId and leftover flag`() {
        var plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r1", scale = 1.0, isLeftover = true))
        plan = PlannerOperations.setScale(plan, 0, "obiady", 1.5)

        val meal = plan[0]?.get("obiady")
        assertEquals("r1", meal?.recipeId)
        assertEquals(1.5, meal?.scale)
        assertTrue(meal?.isLeftover == true)
    }

    @Test
    fun `setScale on an empty slot is a no-op`() {
        val plan = PlannerOperations.setScale(emptyMap(), 0, "obiady", 1.5)
        assertTrue(plan.isEmpty())
    }

    @Test
    fun `nextScaleStep cycles through SCALE_STEPS and wraps around`() {
        assertEquals(1.25, PlannerOperations.nextScaleStep(1.0))
        assertEquals(1.5, PlannerOperations.nextScaleStep(1.25))
        assertEquals(2.0, PlannerOperations.nextScaleStep(1.75))
        assertEquals(1.0, PlannerOperations.nextScaleStep(2.0))
    }

    @Test
    fun `idealScaleFor picks the SCALE_STEPS multiple closest to the target kcal`() {
        val r = recipe("r1", kcal = 200)
        assertEquals(1.0, PlannerOperations.idealScaleFor(r, 210))
        assertEquals(2.0, PlannerOperations.idealScaleFor(r, 400))
        assertEquals(1.5, PlannerOperations.idealScaleFor(r, 290))
    }

    @Test
    fun `idealScaleFor falls back to 1x for a null or zero target`() {
        val r = recipe("r1", kcal = 200)
        assertEquals(1.0, PlannerOperations.idealScaleFor(r, null))
        assertEquals(1.0, PlannerOperations.idealScaleFor(r, 0))
    }

    @Test
    fun `dayTotalKcal sums the scaled kcal of every filled slot for that day only`() {
        val recipesById = mapOf("r1" to recipe("r1", kcal = 200), "r2" to recipe("r2", kcal = 300))
        var plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r1", scale = 1.5))
        plan = PlannerOperations.setMeal(plan, 0, "kolacje", PlannedMeal("r2", scale = 1.0))
        plan = PlannerOperations.setMeal(plan, 1, "obiady", PlannedMeal("r1", scale = 2.0))

        assertEquals(600, PlannerOperations.dayTotalKcal(plan, 0, recipesById))
        assertEquals(400, PlannerOperations.dayTotalKcal(plan, 1, recipesById))
        assertEquals(0, PlannerOperations.dayTotalKcal(plan, 2, recipesById))
    }
}
