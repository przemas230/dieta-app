package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.ShoppingItem

/**
 * FR-25: pure port of index.html's addRecipeToShoppingList /
 * removeRecipeFromShoppingList / addSingleIngredientToShopping. Every
 * ingredient is parsed (RecipePantryMatching.parseIngredient) down to a
 * canonical name + unitCat key ("canon|unitCat", matching index.html's own
 * key format) so the same ingredient from two different recipes accumulates
 * into one entry instead of two, and removing one recipe only subtracts its
 * own `contributions` share rather than deleting the whole entry.
 */
object ShoppingOperations {
    fun keyFor(canonName: String, unitCat: String): String = "$canonName|$unitCat"

    /**
     * FR-106: recipe ids whose EVERY shopping-list item is now ticked off.
     *
     * "Ticked off" means bought, so this is the moment the app knows the user
     * has everything for that dish -- and the moment worth offering to move it
     * into the pantry. Derived from `contributions`, which already records
     * which recipe put each item on the list, so nothing new has to be stored.
     *
     * A recipe with no items left on the list is NOT reported: removing the
     * last item is not the same as buying it, and offering to stock the pantry
     * then would be inventing a purchase that never happened.
     */
    fun fullyBoughtRecipes(items: Map<String, ShoppingItem>): Set<String> {
        val total = mutableMapOf<String, Int>()
        val checked = mutableMapOf<String, Int>()
        items.values.forEach { item ->
            item.contributions.keys.forEach { source ->
                total[source] = (total[source] ?: 0) + 1
                if (item.checked) checked[source] = (checked[source] ?: 0) + 1
            }
        }
        return total.filter { (source, count) -> count > 0 && checked[source] == count }.keys
    }

    /** Shared by addRecipe/addSingleIngredient -- `sourceKey` is the recipe id, or a synthetic "single:<recipeId>:<canon>" key for a lone-ingredient add. */
    private fun addContribution(
        items: Map<String, ShoppingItem>,
        ingredients: List<String>,
        sourceKey: String,
    ): Map<String, ShoppingItem> {
        var result = items
        ingredients.forEach { ingredient ->
            val parsed = RecipePantryMatching.parseIngredient(ingredient)
            val key = keyFor(parsed.canonName, parsed.unitCat)
            val entry = result[key] ?: ShoppingItem(parsed.canonName, parsed.unitCat, 0.0)
            val newContribution = (entry.contributions[sourceKey] ?: 0.0) + parsed.baseQty
            result = result + (
                key to entry.copy(
                    quantity = entry.quantity + parsed.baseQty,
                    contributions = entry.contributions + (sourceKey to newContribution),
                )
            )
        }
        return result
    }

    private fun removeContribution(
        items: Map<String, ShoppingItem>,
        ingredients: List<String>,
        sourceKey: String,
    ): Map<String, ShoppingItem> {
        var result = items
        ingredients.forEach { ingredient ->
            val parsed = RecipePantryMatching.parseIngredient(ingredient)
            val key = keyFor(parsed.canonName, parsed.unitCat)
            val entry = result[key] ?: return@forEach
            val contribution = entry.contributions[sourceKey] ?: return@forEach
            val newQty = entry.quantity - contribution
            val newContributions = entry.contributions - sourceKey
            result = if (newQty <= 0.0001 || newContributions.isEmpty()) {
                result - key
            } else {
                result + (key to entry.copy(quantity = newQty, contributions = newContributions))
            }
        }
        return result
    }

    /** FR-25: "Dodaj do listy zakupów" on a recipe card -- adds every ingredient, keyed by the recipe's own id. */
    fun addRecipe(items: Map<String, ShoppingItem>, recipe: Recipe): Map<String, ShoppingItem> =
        addContribution(items, recipe.ingredients, recipe.id)

    /** Reverses addRecipe -- only this recipe's share is subtracted, so an item another recipe also needs survives. */
    fun removeRecipe(items: Map<String, ShoppingItem>, recipe: Recipe): Map<String, ShoppingItem> =
        removeContribution(items, recipe.ingredients, recipe.id)

    /** FR-16's "🛒" per-ingredient add -- same accumulation, keyed by a synthetic source (not the recipe id) so it doesn't collide with a later whole-recipe add/remove. */
    fun addSingleIngredient(items: Map<String, ShoppingItem>, ingredientText: String, sourceKey: String): Map<String, ShoppingItem> =
        addContribution(items, listOf(ingredientText), sourceKey)

    /** Whether any shopping entry still carries a contribution from this recipe -- drives the "✓ Na liście zakupów" toggle state. */
    fun isRecipeAdded(items: Map<String, ShoppingItem>, recipeId: String): Boolean =
        items.values.any { it.contributions.containsKey(recipeId) }

    /** Result of [addDayPlanWithSummary] -- counts drive the FR-58 day-strip's toast message. */
    data class DayPlanAddResult(val items: Map<String, ShoppingItem>, val added: Int, val already: Int)

    /**
     * Shared by [addDayPlanWithSummary] and [addWeekPlan] -- adds every given
     * planned-meal OCCURRENCE's (scaled, FR-20) ingredients, skipping only
     * recipes that were already on the list BEFORE this call started
     * (`alreadyOnListBefore`, snapshotted once up front). **2026-08-11 fix**:
     * this used to re-check the live, mutating `items` map after each
     * addition (`isRecipeAdded(result, recipe.id)`), so if the SAME recipe
     * appeared more than once among the given occurrences (e.g. planned for
     * both Tuesday and Friday), only the FIRST occurrence's ingredients were
     * ever added -- a real bug the user caught ("jak masz banana na liście
     * we wtorek i w piątek to musisz wziąć pod uwagę że... potrzebne są dwa
     * banany"). Snapshotting the "already on the list" set ONCE before the
     * loop means every occurrence in THIS call contributes independently
     * (so two plannings of the same recipe correctly double its ingredient
     * quantities), while a recipe genuinely already on the list from an
     * EARLIER, separate call is still skipped -- re-running "Dodaj tydzień"
     * twice in a row stays idempotent.
     */
    private fun addOccurrences(
        items: Map<String, ShoppingItem>,
        occurrences: List<PlannedMeal>,
        recipesById: Map<String, Recipe>,
    ): DayPlanAddResult {
        val alreadyOnListBefore = items.values.flatMap { it.contributions.keys }.toSet()
        var result = items
        var added = 0
        var already = 0
        occurrences.forEach { meal ->
            val recipe = recipesById[meal.recipeId] ?: return@forEach
            if (recipe.id in alreadyOnListBefore) {
                already++
                return@forEach
            }
            val scaled = recipe.copy(ingredients = PlannerOperations.scaleIngredients(recipe.ingredients, meal.scale))
            result = addRecipe(result, scaled)
            added++
        }
        return DayPlanAddResult(result, added, already)
    }

    /** FR-27 / the Planer's own per-day add button / FR-58's shopping-day-strip card. */
    fun addDayPlanWithSummary(
        items: Map<String, ShoppingItem>,
        dayMeals: Map<String, PlannedMeal>,
        recipesById: Map<String, Recipe>,
    ): DayPlanAddResult = addOccurrences(items, dayMeals.values.toList(), recipesById)

    fun addDayPlan(
        items: Map<String, ShoppingItem>,
        dayMeals: Map<String, PlannedMeal>,
        recipesById: Map<String, Recipe>,
    ): Map<String, ShoppingItem> = addDayPlanWithSummary(items, dayMeals, recipesById).items

    /**
     * FR-27: "add the whole week's ingredients" button on the Zakupy tab.
     * Flattens every day's planned meals into one occurrence list BEFORE
     * calling [addOccurrences], so the "already on the list" snapshot covers
     * the WHOLE week at once -- calling [addDayPlan] once per day in a loop
     * would instead re-derive that snapshot after each day, making a recipe
     * planned on day 1 look "already added" by the time day 3 is processed,
     * silently dropping day 3's contribution (the same class of bug as the
     * fix in [addOccurrences]'s doc comment, just one level up).
     */
    fun addWeekPlan(
        items: Map<String, ShoppingItem>,
        weekPlan: WeekPlan,
        recipesById: Map<String, Recipe>,
    ): Map<String, ShoppingItem> {
        val occurrences = weekPlan.keys.sorted().flatMap { day -> weekPlan[day].orEmpty().values }
        return addOccurrences(items, occurrences, recipesById).items
    }

    fun toggleChecked(items: Map<String, ShoppingItem>, key: String): Map<String, ShoppingItem> {
        val item = items[key] ?: return items
        return items + (key to item.copy(checked = !item.checked))
    }

    fun removeItem(items: Map<String, ShoppingItem>, key: String): Map<String, ShoppingItem> = items - key

    fun clearChecked(items: Map<String, ShoppingItem>): Map<String, ShoppingItem> = items.filterValues { !it.checked }

    /** FR-26: "Wyczyść całą listę" -- unlike clearChecked, drops EVERY item regardless of checked state. index.html also resets a separate `recipeAdded` map, but Android's isRecipeAdded is derived straight from `items`, so clearing items alone already un-marks every recipe. */
    fun clearAll(items: Map<String, ShoppingItem>): Map<String, ShoppingItem> = emptyMap()

    /**
     * 2026-08-11: which planner day(s) actually need a given shopping list
     * item, so the user can shop for just "today + jutro" and stop instead
     * of buying everything on the list at once. Purely DERIVED from the
     * current week plan + which recipes are actually on the list
     * (isRecipeAdded) -- never stored, recomputed fresh, so it can never
     * drift out of sync. An item added straight from a recipe card (not via
     * a planned day) simply gets no day in the result -- there's no day to
     * report. Port of index.html's computeIngredientDays() (2026-08-11).
     */
    fun computeIngredientDays(
        items: Map<String, ShoppingItem>,
        weekPlan: WeekPlan,
        recipesById: Map<String, Recipe>,
    ): Map<String, Set<Int>> {
        val result = mutableMapOf<String, MutableSet<Int>>()
        weekPlan.forEach { (day, dayMeals) ->
            dayMeals.values.forEach { meal ->
                if (!isRecipeAdded(items, meal.recipeId)) return@forEach
                val recipe = recipesById[meal.recipeId] ?: return@forEach
                val scaledIngredients = PlannerOperations.scaleIngredients(recipe.ingredients, meal.scale)
                scaledIngredients.forEach { ingredient ->
                    val parsed = RecipePantryMatching.parseIngredient(ingredient)
                    val key = keyFor(parsed.canonName, parsed.unitCat)
                    if (!items.containsKey(key)) return@forEach
                    result.getOrPut(key) { mutableSetOf() }.add(day)
                }
            }
        }
        return result
    }

    private const val SUNDAY_DAY_INDEX = 6

    /** "(dziś, jutro)"-style suffix for a shopping item's day label -- [todayIdx] is ShoppingDayStrip.todayIndex's result. Calls out Sunday specifically since stores are closed then. */
    fun formatIngredientDays(days: Set<Int>?, todayIdx: Int): String {
        if (days.isNullOrEmpty()) return ""
        val labels = days.sorted().map { day ->
            when (day) {
                todayIdx -> "dziś"
                (todayIdx + 1) % 7 -> "jutro"
                (todayIdx + 2) % 7 -> "pojutrze"
                else -> PlannerOperations.DAYS_PL[day].take(3).lowercase()
            }
        }
        val sundayNote = if (SUNDAY_DAY_INDEX in days) " — sklepy nieczynne, kup wcześniej" else ""
        return " (${labels.joinToString(", ")}$sundayNote)"
    }

    /**
     * FR-26: plain-text summary for Android's share sheet -- port of
     * index.html's buildListText(), grouped by category and sorted by name
     * within each group, unchecked items only. Reuses Android's own 8-
     * category IngredientCanon/PantryTiles grouping (already used by the
     * Spiżarnia tile grid) rather than index.html's separate keyword-based
     * `classify()`, which exists only for this one text — same information,
     * one fewer categorization scheme to maintain.
     */
    fun buildShareText(
        items: Map<String, ShoppingItem>,
        ingredientDays: Map<String, Set<Int>> = emptyMap(),
        todayIdx: Int = -1,
    ): String {
        val unchecked = items.values.filter { !it.checked }
        if (unchecked.isEmpty()) return "Lista zakupów jest pusta \uD83C\uDF89"
        val byCategoryLabel = unchecked.groupBy { IngredientCanon.CANON_INFO[it.name]?.cat ?: PantryCategory.INNE.label }
        val sb = StringBuilder("\uD83D\uDED2 Lista zakupów:\n")
        PantryTiles.CATEGORY_ORDER.forEach { cat ->
            val group = byCategoryLabel[cat.label] ?: return@forEach
            sb.append('\n').append(cat.label).append(":\n")
            group.sortedBy { it.name }.forEach { item ->
                val displayName = ShoppingDisplay.displayName(item.name, item.unitCat, item.quantity)
                val dayLabel = formatIngredientDays(ingredientDays[keyFor(item.name, item.unitCat)], todayIdx)
                sb.append("- ").append(ShoppingDisplay.formatQty(item.unitCat, item.quantity)).append(' ').append(displayName).append(dayLabel).append('\n')
            }
        }
        return sb.toString().trim()
    }
}
