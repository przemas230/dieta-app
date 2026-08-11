package com.przemas230.dietaapp.logic

/**
 * FR-44/v2: pure near-top decision for the Przepisy tab's auto-hide/show
 * header -- port of index.html's near-top-only `applyState()` (2026-08-11,
 * on explicit user request, replacing an earlier direction-based version:
 * "górny header... niech tylko rozwijają się na górze listy z przepisami
 * jak już się jest niżej to niech będzie cały czas schowany"). Compose's
 * LazyListState exposes position as (firstVisibleItemIndex, scrollOffset)
 * rather than a single scrollY, hence the two-argument shape.
 */
object HeaderScrollBehavior {
    /** Matches index.html's NEAR_TOP=60 -- close enough to the top that the header should show; anywhere else it should be hidden, regardless of scroll direction. */
    fun isNearTop(index: Int, offset: Int, nearTopPx: Int): Boolean = index == 0 && offset <= nearTopPx
}
