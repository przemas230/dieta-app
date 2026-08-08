package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.SpiceLevel
import com.przemas230.dietaapp.logic.PantryOperations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Thin StateFlow/Android wrapper around PantryOperations (in the :logic
 * module, see android/logic/ — unit-tested there in PantryOperationsTest).
 * Local-only for now, not yet persisted or synced to Firestore — that's
 * README.md step 6 (full sync port).
 */
class PantryViewModel : ViewModel() {
    private val _items = MutableStateFlow<Map<String, PantryItem>>(emptyMap())
    val items: StateFlow<Map<String, PantryItem>> = _items.asStateFlow()

    fun addProduct(name: String, category: PantryCategory, quantity: Double, unit: String) {
        _items.value = PantryOperations.addProduct(_items.value, name, category, quantity, unit)
    }

    fun addSpice(name: String, category: PantryCategory, level: SpiceLevel) {
        _items.value = PantryOperations.addSpice(_items.value, name, category, level)
    }

    fun adjustProductQuantity(name: String, delta: Double) {
        _items.value = PantryOperations.adjustProductQuantity(_items.value, name, delta)
    }

    fun cycleSpiceLevel(name: String) {
        _items.value = PantryOperations.cycleSpiceLevel(_items.value, name)
    }

    fun removeItem(name: String) {
        _items.value = PantryOperations.removeItem(_items.value, name)
    }
}
