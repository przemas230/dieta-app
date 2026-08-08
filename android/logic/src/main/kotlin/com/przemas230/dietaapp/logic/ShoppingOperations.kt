package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ShoppingItem

/**
 * Pure port of ShoppingViewModel's mutation logic from the app module — see
 * PantryOperations.kt for the same pattern.
 */
object ShoppingOperations {
    fun addItem(items: Map<String, ShoppingItem>, name: String, quantity: Double, unit: String): Map<String, ShoppingItem> {
        if (name.isBlank() || quantity <= 0) return items
        return items + (name to ShoppingItem(name, quantity, unit))
    }

    fun toggleChecked(items: Map<String, ShoppingItem>, name: String): Map<String, ShoppingItem> {
        val item = items[name] ?: return items
        return items + (name to item.copy(checked = !item.checked))
    }

    fun removeItem(items: Map<String, ShoppingItem>, name: String): Map<String, ShoppingItem> = items - name

    fun clearChecked(items: Map<String, ShoppingItem>): Map<String, ShoppingItem> = items.filterValues { !it.checked }
}
