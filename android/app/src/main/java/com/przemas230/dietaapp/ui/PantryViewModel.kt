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

    /** FR-102: canonical names deleted from the Spiżarnia for good -- see [deleteForever]. */
    private val _hidden = MutableStateFlow(PantryStore.loadHidden(application))
    val hidden: StateFlow<Set<String>> = _hidden.asStateFlow()

    private fun update(next: Map<String, PantryItem>) {
        _items.value = next
        PantryStore.save(getApplication(), next)
    }

    private fun updateHidden(next: Set<String>) {
        _hidden.value = next
        PantryStore.saveHidden(getApplication(), next)
    }

    /**
     * FR-102: "❌ Usuń produkt ze spiżarni na stałe". [removeItem] only
     * clears the tracked stock -- the tile itself comes back on the next
     * compose because it is derived from the recipe database, which is
     * exactly what the user hit ("nie da się usunąć produktu ze spiżarni
     * całkowicie"). This drops the stock AND remembers the name, so the
     * tile really disappears until [restoreHidden].
     */
    fun deleteForever(name: String) {
        update(PantryOperations.removeItem(_items.value, name))
        updateHidden(PantryOperations.hideForever(_hidden.value, name))
    }

    /** FR-102: the "↩️ Przywróć usunięte produkty" button -- never a one-way door. */
    fun restoreHidden() {
        updateHidden(PantryOperations.restoreAllHidden())
    }

    /** FR-102: adding a deleted product back by hand (custom tile / "Mam to") un-deletes it. */
    fun unhide(name: String) {
        if (name in _hidden.value) updateHidden(_hidden.value - name)
    }

    /** FR-73: applies an incoming cloud snapshot of the hidden set. */
    fun replaceHidden(hidden: Set<String>) = updateHidden(hidden)

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

    /**
     * FR-106: puts a whole recipe's ingredients into the pantry after the
     * shopping list says they were all bought. Unlike [restoreForRecipe] this
     * CREATES entries that were not tracked before -- see
     * RecipePantryMatching.stockFromRecipe for why the two cannot be one
     * function.
     */
    fun stockFromRecipe(recipe: Recipe) {
        update(RecipePantryMatching.stockFromRecipe(_items.value, recipe))
    }

    /** FR-15: called when a cook-history entry is deleted, to undo its subtraction. */
    fun restoreForRecipe(recipe: Recipe) {
        update(RecipePantryMatching.restoreForRecipe(_items.value, recipe))
    }

    /** FR-16: "Mam to" toggle in the per-recipe pantry-check window. */
    fun toggleHaveIngredient(name: String, category: PantryCategory, unitCat: String) {
        // FR-102: saying "mam to" about an ingredient that was once deleted
        // for good is a clear signal it should exist again -- otherwise the
        // stock entry would be written while the tile stayed hidden in
        // Spiżarnia, which reads as a bug.
        if (!_items.value.containsKey(name)) unhide(name)
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
