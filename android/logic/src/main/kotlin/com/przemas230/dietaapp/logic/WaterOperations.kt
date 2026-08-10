package com.przemas230.dietaapp.logic

/**
 * FR-70: pure port of index.html's header droplet click handler --
 * `state.water.count = (state.water.count === i+1) ? i : i+1`. Tapping
 * droplet `index` (0-based) sets the count to `index+1`, except tapping the
 * droplet exactly at the current level steps back by one glass instead of
 * jumping straight to 0 -- every value 0..MAX_LEVEL stays reachable without
 * wrapping, unlike the original single-button-increment design this replaced.
 */
object WaterOperations {
    const val MAX_LEVEL = 8

    fun tapDroplet(current: Int, index: Int): Int = if (current == index + 1) index else index + 1
}
