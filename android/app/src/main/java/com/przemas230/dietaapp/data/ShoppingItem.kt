package com.przemas230.dietaapp.data

/**
 * Mirrors the shape of state.shopping entries in the web app's index.html
 * (name, qty, unit, checked) — deliberately without the "contributions"
 * bookkeeping the web app uses to auto-derive quantities from the planner,
 * since that comes with the planner port itself (README.md step 4/6).
 */
data class ShoppingItem(
    val name: String,
    val quantity: Double,
    val unit: String,
    val checked: Boolean = false,
)
