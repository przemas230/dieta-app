package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Local-only shopping-list state (name -> ShoppingItem), same key shape as
 * the web app's state.shopping map. Not yet persisted or synced to
 * Firestore, and not yet auto-filled from the planner (README.md steps 4/6).
 */
class ShoppingViewModel : ViewModel() {
    private val _items = MutableStateFlow<Map<String, ShoppingItem>>(emptyMap())
    val items: StateFlow<Map<String, ShoppingItem>> = _items.asStateFlow()

    fun addItem(name: String, quantity: Double, unit: String) {
        if (name.isBlank() || quantity <= 0) return
        _items.value = _items.value + (name to ShoppingItem(name, quantity, unit))
    }

    fun toggleChecked(name: String) {
        val item = _items.value[name] ?: return
        _items.value = _items.value + (name to item.copy(checked = !item.checked))
    }

    fun removeItem(name: String) {
        _items.value = _items.value - name
    }

    fun clearChecked() {
        _items.value = _items.value.filterValues { !it.checked }
    }
}
