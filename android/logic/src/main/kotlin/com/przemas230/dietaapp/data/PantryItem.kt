package com.przemas230.dietaapp.data

/**
 * Mirrors the shape of state.pantry entries in the web app's index.html:
 * a "product" has a quantity + unit, a "spice" has a coarse Mało/Wystarczy/
 * Dużo level instead of an exact quantity. Category labels are a one-to-one
 * port of index.html's PANTRY_CAT_ORDER (+ "Inne") — the same 8 labels
 * IngredientCanon.CANON_INFO's `cat` field already uses, see PantryCategory.byLabel.
 *
 * Lives in the plain :logic module (no Android dependency) so the pantry
 * mutation logic can be unit-tested — see PantryOperationsTest.
 */
enum class PantryCategory(val label: String) {
    NABIAL("Nabiał"),
    WARZYWA("Warzywa"),
    OWOCE("Owoce"),
    MIESO("Mięso, ryby, jajka"),
    STRACZKI("Strączki i orzechy"),
    ZBOZOWE("Pieczywo i zboża"),
    PRZYPRAWY("Przyprawy"),
    INNE("Inne");

    companion object {
        /** IngredientCanon.CANON_INFO's `cat` strings map onto these labels directly -- unmapped/unknown falls back to INNE, same as index.html's `{cat:"Inne", emoji:"🍽️"}` fallback. */
        fun byLabel(label: String): PantryCategory = entries.find { it.label == label } ?: INNE
    }
}

sealed class PantryItem {
    abstract val name: String
    abstract val category: PantryCategory

    data class Product(
        override val name: String,
        override val category: PantryCategory,
        val quantity: Double,
        val unit: String,
        // On explicit user request ("dodaj opcje zmieniania skoku po
        // przytrzymaniu kafelka") -- overrides PantryTiles.tileStep(unitCat)
        // for this specific product's +/- tap increment. Kept on the item
        // itself (not a separate persisted map like index.html's
        // pantryStepOverride) as a deliberate simplification -- resets if
        // the item is fully untracked and re-added, same trade-off already
        // made for unit overrides on Android (see android/PARITY.md's FR-30
        // note: unit-override-via-long-press wasn't ported at all here).
        val stepOverride: Double? = null,
    ) : PantryItem()

    data class Spice(
        override val name: String,
        override val category: PantryCategory,
        val level: SpiceLevel,
    ) : PantryItem()
}

/**
 * Port of index.html's SPICE_LEVELS -- exactly 3 clamped levels, no "Brak"/
 * absent level: an untracked spice simply has no PantryItem.Spice entry at
 * all (see PantryOperations.tileTapDelta), it doesn't sit at some "Brak"
 * step within a wrapping cycle.
 */
enum class SpiceLevel(val label: String) {
    MALO("Mało"),
    WYSTARCZY("Wystarczy"),
    DUZO("Dużo"),
}
