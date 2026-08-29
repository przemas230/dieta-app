package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Recipe

/**
 * FR-15: matches recipe ingredient text against pantry `Product` entries so
 * marking a dish "done" can decrement matching stock (and undo it if the
 * entry is later deleted). Port of index.html's parseIngredient /
 * subtractRecipeFromPantry / restoreRecipeToPantry, scaled down to the
 * weight/volume/count buckets Android's free-text pantry unit field can
 * actually express -- PantryItem.kt already notes the pantry model here is
 * a simplified stand-in for the web app's full unit system.
 */
object RecipePantryMatching {
    private data class UnitInfo(val cat: String, val factor: Double)

    private val UNIT_DEFS: Map<String, UnitInfo> = mapOf(
        "kg" to UnitInfo("weight", 1000.0),
        "g" to UnitInfo("weight", 1.0),
        "l" to UnitInfo("volume", 1000.0),
        "ml" to UnitInfo("volume", 1.0),
        "szt" to UnitInfo("count", 1.0),
        "szt." to UnitInfo("count", 1.0),
    )

    private val QTY_REGEX = Regex("""^(\d+/\d+|\d+[.,]\d+|\d+)\s*""")

    data class ParsedIngredient(val canonName: String, val baseQty: Double, val unitCat: String)

    /** Leading quantity + known unit word, then IngredientCanon name resolution -- mirrors index.html's parseIngredient(). */
    fun parseIngredient(text: String): ParsedIngredient {
        val trimmed = text.trim()
        val match = QTY_REGEX.find(trimmed)
        var qty = 1.0
        var rest = trimmed
        if (match != null) {
            val raw = match.groupValues[1]
            qty = if (raw.contains('/')) {
                val (num, den) = raw.split('/')
                num.toDouble() / den.toDouble()
            } else {
                raw.replace(',', '.').toDoubleOrNull() ?: 1.0
            }
            rest = trimmed.substring(match.value.length).trim()
        }
        val tokens = rest.lowercase().split(Regex("""\s+""")).filter { it.isNotEmpty() }
        val unitInfo = UNIT_DEFS[tokens.firstOrNull() ?: ""]
        val unitCat = unitInfo?.cat ?: "count"
        val factor = unitInfo?.factor ?: 1.0
        val nameSource = if (unitInfo != null) tokens.drop(1).joinToString(" ") else rest
        val baseQty = qty * factor
        val canonName = IngredientCanon.pantryCanon(
            IngredientCanon.coreName(nameSource).ifBlank { IngredientCanon.coreName(trimmed) }
                .ifBlank { trimmed.lowercase() },
        )
        return ParsedIngredient(canonName, baseQty, unitCat)
    }

    /** weight/volume/count for a concrete pantry unit string ("g", "l", "szt.", ...) -- also used by PantryScreen's badge formatting. */
    fun pantryUnitCat(unit: String): String = UNIT_DEFS[unit.trim().lowercase()]?.cat ?: "count"
    private fun pantryUnitFactor(unit: String): Double = UNIT_DEFS[unit.trim().lowercase()]?.factor ?: 1.0

    private fun applyDelta(
        items: Map<String, PantryItem>,
        recipe: Recipe,
        sign: Int,
    ): Map<String, PantryItem> {
        var result = items
        recipe.ingredients.forEach { ingredient ->
            val parsed = parseIngredient(ingredient)
            val entry = result[parsed.canonName] as? PantryItem.Product ?: return@forEach
            if (pantryUnitCat(entry.unit) != parsed.unitCat) return@forEach
            val deltaInPantryUnit = parsed.baseQty / pantryUnitFactor(entry.unit)
            val newQty = maxOf(0.0, entry.quantity + sign * deltaInPantryUnit)
            result = result + (parsed.canonName to entry.copy(quantity = Math.round(newQty * 100) / 100.0))
        }
        return result
    }

    /** Subtracts matching ingredients, floored at 0 -- entries are kept (not removed) at zero, like index.html. */
    fun subtractForRecipe(items: Map<String, PantryItem>, recipe: Recipe): Map<String, PantryItem> =
        applyDelta(items, recipe, sign = -1)

    /**
     * FR-106: puts a whole recipe's worth of ingredients INTO the pantry --
     * what "I have just bought all of this" means.
     *
     * Deliberately not [restoreForRecipe]. That one runs through [applyDelta],
     * which skips any ingredient the pantry does not already track (`?:
     * return@forEach`) because it exists to reverse a subtraction, and you
     * cannot un-subtract from something that was never there. Stocking up
     * after shopping is the opposite situation: the ingredients worth adding
     * are exactly the ones you did NOT have, so they have to be created.
     *
     * An existing entry whose unit category disagrees with the recipe's
     * (pantry in "szt.", recipe in grams) is left alone rather than guessed
     * at -- same cautious rule the rest of this file applies when the two
     * units cannot be safely reconciled.
     */
    fun stockFromRecipe(items: Map<String, PantryItem>, recipe: Recipe): Map<String, PantryItem> {
        var result = items
        recipe.ingredients.forEach { ingredient ->
            val parsed = parseIngredient(ingredient)
            if (parsed.baseQty <= 0.0) return@forEach
            val existing = result[parsed.canonName] as? PantryItem.Product
            if (existing == null) {
                val unit = PantryTiles.unitCatToUnit(parsed.unitCat)
                val category = PantryTiles.categoryAndEmoji(parsed.canonName).first
                result = result + (
                    parsed.canonName to PantryItem.Product(
                        parsed.canonName,
                        category,
                        Math.round(parsed.baseQty * 100) / 100.0,
                        unit,
                    )
                )
            } else if (pantryUnitCat(existing.unit) == parsed.unitCat) {
                val added = existing.quantity + parsed.baseQty / pantryUnitFactor(existing.unit)
                result = result + (parsed.canonName to existing.copy(quantity = Math.round(added * 100) / 100.0))
            }
        }
        return result
    }

    /** Reverses subtractForRecipe, e.g. when a cook-history entry is deleted. */
    fun restoreForRecipe(items: Map<String, PantryItem>, recipe: Recipe): Map<String, PantryItem> =
        applyDelta(items, recipe, sign = 1)

    /**
     * FR-75: how much of a shopping-list quantity is still missing once
     * matching pantry stock is subtracted -- null means fully covered
     * ("✓" tile badge). A missing/mismatched-unit-category pantry entry is
     * treated as zero coverage (the full shopping quantity still shows as
     * missing) -- same cautious fallback as the FR-16 "🏺 masz" pantry check,
     * since e.g. a pantry item tracked in "szt." can't safely cover a
     * shopping-list need expressed in grams.
     */
    fun missingAfterPantry(shoppingQty: Double, shoppingUnitCat: String, pantryEntry: PantryItem.Product?): Double? {
        if (pantryEntry == null || pantryUnitCat(pantryEntry.unit) != shoppingUnitCat) return shoppingQty
        val pantryQtyInBaseUnits = pantryEntry.quantity * pantryUnitFactor(pantryEntry.unit)
        val missing = shoppingQty - pantryQtyInBaseUnits
        return if (missing <= 0.0001) null else missing
    }

    /**
     * FR-2 (🏺 "dania z tego, co mam w spiżarni" filter): fraction of a
     * recipe's ingredients that already have a pantry entry -- port of
     * index.html's `pantryCoverageRatio`/`pantryMatch`. Same presence-only
     * check (`containsKey`, ignoring product quantity) already used
     * elsewhere in this app for the "have it" ingredient highlight
     * (RecipeListScreen's `haveIt`) -- intentionally not re-adding web's
     * qty<=0 exclusion here, for consistency with that existing convention.
     */
    fun pantryCoverageRatio(recipe: Recipe, pantryItems: Map<String, PantryItem>): Double {
        if (recipe.ingredients.isEmpty()) return 0.0
        val have = recipe.ingredients.count { ingredient -> parseIngredient(ingredient).canonName in pantryItems }
        return have.toDouble() / recipe.ingredients.size
    }

    /**
     * 2026-08-11 (compact search dropdown, user request): every distinct
     * canon ingredient name that appears in at least one of [recipes],
     * alphabetically sorted (Polish collation) -- feeds the "🔍" search
     * dropdown's ingredient picker so tapping an entry there searches for
     * exactly the same canon name the recipe's own ingredient line would
     * parse to (same [parseIngredient] this file already uses for pantry
     * matching), not raw, differently-worded ingredient text.
     */
    fun uniqueIngredientNames(recipes: List<Recipe>): List<String> =
        recipes.asSequence()
            .flatMap { it.ingredients.asSequence() }
            .map { parseIngredient(it).canonName }
            .filter { it.isNotBlank() }
            .distinct()
            .sortedWith(java.text.Collator.getInstance(java.util.Locale("pl", "PL")))
            .toList()
}
