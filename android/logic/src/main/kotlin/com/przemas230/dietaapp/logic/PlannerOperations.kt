package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import kotlin.math.abs

data class PlannerCategory(val id: String, val label: String, val emoji: String)

/** One WeekPlan entry per day index (0=Poniedziałek..6=Niedziela) -> category id -> the filled slot, if any. */
typealias WeekPlan = Map<Int, Map<String, PlannedMeal>>

/**
 * FR-18/19/20: pure port of index.html's Planer state mutators
 * (state.planner/state.plannerScale/state.plannerLeftover, SCALE_STEPS,
 * setPlannerScale/nextScaleStep/idealScaleFor). Random generation, whole-day/
 * week clearing, and the leftover/prep-ahead-suggestion mechanics (FR-21-24,
 * FR-27) are a separate, later PARITY.md step -- see android/PARITY.md.
 */
object PlannerOperations {
    val DAYS_PL = listOf("Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela")

    // Same 5 slots as index.html's CATS -- distinct from RecipeBrowsing's
    // 4-tab CATEGORIES, which merges sniadania+drugie into one browse tab
    // (FR-74) but the Planer still needs them as two independent daily slots.
    val PLANNER_CATEGORIES = listOf(
        PlannerCategory("sniadania", "Śniadanie", "🍳"),
        PlannerCategory("drugie", "II Śniadanie", "🥪"),
        PlannerCategory("obiady", "Obiad", "🍲"),
        PlannerCategory("kolacje", "Kolacja", "🌙"),
        PlannerCategory("deser", "Deser / Przekąska", "🍰"),
    )

    val SCALE_STEPS = listOf(1.0, 1.25, 1.5, 1.75, 2.0)

    fun setMeal(plan: WeekPlan, day: Int, cat: String, meal: PlannedMeal): WeekPlan {
        val dayMap = plan[day].orEmpty() + (cat to meal)
        return plan + (day to dayMap)
    }

    fun clearSlot(plan: WeekPlan, day: Int, cat: String): WeekPlan {
        val dayMap = plan[day].orEmpty() - cat
        return plan + (day to dayMap)
    }

    fun setScale(plan: WeekPlan, day: Int, cat: String, scale: Double): WeekPlan {
        val current = plan[day]?.get(cat) ?: return plan
        return setMeal(plan, day, cat, current.copy(scale = scale))
    }

    fun nextScaleStep(scale: Double): Double {
        val i = SCALE_STEPS.indexOf(scale)
        return SCALE_STEPS[(if (i < 0) 0 else i + 1) % SCALE_STEPS.size]
    }

    /** Smallest SCALE_STEPS multiple landing closest to the slot's kcal target -- so auto-picks default to a sensible size instead of always 1x. */
    fun idealScaleFor(recipe: Recipe, targetKcal: Int?): Double {
        if (targetKcal == null || targetKcal == 0 || recipe.kcal == 0) return 1.0
        var best = 1.0
        var bestDiff = abs(recipe.kcal - targetKcal).toDouble()
        for (step in SCALE_STEPS) {
            val diff = abs(recipe.kcal * step - targetKcal)
            if (diff < bestDiff) {
                bestDiff = diff
                best = step
            }
        }
        return best
    }

    /** Recipe.kcal scaled and rounded, matching index.html's scaleRecipe (Math.round(r.kcal*scale)). */
    fun scaledKcal(recipe: Recipe, scale: Double): Int = Math.round(recipe.kcal * scale).toInt()

    /** Sum of scaled kcal across all 5 slots for one day, for the day-card's "Razem: N kcal" footer. */
    fun dayTotalKcal(plan: WeekPlan, day: Int, recipesById: Map<String, Recipe>): Int {
        val dayMap = plan[day] ?: return 0
        return dayMap.values.sumOf { meal ->
            val recipe = recipesById[meal.recipeId] ?: return@sumOf 0
            scaledKcal(recipe, meal.scale)
        }
    }
}
