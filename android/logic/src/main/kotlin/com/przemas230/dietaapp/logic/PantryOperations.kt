package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.SpiceLevel

/**
 * Pure port of PantryViewModel's mutation logic from the app module — every
 * function takes the current name-keyed map and returns the next one,
 * with no Android/StateFlow dependency, so it can be unit-tested directly.
 * PantryViewModel becomes a thin StateFlow wrapper around these.
 */
object PantryOperations {
    fun addProduct(
        items: Map<String, PantryItem>,
        name: String,
        category: PantryCategory,
        quantity: Double,
        unit: String,
    ): Map<String, PantryItem> {
        if (name.isBlank() || quantity <= 0) return items
        return items + (name to PantryItem.Product(name, category, quantity, unit))
    }

    fun addSpice(items: Map<String, PantryItem>, name: String, category: PantryCategory, level: SpiceLevel): Map<String, PantryItem> {
        if (name.isBlank()) return items
        return items + (name to PantryItem.Spice(name, category, level))
    }

    fun adjustProductQuantity(items: Map<String, PantryItem>, name: String, delta: Double): Map<String, PantryItem> {
        val item = items[name] as? PantryItem.Product ?: return items
        val newQty = item.quantity + delta
        return if (newQty <= 0.0001) {
            items - name
        } else {
            items + (name to item.copy(quantity = newQty))
        }
    }

    fun cycleSpiceLevel(items: Map<String, PantryItem>, name: String): Map<String, PantryItem> {
        val item = items[name] as? PantryItem.Spice ?: return items
        val next = when (item.level) {
            SpiceLevel.BRAK -> SpiceLevel.MALO
            SpiceLevel.MALO -> SpiceLevel.WYSTARCZY
            SpiceLevel.WYSTARCZY -> SpiceLevel.BRAK
        }
        return items + (name to item.copy(level = next))
    }

    fun removeItem(items: Map<String, PantryItem>, name: String): Map<String, PantryItem> = items - name

    /** FR-30: long-press "🗂️ Zmień kategorię" -- moves the tile to another section without touching its quantity/unit/level. */
    fun changeCategory(items: Map<String, PantryItem>, name: String, category: PantryCategory): Map<String, PantryItem> {
        val item = items[name] ?: return items
        val updated: PantryItem = when (item) {
            is PantryItem.Product -> item.copy(category = category)
            is PantryItem.Spice -> item.copy(category = category)
        }
        return items + (name to updated)
    }

    /** IngredientCanon.CANON_INFO's cat labels don't have a "Strączki i orzechy" bucket here -- falls back to INNE, like an unrecognized ingredient would. */
    fun categoryForCanon(label: String): PantryCategory = when (label) {
        "Nabiał" -> PantryCategory.NABIAL
        "Warzywa" -> PantryCategory.WARZYWA
        "Owoce" -> PantryCategory.OWOCE
        "Mięso, ryby, jajka" -> PantryCategory.MIESO
        "Pieczywo i zboża" -> PantryCategory.ZBOZOWE
        "Przyprawy" -> PantryCategory.PRZYPRAWY
        else -> PantryCategory.INNE
    }

    /**
     * FR-16: "Mam to" toggle in the per-recipe pantry-check window -- port of
     * index.html's haveBtn handler in openPantryModal. Removes the entry if
     * present; otherwise adds a spice at "Wystarczy" (for Przyprawy) or a
     * product at a default step quantity (100 for weight/volume, 1 for
     * count), same as web's tileStep().
     */
    fun toggleHaveIngredient(
        items: Map<String, PantryItem>,
        name: String,
        category: PantryCategory,
        unitCat: String,
    ): Map<String, PantryItem> {
        if (items.containsKey(name)) return items - name
        return if (category == PantryCategory.PRZYPRAWY) {
            items + (name to PantryItem.Spice(name, category, SpiceLevel.WYSTARCZY))
        } else {
            val (qty, unit) = when (unitCat) {
                "weight" -> 100.0 to "g"
                "volume" -> 100.0 to "ml"
                else -> 1.0 to "szt."
            }
            items + (name to PantryItem.Product(name, category, qty, unit))
        }
    }
}
