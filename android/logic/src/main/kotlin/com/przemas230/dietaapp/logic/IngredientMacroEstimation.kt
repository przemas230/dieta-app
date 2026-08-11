package com.przemas230.dietaapp.logic

/** A single line's estimated contribution -- kcal/protein/carbs/fat unrounded, canon for diagnostics. */
data class IngredientMacroEstimate(
    val canon: String,
    val kcal: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
)

/** Summed estimate across a whole ingredients textarea -- `matched`/`total` drive the "Rozpoznano X z Y składników" hint. */
data class RecipeMacroEstimate(
    val total: Int,
    val matched: Int,
    val kcal: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
)

/**
 * FR-66: one-to-one port of index.html's `estimateIngredientMacros`/
 * `estimateRecipeMacrosFromText` -- lets "Dodaj swój przepis" auto-fill
 * kcal/protein/carbs/fat from what the user actually typed instead of
 * requiring them to already know the dish's nutrition breakdown. Reuses
 * RecipePantryMatching.parseIngredient (same leading-quantity/unit parsing
 * as the pantry-matching feature) and IngredientCanon.pantryCanon (same
 * canonicalization as FR-4/16/25/33/34) -- no separate parsing logic to
 * keep in sync.
 */
object IngredientMacroEstimation {
    fun estimateIngredientMacros(text: String): IngredientMacroEstimate? {
        val raw = text.trim()
        if (raw.isEmpty()) return null
        // parseIngredient's canonName is already fully canonicalized (unlike
        // web's parseIngredient, which returns a pre-canon name requiring a
        // separate pantryCanon() call) -- same "use canonName directly, no
        // re-canonicalizing" convention every other caller in this codebase
        // already follows (e.g. RecipeListScreen.kt's ingredient "have it" check).
        val parsed = RecipePantryMatching.parseIngredient(raw)
        val canon = parsed.canonName
        val entry = IngredientMacroDb.TABLE[canon] ?: return null
        val scale = if (entry.per1Szt) {
            parsed.baseQty
        } else {
            val grams = if (parsed.unitCat == "weight" || parsed.unitCat == "volume") {
                parsed.baseQty
            } else {
                (entry.typicalG ?: 0.0) * parsed.baseQty
            }
            grams / 100.0
        }
        return IngredientMacroEstimate(
            canon = canon,
            kcal = entry.kcal * scale,
            protein = entry.protein * scale,
            carbs = entry.carbs * scale,
            fat = entry.fat * scale,
        )
    }

    fun estimateRecipeMacrosFromText(ingredientsText: String): RecipeMacroEstimate {
        val lines = ingredientsText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        var kcal = 0.0
        var protein = 0.0
        var carbs = 0.0
        var fat = 0.0
        var matched = 0
        lines.forEach { line ->
            val estimate = estimateIngredientMacros(line) ?: return@forEach
            matched++
            kcal += estimate.kcal
            protein += estimate.protein
            carbs += estimate.carbs
            fat += estimate.fat
        }
        return RecipeMacroEstimate(
            total = lines.size,
            matched = matched,
            kcal = Math.round(kcal).toInt(),
            protein = Math.round(protein * 10) / 10.0,
            carbs = Math.round(carbs * 10) / 10.0,
            fat = Math.round(fat * 10) / 10.0,
        )
    }
}
