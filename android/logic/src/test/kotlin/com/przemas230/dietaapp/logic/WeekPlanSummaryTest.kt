package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * FR-100 (ported to Android 2026-08-29). The two judgement calls in this
 * summary -- averaging over PLANNED days only, and reporting macros only
 * from dishes that carry them -- are the whole reason the number can be
 * trusted, so they are pinned down here.
 */
class WeekPlanSummaryTest {
    private fun recipe(id: String, kcal: Int, macros: Boolean = true) = Recipe(
        id = id,
        cat = "obiady",
        name = "Danie $id",
        kcal = kcal,
        time = "10 min",
        ingredients = listOf("100 g czegoś"),
        method = "Zrób",
        protein = if (macros) 20.0 else null,
        carbs = if (macros) 30.0 else null,
        fat = if (macros) 10.0 else null,
        fiber = null,
        gi = null,
        gl = null,
    )

    private fun plan(vararg days: Pair<Int, List<Pair<String, String>>>): WeekPlan =
        days.associate { (day, meals) ->
            day to meals.associate { (cat, id) -> cat to PlannedMeal(id, 1.0, false) }
        }

    @Test
    fun `nothing planned means no card at all`() {
        assertNull(WeekPlanSummary.compute(emptyMap(), emptyMap()))
    }

    @Test
    fun `the average is over PLANNED days, not over seven`() {
        val recipes = mapOf("a" to recipe("a", 600), "b" to recipe("b", 400))
        val weekPlan = plan(0 to listOf("obiady" to "a"), 1 to listOf("obiady" to "b"))
        val summary = WeekPlanSummary.compute(weekPlan, recipes.mapValues { it.value })!!
        assertEquals(2, summary.plannedDays)
        assertEquals(1000, summary.totalKcal)
        // 500, not 1000/7 -- a half-planned week must not read as a crash diet.
        assertEquals(500, summary.avgKcal)
    }

    @Test
    fun `dishes without macros are counted for kcal but excluded from the macro average`() {
        val recipes = mapOf("a" to recipe("a", 600), "b" to recipe("b", 400, macros = false))
        val weekPlan = plan(0 to listOf("obiady" to "a", "kolacje" to "b"))
        val summary = WeekPlanSummary.compute(weekPlan, recipes)!!
        assertEquals(2, summary.totalMeals)
        assertEquals(1, summary.macroMeals)
        assertEquals(1000, summary.totalKcal)
        assertEquals(20, summary.avgProtein)
    }

    @Test
    fun `a week with no macros anywhere reports null rather than zero`() {
        val recipes = mapOf("a" to recipe("a", 600, macros = false))
        val summary = WeekPlanSummary.compute(plan(0 to listOf("obiady" to "a")), recipes)!!
        assertNull(summary.avgProtein)
        assertEquals(0, summary.macroMeals)
    }

    @Test
    fun `being within 50 kcal of the target counts as on target`() {
        assertTrue(WeekPlanSummary.targetComparison(1500, 1480).second)
        assertTrue(WeekPlanSummary.targetComparison(1430, 1480).second)
        val (label, onTarget) = WeekPlanSummary.targetComparison(1700, 1480)
        assertEquals(false, onTarget)
        assertEquals("+220 kcal vs cel 1480", label)
    }

    // ---- FR-110: realizacja tygodnia ----

    @Test
    fun `realization counts only planned slots up to today`() {
        val plan = plan(
            0 to listOf("obiady" to "r1", "kolacje" to "r2"),
            1 to listOf("obiady" to "r3"),
            5 to listOf("obiady" to "r4"),
        )

        val result = WeekPlanSummary.realization(plan, todayDayIndex = 1) { day, cat ->
            day == 0 && cat == "obiady"
        }

        assertEquals(3, result?.plannedSoFar, "Friday is still ahead and must not count against the week")
        assertEquals(1, result?.eatenMeals)
        assertEquals(33, result?.percent)
    }

    @Test
    fun `a perfectly followed Monday is 100 percent, not a fraction of the week`() {
        val plan = plan(
            0 to listOf("obiady" to "r1"),
            3 to listOf("obiady" to "r2"),
            4 to listOf("obiady" to "r3"),
        )

        val result = WeekPlanSummary.realization(plan, todayDayIndex = 0) { _, _ -> true }

        assertEquals(100, result?.percent)
    }

    @Test
    fun `eating something that was never planned cannot raise the number`() {
        val plan = plan(0 to listOf("obiady" to "r1"))

        val result = WeekPlanSummary.realization(plan, todayDayIndex = 0) { _, cat ->
            cat == "obiady" || cat == "deser"
        }

        assertEquals(1, result?.plannedSoFar)
        assertEquals(1, result?.eatenMeals)
    }

    @Test
    fun `nothing planned so far means no row at all`() {
        val plan = plan(5 to listOf("obiady" to "r1"))

        assertNull(WeekPlanSummary.realization(plan, todayDayIndex = 2) { _, _ -> true })
    }

    @Test
    fun `percentages round to the nearest whole`() {
        assertEquals(67, WeekPlanSummary.Realization(2, 3).percent)
        assertEquals(0, WeekPlanSummary.Realization(0, 4).percent)
    }
}
