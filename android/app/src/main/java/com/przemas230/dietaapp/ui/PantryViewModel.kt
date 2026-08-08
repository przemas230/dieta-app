package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.SpiceLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Local-only pantry state (name -> PantryItem), same key shape as the web
 * app's state.pantry map. Not yet persisted or synced to Firestore — that's
 * README.md step 6 (full sync port). Held in memory for now, same as the
 * rest of this milestone's screens.
 */
class PantryViewModel : ViewModel() {
    private val _items = MutableStateFlow<Map<String, PantryItem>>(emptyMap())
    val items: StateFlow<Map<String, PantryItem>> = _items.asStateFlow()

    fun addProduct(name: String, category: PantryCategory, quantity: Double, unit: String) {
        if (name.isBlank() || quantity <= 0) return
        _items.value = _items.value + (name to PantryItem.Product(name, category, quantity, unit))
    }

    fun addSpice(name: String, category: PantryCategory, level: SpiceLevel) {
        if (name.isBlank()) return
        _items.value = _items.value + (name to PantryItem.Spice(name, category, level))
    }

    fun adjustProductQuantity(name: String, delta: Double) {
        val item = _items.value[name] as? PantryItem.Product ?: return
        val newQty = item.quantity + delta
        _items.value = if (newQty <= 0.0001) {
            _items.value - name
        } else {
            _items.value + (name to item.copy(quantity = newQty))
        }
    }

    fun cycleSpiceLevel(name: String) {
        val item = _items.value[name] as? PantryItem.Spice ?: return
        val next = when (item.level) {
            SpiceLevel.BRAK -> SpiceLevel.MALO
            SpiceLevel.MALO -> SpiceLevel.WYSTARCZY
            SpiceLevel.WYSTARCZY -> SpiceLevel.BRAK
        }
        _items.value = _items.value + (name to item.copy(level = next))
    }

    fun removeItem(name: String) {
        _items.value = _items.value - name
    }
}
