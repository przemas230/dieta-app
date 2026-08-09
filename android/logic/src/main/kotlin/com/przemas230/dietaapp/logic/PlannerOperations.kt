package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import kotlin.math.abs
import kotlin.math.round
import kotlin.random.Random

data class PlannerCategory(val id: String, val label: String, val emoji: String)

/** One WeekPlan entry per day index (0=Poniedziałek..6=Niedziela) -> category id -> the filled slot, if any. */
typealias WeekPlan = Map<Int, Map<String, PlannedMeal>>

/**
 * FR-18/19/20/21/22/23/24: pure port of index.html's Planer state mutators
 * (state.planner/state.plannerScale/state.plannerLeftover, SCALE_STEPS,
 * setPlannerScale/nextScaleStep/idealScaleFor/fittingPool/isPrepAheadFriendly).
 * FR-27 (add the whole week's ingredients to the shopping list) needs the
 * shared whole-recipe-to-shopping-list mechanism from FR-25, which isn't
 * built yet -- see android/PARITY.md.
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

    private val LEADING_QTY_REGEX = Regex("""^(\d+(?:[.,]\d+)?)(?:/(\d+))?\s*(.*)$""")

    // Matches index.html's formatScaledAmount: snaps to a multiple of 5 once
    // the value is large (nobody needs "747 g", "745" reads fine), otherwise
    // rounds to the nearest half and uses a Polish decimal comma.
    private fun formatScaledAmount(value: Double): String {
        if (value >= 20) return (Math.round(value / 5.0) * 5).toString()
        val rounded = Math.round(value * 2) / 2.0
        return if (rounded == rounded.toLong().toDouble()) rounded.toLong().toString() else rounded.toString().replace('.', ',')
    }

    /**
     * FR-27: scales the leading quantity of a freeform ingredient line ("2
     * jajka" -> "3 jajka" at 1.5x). Lines without a leading number ("garść
     * szpinaku", "sól") are left as-is -- there's nothing reliable to scale.
     * Port of index.html's scaleIngredientText.
     */
    fun scaleIngredientText(text: String, scale: Double): String {
        if (scale == 1.0) return text
        val match = LEADING_QTY_REGEX.find(text) ?: return text
        val (numberPart, denominatorPart, rest) = match.destructured
        var value = numberPart.replace(',', '.').toDoubleOrNull() ?: return text
        if (denominatorPart.isNotEmpty()) value /= denominatorPart.toDouble()
        val scaledLabel = formatScaledAmount(value * scale)
        return if (rest.isNotEmpty()) "$scaledLabel $rest" else scaledLabel
    }

    fun scaleIngredients(ingredients: List<String>, scale: Double): List<String> =
        if (scale == 1.0) ingredients else ingredients.map { scaleIngredientText(it, scale) }

    /** Sum of scaled kcal across all 5 slots for one day, for the day-card's "Razem: N kcal" footer. */
    fun dayTotalKcal(plan: WeekPlan, day: Int, recipesById: Map<String, Recipe>): Int {
        val dayMap = plan[day] ?: return 0
        return dayMap.values.sumOf { meal ->
            val recipe = recipesById[meal.recipeId] ?: return@sumOf 0
            scaledKcal(recipe, meal.scale)
        }
    }

    fun clearDay(plan: WeekPlan, day: Int): WeekPlan = plan + (day to emptyMap())

    /** FR-23/24: carries a recipe over as a leftovers entry (base 1x portion, flagged so no new shopping-list entry is implied). */
    fun planLeftover(plan: WeekPlan, day: Int, cat: String, recipeId: String): WeekPlan =
        setMeal(plan, day, cat, PlannedMeal(recipeId, scale = 1.0, isLeftover = true))

    // Rough reheats-well heuristic (FR-24) -- one-pot/braised/grain-based
    // obiady & kolacje are the dishes people actually batch-cook, so a
    // keyword match on the name stands in for a real "nadaje się na 2 dni"
    // field, same tradeoff index.html makes.
    private val MEAL_PREP_KEYWORDS = listOf(
        "gulasz", "curry", "chili", "zapiekanka", "kasza", "ryż", "ryżem", "zupa", "krem", "gołąbki",
        "leczo", "bigos", "risotto", "dal", "dhal", "żeberka", "pieczeń", "klopsiki", "pulpety",
        "farsz", "gniazdka", "potrawka", "chłodnik",
    )

    fun isPrepAheadFriendly(recipe: Recipe): Boolean {
        if (recipe.cat != "obiady" && recipe.cat != "kolacje") return false
        val name = recipe.name.lowercase()
        return MEAL_PREP_KEYWORDS.any { name.contains(it) }
    }

    /** FR-24: for an empty slot, the previous day's non-leftover prep-ahead-friendly pick to offer carrying over -- never suggested over an existing choice. */
    fun prepAheadSuggestion(plan: WeekPlan, day: Int, cat: String, recipesById: Map<String, Recipe>): Recipe? {
        if (plan[day]?.get(cat) != null) return null
        val prevDay = (day + 6) % 7
        val prevMeal = plan[prevDay]?.get(cat) ?: return null
        if (prevMeal.isLeftover) return null
        val prevRecipe = recipesById[prevMeal.recipeId] ?: return null
        return if (isPrepAheadFriendly(prevRecipe)) prevRecipe else null
    }

    /**
     * FR-21: candidate pool for random picks -- only the best-fitting top
     * ~20% (min 3) of a category's recipes for this profile, so auto-planning
     * can't land wildly off the meal-slot's macro/kcal target while still
     * leaving room for variety across the week. Falls back to the full
     * category if nothing scores (e.g. profile not configured).
     */
    fun fittingPool(recipes: List<Recipe>, cat: String, profile: Profile, macroTargets: MacroTargets): List<Recipe> {
        val target = macroTargets.forCategory(cat)
        val inCat = recipes.filter { it.cat == cat }
        val scored = inCat
            .mapNotNull { r -> RecipeMatching.matchScore(r, target, profile)?.let { r to it } }
            .sortedByDescending { it.second }
        if (scored.isEmpty()) return inCat
        val topN = maxOf(3, round(scored.size * 0.2).toInt())
        return scored.take(topN).map { it.first }
    }

    /** FR-21: "🎲 Losuj ten dzień" -- independent random pick per category, overwriting only this one day. */
    fun randomizeDay(
        plan: WeekPlan,
        day: Int,
        recipes: List<Recipe>,
        profile: Profile,
        macroTargets: MacroTargets,
        kcalTargets: DailyCalorieTargets,
        random: Random = Random.Default,
    ): WeekPlan {
        var dayMap = emptyMap<String, PlannedMeal>()
        PLANNER_CATEGORIES.forEach { cat ->
            val pool = fittingPool(recipes, cat.id, profile, macroTargets)
            if (pool.isNotEmpty()) {
                val pick = pool[random.nextInt(pool.size)]
                dayMap = dayMap + (cat.id to PlannedMeal(pick.id, idealScaleFor(pick, kcalTargets.forCategory(cat.id))))
            }
        }
        return plan + (day to dayMap)
    }

    /**
     * FR-21: "🎲 Wygeneruj losowo cały tydzień" -- shuffles each category's
     * fitting pool once, then round-robins through it across the 7 days
     * (same as index.html's autoPlanBtn), so a small pool still gives
     * variety instead of repeating the same dish immediately.
     */
    fun randomizeWeek(
        recipes: List<Recipe>,
        profile: Profile,
        macroTargets: MacroTargets,
        kcalTargets: DailyCalorieTargets,
        random: Random = Random.Default,
    ): WeekPlan {
        val pools = PLANNER_CATEGORIES.associate { cat -> cat.id to fittingPool(recipes, cat.id, profile, macroTargets).shuffled(random) }
        val nextIndex = PLANNER_CATEGORIES.associate { it.id to 0 }.toMutableMap()
        var plan: WeekPlan = emptyMap()
        for (day in 0..6) {
            var dayMap = emptyMap<String, PlannedMeal>()
            PLANNER_CATEGORIES.forEach { cat ->
                val pool = pools.getValue(cat.id)
                if (pool.isNotEmpty()) {
                    val i = nextIndex.getValue(cat.id)
                    val pick = pool[i % pool.size]
                    nextIndex[cat.id] = i + 1
                    dayMap = dayMap + (cat.id to PlannedMeal(pick.id, idealScaleFor(pick, kcalTargets.forCategory(cat.id))))
                }
            }
            plan = plan + (day to dayMap)
        }
        return plan
    }

    /** "🔁 Losuj inne danie" for one slot -- rerolls within the fitting pool, avoiding the current pick when another option exists. */
    fun regenerateSlot(
        plan: WeekPlan,
        day: Int,
        cat: String,
        recipes: List<Recipe>,
        profile: Profile,
        macroTargets: MacroTargets,
        kcalTargets: DailyCalorieTargets,
        random: Random = Random.Default,
    ): WeekPlan {
        val pool = fittingPool(recipes, cat, profile, macroTargets)
        if (pool.isEmpty()) return plan
        val currentId = plan[day]?.get(cat)?.recipeId
        val candidates = pool.filter { it.id != currentId }.ifEmpty { pool }
        val pick = candidates[random.nextInt(candidates.size)]
        return setMeal(plan, day, cat, PlannedMeal(pick.id, idealScaleFor(pick, kcalTargets.forCategory(cat))))
    }
}
