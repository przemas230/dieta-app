package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.PantryStore
import com.przemas230.dietaapp.data.Recipe
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

    /**
     * FR-28: tap the upper half of a tile (dir=+1) to add, the lower half
     * (dir=-1) to subtract -- also how "➕ Dodaj własny" creates a brand-new
     * custom tile (a plain dir=+1 tap on a name that doesn't exist yet).
     */
    fun tileTapDelta(name: String, category: PantryCategory, unitCat: String, dir: Int) {
        update(PantryOperations.tileTapDelta(_items.value, name, category, unitCat, dir))
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

    /** Long-press a product tile -> "🔢 Zmień skok +/-". */
    fun changeStep(name: String, newStep: Double) {
        update(PantryOperations.changeStep(_items.value, name, newStep))
    }

    /** FR-73: applies an incoming cloud snapshot wholesale (last-cloud-write-wins), replacing local state. */
    fun replaceAll(items: Map<String, PantryItem>) {
        update(items)
    }
}
