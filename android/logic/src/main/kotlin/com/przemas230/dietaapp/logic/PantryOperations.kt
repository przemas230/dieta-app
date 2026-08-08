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
}
