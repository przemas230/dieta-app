package com.przemas230.dietaapp.data

/**
 * FR-36: one meal-slot's "did I actually eat this" state for today --
 * mirrors index.html's state.eaten[date][catId] shape ({done, kcal, name}).
 * kcal/name are captured at the moment something is marked eaten (not
 * re-derived later from the Planer, which is a reusable weekly template,
 * not a per-date record) so unmarking-then-remarking after the Planer slot
 * changes still reports what was actually eaten at the time.
 */
data class EatenEntry(
    val done: Boolean,
    val kcal: Int? = null,
    val name: String? = null,
    /**
     * FR-99 (2026-08-29): how much of the dish was actually eaten, 0..1 --
     * a left swipe on the Planer card records half a portion instead of
     * the all-or-nothing flag [done] alone could express. Defaults to a
     * whole portion, so every entry written before this existed (and every
     * entry coming from a device that predates it) reads back exactly as
     * it always did. Only meaningful while [done] is true.
     */
    val portion: Double = 1.0,
)

/**
 * FR-83: one calendar day's full eaten record -- mirrors index.html's
 * state.eaten[date] shape ({catId: EatenEntry-like, snacks: [...]} in one
 * flat object). Introduced so Android can track every date the user has
 * touched, not just "today" -- see EatenViewModel's doc comment for what
 * changed and why.
 */
data class EatenDay(val entries: Map<String, EatenEntry> = emptyMap(), val snacks: List<Snack> = emptyList())
