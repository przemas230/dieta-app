package com.przemas230.dietaapp.logic

/**
 * FR-44: pure scroll-direction/near-top decisions for the Przepisy tab's
 * auto-hide/show header -- port of index.html's direction-based show/hide
 * toolbar logic (`applyState`, NEAR_TOP/DELTA thresholds). Compose's
 * LazyListState exposes position as (firstVisibleItemIndex, scrollOffset)
 * rather than a single scrollY, so "direction" here means index advancing,
 * or the same index with a larger/smaller offset -- not a raw pixel delta.
 */
object HeaderScrollBehavior {
    fun scrolledDown(prevIndex: Int, prevOffset: Int, currIndex: Int, currOffset: Int): Boolean =
        if (currIndex != prevIndex) currIndex > prevIndex else currOffset > prevOffset

    fun scrolledUp(prevIndex: Int, prevOffset: Int, currIndex: Int, currOffset: Int): Boolean =
        if (currIndex != prevIndex) currIndex < prevIndex else currOffset < prevOffset

    /** Matches index.html's NEAR_TOP=60 -- close enough to the top that the header should show regardless of direction. */
    fun isNearTop(index: Int, offset: Int, nearTopPx: Int): Boolean = index == 0 && offset <= nearTopPx
}
