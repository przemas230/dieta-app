package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe

/**
 * FR-100 (ported to Android 2026-08-29): the "📊 Zaplanowany tydzień" card's
 * numbers — a one-to-one port of index.html's `computeWeekPlanSummary()`.
 *
 * Two deliberate choices carried over verbatim, because they are the whole
 * reason the figure is trustworthy:
 *
 * 1. Averages are taken over PLANNED days only, never over 7. A half-planned
 *    week would otherwise report an average far under target and read like a
 *    crash diet rather than an unfinished plan.
 * 2. Macros are summed only from recipes that actually carry them (custom
 *    recipes may not) and reported together with the count they came from,
 *    so a partial figure can never masquerade as a complete one.
 */
object WeekPlanSummary {
    data class Summary(
        val plannedDays: Int,
        val totalMeals: Int,
        val totalKcal: Int,
        val avgKcal: Int,
        /** How many of [totalMeals] actually carried macros -- shown so a partial average says so. */
        val macroMeals: Int,
        val avgProtein: Int?,
        val avgCarbs: Int?,
        val avgFat: Int?,
    )

    /** Null when nothing is planned at all -- the caller renders no card rather than a row of zeroes. */
    fun compute(weekPlan: WeekPlan, recipesById: Map<String, Recipe>): Summary? {
        var plannedDays = 0
        var totalKcal = 0
        var totalMeals = 0
        var macroMeals = 0
        var protein = 0
        var carbs = 0
        var fat = 0

        for (day in 0..6) {
            val dayMeals = weekPlan[day].orEmpty()
            var dayKcal = 0
            var dayHasMeal = false
            PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                val meal = dayMeals[category.id] ?: return@forEach
                val recipe = recipesById[meal.recipeId] ?: return@forEach
                dayHasMeal = true
                totalMeals++
                dayKcal += PlannerOperations.scaledKcal(recipe, meal.scale)
                val p = recipe.protein
                val c = recipe.carbs
                val f = recipe.fat
                if (p != null && c != null && f != null) {
                    macroMeals++
                    protein += Math.round(p * meal.scale).toInt()
                    carbs += Math.round(c * meal.scale).toInt()
                    fat += Math.round(f * meal.scale).toInt()
                }
            }
            if (dayHasMeal) {
                plannedDays++
                totalKcal += dayKcal
            }
        }

        if (plannedDays == 0) return null
        return Summary(
            plannedDays = plannedDays,
            totalMeals = totalMeals,
            totalKcal = totalKcal,
            avgKcal = Math.round(totalKcal.toDouble() / plannedDays).toInt(),
            macroMeals = macroMeals,
            avgProtein = if (macroMeals > 0) Math.round(protein.toDouble() / plannedDays).toInt() else null,
            avgCarbs = if (macroMeals > 0) Math.round(carbs.toDouble() / plannedDays).toInt() else null,
            avgFat = if (macroMeals > 0) Math.round(fat.toDouble() / plannedDays).toInt() else null,
        )
    }

    /**
     * FR-110: how much of the plan actually happened -- "8 z 15 posiłków" for
     * the part of the week that is already behind us.
     *
     * The card above it says what the week was SUPPOSED to look like; nothing
     * anywhere said whether it did. Two judgement calls make the number worth
     * showing at all:
     *
     * 1. **Only days up to and including today count.** Measuring against the
     *    whole week would report a 13% "realizacja" every Monday evening for
     *    a plan being followed perfectly -- a number that punishes the user
     *    for the calendar rather than for anything they did.
     * 2. **Only PLANNED slots count.** Eating something unplanned is not a
     *    failure to follow the plan, and counting it would let a day of
     *    snacks read as a day of adherence.
     */
    data class Realization(val eatenMeals: Int, val plannedSoFar: Int) {
        val percent: Int get() = if (plannedSoFar == 0) 0 else Math.round(eatenMeals * 100.0 / plannedSoFar).toInt()
    }

    /**
     * @param isEaten asked once per planned slot up to [todayDayIndex] -- taken
     *   as a lambda because "eaten" is stored per calendar DATE on both
     *   platforms while the plan is a weekday template, and only the caller
     *   knows how its week maps onto real dates.
     * @return null when nothing was planned for the part of the week that has
     *   already happened -- the caller shows no row rather than "0 z 0".
     */
    fun realization(
        weekPlan: WeekPlan,
        todayDayIndex: Int,
        isEaten: (dayIndex: Int, categoryId: String) -> Boolean,
    ): Realization? {
        var planned = 0
        var eaten = 0
        for (day in 0..todayDayIndex.coerceIn(0, 6)) {
            val dayMeals = weekPlan[day].orEmpty()
            PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                dayMeals[category.id] ?: return@forEach
                planned++
                if (isEaten(day, category.id)) eaten++
            }
        }
        if (planned == 0) return null
        return Realization(eaten, planned)
    }

    /**
     * How the average compares to the daily target. ±50 kcal counts as "on
     * target" -- the same tolerance index.html uses, chosen so normal
     * rounding across five meals doesn't read as missing the goal.
     */
    fun targetComparison(avgKcal: Int, targetKcal: Int): Pair<String, Boolean> {
        val diff = avgKcal - targetKcal
        if (kotlin.math.abs(diff) <= 50) return "w celu ($targetKcal kcal)" to true
        val sign = if (diff > 0) "+" else "−"
        return "$sign${kotlin.math.abs(diff)} kcal vs cel $targetKcal" to false
    }
}
