package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

/** Picks the MacroTargets field matching a Recipe.cat value ("sniadania"/"drugie"/"obiady"/"kolacje"/"deser"). */
fun MacroTargets.forCategory(cat: String): MacroGrams? = when (cat) {
    "sniadania" -> sniadania
    "drugie" -> drugie
    "obiady" -> obiady
    "kolacje" -> kolacje
    "deser" -> deser
    else -> null
}

/** Same lookup for the plain kcal targets, used by PlannerOperations.idealScaleFor. */
fun DailyCalorieTargets.forCategory(cat: String): Int? = when (cat) {
    "sniadania" -> sniadania
    "drugie" -> drugie
    "obiady" -> obiady
    "kolacje" -> kolacje
    "deser" -> deser
    else -> null
}

/**
 * FR-11: port of index.html's recipeMatchScore — 0-100, how well a recipe's
 * macros fit its meal-slot target, penalized for high glycemic load (unless
 * the user turned off "Trzymaj się niskiego indeksu glikemicznego", FR-9).
 * The GL penalty is relaxed for "budowanie masy" — see index.html's comment
 * on recipeMatchScore for why a carb-heavy, higher-GL meal is an accepted
 * tradeoff there but not during redukcja/utrzymanie.
 */
object RecipeMatching {
    fun matchScore(recipe: Recipe, target: MacroGrams?, profile: Profile): Int? {
        val protein = recipe.protein ?: return null
        val carbs = recipe.carbs ?: return null
        val fat = recipe.fat ?: return null
        if (target == null) return null

        val dp = abs(protein - target.protein) / max(target.protein.toDouble(), 1.0)
        val dc = abs(carbs - target.carbs) / max(target.carbs.toDouble(), 1.0)
        val df = abs(fat - target.fat) / max(target.fat.toDouble(), 1.0)
        var score = 100 - (dp + dc + df) / 3 * 100

        if (profile.strictLowGI) {
            val gl = recipe.gl ?: 0.0
            val glThreshold = if (profile.goal == Goal.BUDOWANIE) 35.0 else 20.0
            val glWeight = if (profile.goal == Goal.BUDOWANIE) 0.6 else 1.5
            if (gl > glThreshold) score -= (gl - glThreshold) * glWeight
        }

        return score.coerceIn(0.0, 100.0).roundToInt()
    }
}
