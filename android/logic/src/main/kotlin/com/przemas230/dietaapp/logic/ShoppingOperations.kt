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
     * FR-27 / the Planer's own per-day add button / FR-58's shopping-day-strip
     * card: adds every planned meal's (scaled, FR-20) ingredients, skipping
     * any recipe id already on the list -- same "if(state.recipeAdded[r.id])
     * return" dedup index.html's day/week-add loops use, so a dish planned
     * twice in the same day/week is still only ever added once. Also reports
     * how many recipes were newly added vs. already present, for callers that
     * need to tell the user what happened (index.html's addDayToShoppingList
     * toast).
     */
    fun addDayPlanWithSummary(
        items: Map<String, ShoppingItem>,
        dayMeals: Map<String, PlannedMeal>,
        recipesById: Map<String, Recipe>,
    ): DayPlanAddResult {
        var result = items
        var added = 0
        var already = 0
        dayMeals.values.forEach { meal ->
            val recipe = recipesById[meal.recipeId] ?: return@forEach
            if (isRecipeAdded(result, recipe.id)) {
                already++
                return@forEach
            }
            val scaled = recipe.copy(ingredients = PlannerOperations.scaleIngredients(recipe.ingredients, meal.scale))
            result = addRecipe(result, scaled)
            added++
        }
        return DayPlanAddResult(result, added, already)
    }

    fun addDayPlan(
        items: Map<String, ShoppingItem>,
        dayMeals: Map<String, PlannedMeal>,
        recipesById: Map<String, Recipe>,
    ): Map<String, ShoppingItem> = addDayPlanWithSummary(items, dayMeals, recipesById).items

    /** FR-27: "add the whole week's ingredients" button on the Zakupy tab. */
    fun addWeekPlan(
        items: Map<String, ShoppingItem>,
        weekPlan: WeekPlan,
        recipesById: Map<String, Recipe>,
    ): Map<String, ShoppingItem> {
        var result = items
        weekPlan.keys.sorted().forEach { day ->
            result = addDayPlan(result, weekPlan[day].orEmpty(), recipesById)
        }
        return result
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
     * FR-26: plain-text summary for Android's share sheet -- port of
     * index.html's buildListText(), grouped by category and sorted by name
     * within each group, unchecked items only. Reuses Android's own 8-
     * category IngredientCanon/PantryTiles grouping (already used by the
     * Spiżarnia tile grid) rather than index.html's separate keyword-based
     * `classify()`, which exists only for this one text — same information,
     * one fewer categorization scheme to maintain.
     */
    fun buildShareText(items: Map<String, ShoppingItem>): String {
        val unchecked = items.values.filter { !it.checked }
        if (unchecked.isEmpty()) return "Lista zakupów jest pusta \uD83C\uDF89"
        val byCategoryLabel = unchecked.groupBy { IngredientCanon.CANON_INFO[it.name]?.cat ?: PantryCategory.INNE.label }
        val sb = StringBuilder("\uD83D\uDED2 Lista zakupów:\n")
        PantryTiles.CATEGORY_ORDER.forEach { cat ->
            val group = byCategoryLabel[cat.label] ?: return@forEach
            sb.append('\n').append(cat.label).append(":\n")
            group.sortedBy { it.name }.forEach { item ->
                val displayName = ShoppingDisplay.displayName(item.name, item.unitCat, item.quantity)
                sb.append("- ").append(ShoppingDisplay.formatQty(item.unitCat, item.quantity)).append(' ').append(displayName).append('\n')
            }
        }
        return sb.toString().trim()
    }
}
