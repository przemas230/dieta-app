package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.PantryStore
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.SpiceLevel
import com.przemas230.dietaapp.logic.PantryOperations
import com.przemas230.dietaapp.logic.RecipePantryMatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Thin StateFlow/Android wrapper around PantryOperations (in the :logic
 * module, see android/logic/ — unit-tested there in PantryOperationsTest).
 * Persisted locally via PantryStore (SharedPreferences, write-through on
 * every mutation) — not yet synced to Firestore across devices, that's
 * README.md step 6 (full sync port), but no longer wiped just by the app
 * process dying in the background.
 */
class PantryViewModel(application: Application) : AndroidViewModel(application) {
    private val _items = MutableStateFlow(PantryStore.load(application))
    val items: StateFlow<Map<String, PantryItem>> = _items.asStateFlow()

    private fun update(next: Map<String, PantryItem>) {
        _items.value = next
        PantryStore.save(getApplication(), next)
    }

    fun addProduct(name: String, category: PantryCategory, quantity: Double, unit: String) {
        update(PantryOperations.addProduct(_items.value, name, category, quantity, unit))
    }

    fun addSpice(name: String, category: PantryCategory, level: SpiceLevel) {
        update(PantryOperations.addSpice(_items.value, name, category, level))
    }

    fun adjustProductQuantity(name: String, delta: Double) {
        update(PantryOperations.adjustProductQuantity(_items.value, name, delta))
    }

    fun cycleSpiceLevel(name: String) {
        update(PantryOperations.cycleSpiceLevel(_items.value, name))
    }

    fun removeItem(name: String) {
        update(PantryOperations.removeItem(_items.value, name))
    }

    /** FR-15: called when a recipe is marked "✅ Zrobione dzisiaj". */
    fun subtractForRecipe(recipe: Recipe) {
        update(RecipePantryMatching.subtractForRecipe(_items.value, recipe))
    }

    /** FR-15: called when a cook-history entry is deleted, to undo its subtraction. */
    fun restoreForRecipe(recipe: Recipe) {
        update(RecipePantryMatching.restoreForRecipe(_items.value, recipe))
    }

    /** FR-16: "Mam to" toggle in the per-recipe pantry-check window. */
    fun toggleHaveIngredient(name: String, category: PantryCategory, unitCat: String) {
        update(PantryOperations.toggleHaveIngredient(_items.value, name, category, unitCat))
    }

    /** FR-30: long-press a tile -> "🗂️ Zmień kategorię". */
    fun changeCategory(name: String, category: PantryCategory) {
        update(PantryOperations.changeCategory(_items.value, name, category))
    }
}
