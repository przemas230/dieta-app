package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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

/** A recipe whose macros exactly match `target`, so RecipeMatching.matchScore gives it a perfect (or tied) score. */
private fun matchingRecipe(id: String, cat: String, target: MacroGrams, name: String = "Danie $id") = Recipe(
    id = id,
    cat = cat,
    name = name,
    time = "10 min",
    kcal = 400,
    ingredients = emptyList(),
    method = "",
    protein = target.protein.toDouble(),
    carbs = target.carbs.toDouble(),
    fat = target.fat.toDouble(),
    fiber = null,
    gi = null,
    gl = 0.0,
)

private val testProfile = Profile(strictLowGI = false, configured = true)
private val testMacroTargets = MacroTargets(
    daily = MacroGrams(100, 100, 100),
    sniadania = MacroGrams(20, 20, 10),
    drugie = MacroGrams(20, 20, 10),
    obiady = MacroGrams(30, 30, 15),
    kolacje = MacroGrams(20, 20, 10),
    deser = MacroGrams(10, 10, 5),
)
private val testKcalTargets = DailyCalorieTargets(daily = 1500, sniadania = 300, drugie = 300, obiady = 450, kolacje = 300, deser = 150)

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

    @Test
    fun `clearDay wipes only the targeted day`() {
        var plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r1"))
        plan = PlannerOperations.setMeal(plan, 1, "obiady", PlannedMeal("r2"))

        plan = PlannerOperations.clearDay(plan, 0)

        assertTrue(plan[0].orEmpty().isEmpty())
        assertEquals("r2", plan[1]?.get("obiady")?.recipeId)
    }

    @Test
    fun `planLeftover sets a base-scale leftover entry`() {
        val plan = PlannerOperations.planLeftover(emptyMap(), 2, "obiady", "r1")
        val meal = plan[2]?.get("obiady")
        assertEquals("r1", meal?.recipeId)
        assertEquals(1.0, meal?.scale)
        assertTrue(meal?.isLeftover == true)
    }

    @Test
    fun `isPrepAheadFriendly matches keyword recipes only in obiady or kolacje`() {
        assertTrue(PlannerOperations.isPrepAheadFriendly(recipe("r1", 400, cat = "obiady").copy(name = "Gulasz wołowy")))
        assertTrue(PlannerOperations.isPrepAheadFriendly(recipe("r2", 400, cat = "kolacje").copy(name = "Zupa krem z dyni")))
        assertFalse(PlannerOperations.isPrepAheadFriendly(recipe("r3", 400, cat = "sniadania").copy(name = "Gulasz wołowy")))
        assertFalse(PlannerOperations.isPrepAheadFriendly(recipe("r4", 400, cat = "obiady").copy(name = "Sałatka z kurczakiem")))
    }

    @Test
    fun `prepAheadSuggestion offers the previous day's pick for an empty slot`() {
        val gulasz = recipe("r1", 400, cat = "obiady").copy(name = "Gulasz wołowy")
        val recipesById = mapOf("r1" to gulasz)
        val plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r1"))

        assertEquals(gulasz, PlannerOperations.prepAheadSuggestion(plan, 1, "obiady", recipesById))
    }

    @Test
    fun `prepAheadSuggestion is null when the slot is filled, the source was itself leftovers, or the dish isn't prep-ahead-friendly`() {
        val gulasz = recipe("r1", 400, cat = "obiady").copy(name = "Gulasz wołowy")
        val salatka = recipe("r2", 400, cat = "obiady").copy(name = "Sałatka z kurczakiem")
        val recipesById = mapOf("r1" to gulasz, "r2" to salatka)

        var plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r1"))
        plan = PlannerOperations.setMeal(plan, 1, "obiady", PlannedMeal("r2"))
        assertNull(PlannerOperations.prepAheadSuggestion(plan, 1, "obiady", recipesById)) // slot 1 already filled

        val leftoverPlan = PlannerOperations.planLeftover(emptyMap(), 0, "obiady", "r1")
        assertNull(PlannerOperations.prepAheadSuggestion(leftoverPlan, 1, "obiady", recipesById)) // day 0 was itself leftovers

        val nonPrepPlan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("r2"))
        assertNull(PlannerOperations.prepAheadSuggestion(nonPrepPlan, 1, "obiady", recipesById)) // sałatka isn't prep-ahead-friendly

        assertNull(PlannerOperations.prepAheadSuggestion(emptyMap(), 1, "obiady", recipesById)) // day 0 empty
    }

    @Test
    fun `fittingPool returns at least 3 top-scoring recipes from the same category`() {
        val target = testMacroTargets.obiady!!
        val recipes = (1..5).map { matchingRecipe("obiady-$it", "obiady", target) } +
            listOf(matchingRecipe("kolacje-1", "kolacje", testMacroTargets.kolacje!!))

        val pool = PlannerOperations.fittingPool(recipes, "obiady", testProfile, testMacroTargets)

        assertEquals(3, pool.size)
        assertTrue(pool.all { it.cat == "obiady" })
    }

    @Test
    fun `fittingPool falls back to the whole category when nothing scores`() {
        val unscored = listOf(recipe("r1", 400, cat = "obiady"), recipe("r2", 400, cat = "obiady"))
        val pool = PlannerOperations.fittingPool(unscored, "obiady", testProfile, testMacroTargets)
        assertEquals(2, pool.size)
    }

    private fun fixtureRecipes(perCategory: Int) = PlannerOperations.PLANNER_CATEGORIES.flatMap { cat ->
        val target = testMacroTargets.forCategory(cat.id)!!
        (1..perCategory).map { matchingRecipe("${cat.id}-$it", cat.id, target) }
    }

    @Test
    fun `randomizeDay fills every category from its own category's pool`() {
        val recipes = fixtureRecipes(perCategory = 3)
        val recipesById = recipes.associateBy { it.id }

        val plan = PlannerOperations.randomizeDay(emptyMap(), 0, recipes, testProfile, testMacroTargets, testKcalTargets, Random(42))

        val dayMeals = plan[0].orEmpty()
        assertEquals(PlannerOperations.PLANNER_CATEGORIES.size, dayMeals.size)
        PlannerOperations.PLANNER_CATEGORIES.forEach { cat ->
            val meal = dayMeals[cat.id]
            assertEquals(cat.id, recipesById[meal?.recipeId]?.cat)
        }
    }

    @Test
    fun `randomizeWeek round-robins each category's shuffled pool without repeats within one cycle`() {
        val recipes = fixtureRecipes(perCategory = 3)

        val plan = PlannerOperations.randomizeWeek(recipes, testProfile, testMacroTargets, testKcalTargets, Random(7))

        val obiadyPicks = (0..2).map { day -> plan[day]?.get("obiady")?.recipeId }
        assertEquals(3, obiadyPicks.toSet().size) // all 3 distinct within the first cycle
        assertEquals(obiadyPicks[0], plan[3]?.get("obiady")?.recipeId) // cycle wraps exactly
        for (day in 0..6) {
            assertEquals(PlannerOperations.PLANNER_CATEGORIES.size, plan[day].orEmpty().size)
        }
    }

    @Test
    fun `regenerateSlot avoids the current pick when another option exists`() {
        val recipes = fixtureRecipes(perCategory = 3)
        val plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("obiady-1"))

        repeat(20) {
            val result = PlannerOperations.regenerateSlot(
                plan, 0, "obiady", recipes, testProfile, testMacroTargets, testKcalTargets, Random(it),
            )
            assertFalse(result[0]?.get("obiady")?.recipeId == "obiady-1")
        }
    }

    @Test
    fun `regenerateSlot falls back to the same recipe when it's the only option`() {
        val recipes = listOf(matchingRecipe("obiady-1", "obiady", testMacroTargets.obiady!!))
        val plan = PlannerOperations.setMeal(emptyMap(), 0, "obiady", PlannedMeal("obiady-1"))

        val result = PlannerOperations.regenerateSlot(plan, 0, "obiady", recipes, testProfile, testMacroTargets, testKcalTargets)

        assertEquals("obiady-1", result[0]?.get("obiady")?.recipeId)
    }
}
