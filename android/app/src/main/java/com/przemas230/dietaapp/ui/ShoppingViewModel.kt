package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.logic.ShoppingOperations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Thin StateFlow/Android wrapper around ShoppingOperations (in the :logic
 * module, see android/logic/ — unit-tested there in ShoppingOperationsTest).
 * Local-only for now, not yet persisted, synced, or auto-filled from the
 * planner (README.md steps 4/6).
 */
class ShoppingViewModel : ViewModel() {
    private val _items = MutableStateFlow<Map<String, ShoppingItem>>(emptyMap())
    val items: StateFlow<Map<String, ShoppingItem>> = _items.asStateFlow()

    fun addItem(name: String, quantity: Double, unit: String) {
        _items.value = ShoppingOperations.addItem(_items.value, name, quantity, unit)
    }

    fun toggleChecked(name: String) {
        _items.value = ShoppingOperations.toggleChecked(_items.value, name)
    }

    fun removeItem(name: String) {
        _items.value = ShoppingOperations.removeItem(_items.value, name)
    }

    fun clearChecked() {
        _items.value = ShoppingOperations.clearChecked(_items.value)
    }
}
