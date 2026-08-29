package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Recipe

/**
 * FR-108: "tego ci nie starczy" — which tracked pantry products fall short of
 * what the dishes still planned for this week need.
 *
 * Until now a shortage only became visible at the stove: the Planer card's
 * "🏺 N/M w spiżarni" badge (FR-16) answers *presence* ("is there any
 * chicken at all"), never *amount*, so 20 g of rice and 2 kg of rice look
 * exactly alike right up to the moment the dish is cooked and FR-15's
 * subtraction floors at zero.
 *
 * Three deliberate limits, each of them about not crying wolf:
 *
 * 1. **Only tracked products count.** An ingredient with no pantry entry is
 *    not a shortage — it's simply something the user does not track, and
 *    there are ~200 such tiles. Flagging those would bury the handful of
 *    entries the user actually maintains under noise the shopping list
 *    already handles.
 * 2. **Only meals still ahead.** Days before today are done with, and a
 *    meal already marked "🍳 zrobione" on its day has ALREADY had its
 *    ingredients subtracted by FR-15/FR-103 — counting it again would
 *    invent a shortage out of a dish that is finished and eaten.
 * 3. **Mismatched unit categories are skipped, never guessed.** A product
 *    tracked in "szt." cannot be compared against a recipe asking for grams
 *    — the same cautious rule [RecipePantryMatching.missingAfterPantry] and
 *    the FR-16 badge already follow.
 *
 * Spices are excluded outright: their Mało/Wystarczy/Dużo level is not a
 * quantity, so there is nothing to subtract from.
 */
object PantryShortage {
    /**
     * [haveBase]/[neededBase] are in the ingredient's BASE units (g, ml or
     * pieces) so callers can format them with the same
     * [ShoppingDisplay.formatQty]/`formatQty()` the rest of both apps uses.
     */
    data class Shortage(
        val canonName: String,
        val unitCat: String,
        val haveBase: Double,
        val neededBase: Double,
        /** Distinct dish names driving the need, in plan order — the answer to "na co mi tego brakuje". */
        val dishes: List<String>,
    ) {
        val missingBase: Double get() = (neededBase - haveBase).coerceAtLeast(0.0)

        /** 1.0 = nothing of what's needed is in stock, 0.1 = a tenth of it is missing. Drives the ordering. */
        val missingRatio: Double get() = if (neededBase <= 0.0) 0.0 else missingBase / neededBase
    }

    private data class Need(var qty: Double, val unitCat: String, val dishes: MutableList<String>)

    /**
     * @param todayDayIndex 0=Poniedziałek..6=Niedziela; days before it are not
     *   looked at. Pass 0 to weigh the whole week (the Planer's own week view
     *   starts on Monday regardless of the current day).
     * @param isCookedOn asked once per planned slot — true means FR-15 already
     *   took these ingredients out of the pantry, so they must not be counted
     *   as still needed. Passed as a lambda rather than a cook-history map so
     *   this stays independent of how each platform stores that history.
     */
    fun compute(
        weekPlan: WeekPlan,
        recipesById: Map<String, Recipe>,
        pantry: Map<String, PantryItem>,
        todayDayIndex: Int,
        isCookedOn: (String, Int) -> Boolean = { _, _ -> false },
    ): List<Shortage> {
        val needs = LinkedHashMap<String, Need>()
        for (day in todayDayIndex.coerceAtLeast(0)..6) {
            val dayMeals = weekPlan[day].orEmpty()
            PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                val meal = dayMeals[category.id] ?: return@forEach
                val recipe = recipesById[meal.recipeId] ?: return@forEach
                if (isCookedOn(recipe.id, day)) return@forEach
                recipe.ingredients.forEach ingredient@{ ingredient ->
                    val parsed = RecipePantryMatching.parseIngredient(ingredient)
                    if (parsed.baseQty <= 0.0) return@ingredient
                    val need = needs.getOrPut(parsed.canonName) {
                        Need(0.0, parsed.unitCat, mutableListOf())
                    }
                    // Two recipes wording the same ingredient in different
                    // units (one "2 szt. cytryny", one "50 ml soku") can't be
                    // added up; the first wording wins and the other is left
                    // out rather than silently summed into a wrong total.
                    if (need.unitCat != parsed.unitCat) return@ingredient
                    need.qty += parsed.baseQty * meal.scale
                    if (recipe.name !in need.dishes) need.dishes += recipe.name
                }
            }
        }

        val result = mutableListOf<Shortage>()
        needs.forEach { (canonName, need) ->
            val entry = pantry[canonName] as? PantryItem.Product ?: return@forEach
            if (RecipePantryMatching.pantryUnitCat(entry.unit) != need.unitCat) return@forEach
            val haveBase = entry.quantity * unitFactor(entry.unit)
            // A hair of tolerance so floating-point remainders on an exactly
            // sufficient stock ("500 g have, 500 g needed") don't report a
            // shortage of 0.0000001 g.
            if (haveBase + 0.0001 >= need.qty) return@forEach
            result += Shortage(canonName, need.unitCat, haveBase, need.qty, need.dishes.toList())
        }

        // Worst relative shortfall first: "nothing left, and three dishes need
        // it" matters more than "9 of the 10 eggs are there". Ties fall back
        // to the absolute gap, then to Polish alphabetical order so the list
        // is stable between renders.
        val collator = java.text.Collator.getInstance(java.util.Locale("pl", "PL"))
        return result.sortedWith(
            compareByDescending<Shortage> { it.missingRatio }
                .thenByDescending { it.missingBase }
                .thenBy(collator) { it.canonName },
        )
    }

    private fun unitFactor(unit: String): Double = when (unit.trim().lowercase()) {
        "kg" -> 1000.0
        "l" -> 1000.0
        else -> 1.0
    }

    /** "na 3 dania" / "na 1 danie" — Polish counts, so it can't be a plain "na N dań". */
    fun dishCountLabel(count: Int): String = when {
        count == 1 -> "na 1 danie"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "na $count dania"
        else -> "na $count dań"
    }
}
