package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.Recipe

/**
 * FR-28: builds the "every ingredient across every recipe" tile list the
 * Spiżarnia screen renders — a one-to-one port of index.html's
 * buildPantryTileList()/computeTileUnitCats(). This is purely a *render-time*
 * derived view; nothing here is persisted. `state.pantry` (Android:
 * PantryStore, via PantryViewModel) only ever holds entries the user has
 * actually tapped, exactly like the web app — an untapped tile is never
 * written to storage, it's recomputed fresh from the recipe database every
 * time the Spiżarnia screen composes.
 */
object PantryTiles {
    /** index.html's PANTRY_CAT_ORDER, plus "Inne" appended last. */
    val CATEGORY_ORDER: List<PantryCategory> = listOf(
        PantryCategory.NABIAL,
        PantryCategory.WARZYWA,
        PantryCategory.OWOCE,
        PantryCategory.MIESO,
        PantryCategory.STRACZKI,
        PantryCategory.ZBOZOWE,
        PantryCategory.PRZYPRAWY,
        PantryCategory.INNE,
    )

    /** Port of index.html's tileStep(unitCat): weight/volume step by 100 (g/ml), everything else by 1. */
    fun tileStep(unitCat: String): Double = if (unitCat == "weight" || unitCat == "volume") 100.0 else 1.0

    /** weight->"g", volume->"ml", count (and anything else in this simplified model)->"szt." -- matches RecipePantryMatching's UNIT_DEFS. */
    fun unitCatToUnit(unitCat: String): String = when (unitCat) {
        "weight" -> "g"
        "volume" -> "ml"
        else -> "szt."
    }

    /** Deduplicated, order-preserving set of every canonical ingredient name across every recipe -- port of buildPantryTileList(). */
    fun buildTileNames(recipes: List<Recipe>): Set<String> {
        val names = LinkedHashSet<String>()
        recipes.forEach { recipe ->
            recipe.ingredients.forEach { ingredient ->
                val raw = IngredientCanon.coreName(ingredient)
                if (raw.isNotEmpty()) names.add(IngredientCanon.pantryCanon(raw))
            }
        }
        return names
    }

    /**
     * Majority-vote default unit category per canonical ingredient, scanning
     * every recipe's ingredient lines through RecipePantryMatching.parseIngredient
     * -- port of index.html's computeTileUnitCats()/TILE_UNIT_CAT, scaled down
     * to this simplified model's 3 buckets (weight/volume/count) instead of
     * the web version's 11.
     */
    fun computeTileUnitCats(recipes: List<Recipe>): Map<String, String> {
        val votes = LinkedHashMap<String, MutableMap<String, Int>>()
        recipes.forEach { recipe ->
            recipe.ingredients.forEach { ingredient ->
                val parsed = RecipePantryMatching.parseIngredient(ingredient)
                val counts = votes.getOrPut(parsed.canonName) { mutableMapOf() }
                counts[parsed.unitCat] = (counts[parsed.unitCat] ?: 0) + 1
            }
        }
        return votes.mapValues { (_, counts) -> counts.entries.maxBy { it.value }.key }
    }

    /** CANON_INFO lookup with the same "Inne"/🍽️ fallback as index.html's `CANON_INFO[name] || {cat:"Inne", emoji:"🍽️"}`. */
    fun categoryAndEmoji(name: String): Pair<PantryCategory, String> {
        val info = IngredientCanon.CANON_INFO[name]
        val category = info?.let { PantryCategory.byLabel(it.cat) } ?: PantryCategory.INNE
        return category to (info?.emoji ?: "🍽️")
    }
}
