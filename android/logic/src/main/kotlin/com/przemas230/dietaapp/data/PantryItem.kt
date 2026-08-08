package com.przemas230.dietaapp.data

/**
 * Mirrors the shape of state.pantry entries in the web app's index.html:
 * a "product" has a quantity + unit category, a "spice" has a coarse
 * Brak/Mało/Wystarczy level instead of an exact quantity. Category list
 * here is a simplified stand-in for the web app's much larger per-ingredient
 * category database — good enough for local browsing, to be reconciled
 * once the real sync (android/README.md step 6) ports pantry data itself.
 *
 * Lives in the plain :logic module (no Android dependency) so the pantry
 * mutation logic can be unit-tested — see PantryOperationsTest.
 */
enum class PantryCategory(val label: String) {
    NABIAL("Nabiał"),
    WARZYWA("Warzywa"),
    OWOCE("Owoce"),
    MIESO("Mięso i ryby"),
    ZBOZOWE("Produkty zbożowe"),
    PRZYPRAWY("Przyprawy"),
    INNE("Inne"),
}

sealed class PantryItem {
    abstract val name: String
    abstract val category: PantryCategory

    data class Product(
        override val name: String,
        override val category: PantryCategory,
        val quantity: Double,
        val unit: String,
    ) : PantryItem()

    data class Spice(
        override val name: String,
        override val category: PantryCategory,
        val level: SpiceLevel,
    ) : PantryItem()
}

enum class SpiceLevel(val label: String) {
    BRAK("Brak"),
    MALO("Mało"),
    WYSTARCZY("Wystarczy"),
}
