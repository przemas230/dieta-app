package com.przemas230.dietaapp.data

/**
 * Mirrors one state.shopping[canon+"|"+unitCat] entry in index.html:
 * quantity is the sum of every contributing recipe's/ingredient's share
 * (`contributions`, keyed by recipe id or a synthetic "single:..." source
 * key for FR-16's single-ingredient add), so removing one recipe (FR-25)
 * only subtracts its own share instead of the whole item.
 *
 * Lives in the plain :logic module (no Android dependency) so the shopping
 * mutation logic can be unit-tested — see ShoppingOperationsTest.
 */
data class ShoppingItem(
    val name: String,
    val unitCat: String,
    val quantity: Double,
    val checked: Boolean = false,
    val contributions: Map<String, Double> = emptyMap(),
)
