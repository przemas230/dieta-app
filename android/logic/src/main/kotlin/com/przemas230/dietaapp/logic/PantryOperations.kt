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
    /**
     * FR-28: the single tap-delta function driving both tile halves — a
     * one-to-one port of index.html's tileTapDelta(). `dir=+1` (tap upper
     * half) increases; `dir=-1` (tap lower half) decreases. An untracked
     * tile only reacts to `dir=+1` (creates the entry — a product at
     * `PantryTiles.tileStep(unitCat)`, or a spice at the lowest level,
     * "Mało"); `dir=-1` on an untracked tile is a no-op, exactly like the
     * web source — there's no "going negative" before ever tapping a tile.
     * A product clamps at quantity 0 but stays tracked/"active" (only the
     * long-press "remove tracking" action deletes the entry) — same for a
     * spice clamped at "Mało"/"Dużo" (no wrap, no auto-delete). Both are
     * deliberate, faithfully-ported quirks of the web source, not bugs.
     */
    fun tileTapDelta(
        items: Map<String, PantryItem>,
        name: String,
        category: PantryCategory,
        unitCat: String,
        dir: Int,
    ): Map<String, PantryItem> {
        val current = items[name]
        return if (category == PantryCategory.PRZYPRAWY) {
            if (current == null) {
                if (dir < 0) return items
                items + (name to PantryItem.Spice(name, category, SpiceLevel.MALO))
            } else {
                val spice = current as? PantryItem.Spice ?: return items
                val nextIndex = SpiceLevel.entries.indexOf(spice.level) + dir
                if (nextIndex < 0) return items
                val clamped = SpiceLevel.entries[nextIndex.coerceAtMost(SpiceLevel.entries.size - 1)]
                items + (name to spice.copy(level = clamped))
            }
        } else {
            val product = current as? PantryItem.Product
            val step = product?.stepOverride ?: PantryTiles.tileStep(unitCat)
            if (product == null) {
                if (dir < 0) return items
                items + (name to PantryItem.Product(name, category, step, PantryTiles.unitCatToUnit(unitCat)))
            } else {
                val newQty = maxOf(0.0, Math.round((product.quantity + dir * step) * 100) / 100.0)
                items + (name to product.copy(quantity = newQty))
            }
        }
    }

    fun removeItem(items: Map<String, PantryItem>, name: String): Map<String, PantryItem> = items - name

    /**
     * FR-102 (2026-08-29, user: "nie da się usunąć produktu ze spiżarni
     * całkowicie, dodaj taką opcję"): which tiles the Spiżarnia grid should
     * actually show. [removeItem] above only drops the tracked STOCK -- the
     * tile itself is derived fresh from the recipe database on every
     * compose (PantryTiles.buildTileNames), so for the ~200 recipe-derived
     * products there was previously no way to make one disappear at all.
     * [hidden] is the persisted set of canonical names the user deleted for
     * good; it is filtered out here, in one place, so the grid and every
     * other consumer of the tile list agree instead of one of them quietly
     * re-introducing a deleted product.
     */
    fun visibleTileNames(
        recipeTileNames: Collection<String>,
        trackedNames: Collection<String>,
        hidden: Set<String>,
    ): List<String> = (recipeTileNames + trackedNames).distinct().filterNot { it in hidden }.sorted()

    /** FR-102: delete for good -- drops any tracked stock AND remembers not to render the tile again. */
    fun hideForever(hidden: Set<String>, name: String): Set<String> = hidden + name

    /** FR-102: "↩️ Przywróć usunięte produkty" -- one button, brings every hidden tile back as an untracked tile. */
    fun restoreAllHidden(): Set<String> = emptySet()

    /** Long-press a product tile -> "🔢 Zmień skok +/-" (weight/volume only, see PantryScreen's TileActionDialog). */
    fun changeStep(items: Map<String, PantryItem>, name: String, newStep: Double): Map<String, PantryItem> {
        val product = items[name] as? PantryItem.Product ?: return items
        return items + (name to product.copy(stepOverride = newStep))
    }

    /** FR-30: long-press "🗂️ Zmień kategorię" -- moves the tile to another section without touching its quantity/unit/level. */
    fun changeCategory(items: Map<String, PantryItem>, name: String, category: PantryCategory): Map<String, PantryItem> {
        val item = items[name] ?: return items
        val updated: PantryItem = when (item) {
            is PantryItem.Product -> item.copy(category = category)
            is PantryItem.Spice -> item.copy(category = category)
        }
        return items + (name to updated)
    }

    fun categoryForCanon(label: String): PantryCategory = PantryCategory.byLabel(label)

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
