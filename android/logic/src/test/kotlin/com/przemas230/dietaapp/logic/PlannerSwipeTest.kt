package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * FR-103: the four graded outcomes of a Planer meal-card swipe. These
 * thresholds are the whole point of the feature (short vs long is what
 * separates "zrobione" from "zjedzone"), so they get pinned down here
 * rather than only living in a Compose gesture handler nothing can test.
 */
class PlannerSwipeTest {
    private val short = PlannerSwipe.SHORT_DP
    private val long = PlannerSwipe.LONG_DP

    @Test
    fun `a drag inside the dead zone commits nothing in either direction`() {
        assertNull(PlannerSwipe.actionFor(0f, short, long))
        assertNull(PlannerSwipe.actionFor(short - 1f, short, long))
        assertNull(PlannerSwipe.actionFor(-short + 1f, short, long))
    }

    @Test
    fun `a short right swipe marks the dish cooked, a long one marks it eaten`() {
        assertEquals(PlannerSwipe.Action.COOKED, PlannerSwipe.actionFor(short, short, long))
        assertEquals(PlannerSwipe.Action.COOKED, PlannerSwipe.actionFor(long - 1f, short, long))
        assertEquals(PlannerSwipe.Action.EATEN, PlannerSwipe.actionFor(long, short, long))
        assertEquals(PlannerSwipe.Action.EATEN, PlannerSwipe.actionFor(long + 500f, short, long))
    }

    @Test
    fun `a short left swipe records half a portion, a long one resets everything`() {
        assertEquals(PlannerSwipe.Action.HALF, PlannerSwipe.actionFor(-short, short, long))
        assertEquals(PlannerSwipe.Action.HALF, PlannerSwipe.actionFor(-long + 1f, short, long))
        assertEquals(PlannerSwipe.Action.RESET, PlannerSwipe.actionFor(-long, short, long))
        assertEquals(PlannerSwipe.Action.RESET, PlannerSwipe.actionFor(-long - 500f, short, long))
    }

    @Test
    fun `tint intensity ramps from 0 at the action's own threshold to 1 at the next one`() {
        val max = PlannerSwipe.MAX_DP
        assertEquals(0f, PlannerSwipe.intensityFor(short, PlannerSwipe.Action.COOKED, short, long, max))
        assertEquals(1f, PlannerSwipe.intensityFor(long, PlannerSwipe.Action.COOKED, short, long, max))
        assertEquals(0f, PlannerSwipe.intensityFor(-long, PlannerSwipe.Action.RESET, short, long, max))
        assertEquals(1f, PlannerSwipe.intensityFor(-max, PlannerSwipe.Action.RESET, short, long, max))
    }

    @Test
    fun `intensity never leaves 0 to 1 even when dragged past the visual clamp`() {
        val max = PlannerSwipe.MAX_DP
        assertEquals(1f, PlannerSwipe.intensityFor(9999f, PlannerSwipe.Action.EATEN, short, long, max))
        assertEquals(0f, PlannerSwipe.intensityFor(short - 10f, PlannerSwipe.Action.COOKED, short, long, max))
    }
}
