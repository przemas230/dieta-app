package com.przemas230.dietaapp.data

/**
 * FR-36: one meal-slot's "did I actually eat this" state for today --
 * mirrors index.html's state.eaten[date][catId] shape ({done, kcal, name}).
 * kcal/name are captured at the moment something is marked eaten (not
 * re-derived later from the Planer, which is a reusable weekly template,
 * not a per-date record) so unmarking-then-remarking after the Planer slot
 * changes still reports what was actually eaten at the time.
 */
data class EatenEntry(val done: Boolean, val kcal: Int? = null, val name: String? = null)
