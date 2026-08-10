package com.przemas230.dietaapp.data

/**
 * FR-40: one day's weigh-in -- mirrors index.html's `state.weights` entries
 * (`{date, kg}`). `dateStr` is "YYYY-MM-DD" (UTC), matching the web app's
 * `todayStr()` -- kept as a string (not epoch millis) since the only things
 * ever done with it are exact-match dedup-by-day and lexicographic sort,
 * both of which "YYYY-MM-DD" strings support directly.
 */
data class WeightEntry(val dateStr: String, val kg: Double)
