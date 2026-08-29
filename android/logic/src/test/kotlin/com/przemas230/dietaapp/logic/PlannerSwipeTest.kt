package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * FR-103 (rebuilt 2026-08-29): the meal-card swipe steps through the dish's
 * lifecycle. These transitions ARE the feature — one swipe right must mean
 * "zrobione" and the next one "zjedzone", and every backward swipe must land
 * exactly on the state its forward twin came from — so they get pinned down
 * here rather than only living inside a Compose gesture handler.
 */
class PlannerSwipeTest {
    @Test
    fun `swiping right walks nothing to cooked to eaten`() {
        assertEquals(PlannerSwipe.Stage.COOKED, PlannerSwipe.nextStage(PlannerSwipe.Stage.NONE, 1))
        assertEquals(PlannerSwipe.Stage.EATEN, PlannerSwipe.nextStage(PlannerSwipe.Stage.COOKED, 1))
    }

    @Test
    fun `swiping right past eaten has nowhere to go`() {
        assertNull(PlannerSwipe.nextStage(PlannerSwipe.Stage.EATEN, 1))
    }

    @Test
    fun `swiping left walks eaten back to cooked and cooked back to nothing`() {
        assertEquals(PlannerSwipe.Stage.COOKED, PlannerSwipe.nextStage(PlannerSwipe.Stage.EATEN, -1))
        assertEquals(PlannerSwipe.Stage.NONE, PlannerSwipe.nextStage(PlannerSwipe.Stage.COOKED, -1))
    }

    @Test
    fun `swiping left from nothing has nowhere to go`() {
        assertNull(PlannerSwipe.nextStage(PlannerSwipe.Stage.NONE, -1))
    }

    @Test
    fun `every forward step is undone exactly by the matching backward step`() {
        for (stage in listOf(PlannerSwipe.Stage.NONE, PlannerSwipe.Stage.COOKED)) {
            val forward = PlannerSwipe.nextStage(stage, 1)!!
            assertEquals(stage, PlannerSwipe.nextStage(forward, -1))
        }
    }

    @Test
    fun `the stage is derived from the eaten and cooked records, eaten winning`() {
        assertEquals(PlannerSwipe.Stage.NONE, PlannerSwipe.stageOf(isEaten = false, isCooked = false))
        assertEquals(PlannerSwipe.Stage.COOKED, PlannerSwipe.stageOf(isEaten = false, isCooked = true))
        assertEquals(PlannerSwipe.Stage.EATEN, PlannerSwipe.stageOf(isEaten = true, isCooked = false))
        // Eaten implies the dish is done with, whether or not it was also
        // logged as cooked -- otherwise a dish cooked AND eaten would render
        // as merely cooked.
        assertEquals(PlannerSwipe.Stage.EATEN, PlannerSwipe.stageOf(isEaten = true, isCooked = true))
    }

    @Test
    fun `a drag shorter than the commit threshold does nothing in either direction`() {
        val commit = PlannerSwipe.COMMIT_DP
        assertEquals(0, PlannerSwipe.directionFor(0f, commit))
        assertEquals(0, PlannerSwipe.directionFor(commit - 1f, commit))
        assertEquals(0, PlannerSwipe.directionFor(-commit + 1f, commit))
    }

    @Test
    fun `reaching the threshold commits one step, and going further changes nothing but the tint`() {
        val commit = PlannerSwipe.COMMIT_DP
        assertEquals(1, PlannerSwipe.directionFor(commit, commit))
        assertEquals(1, PlannerSwipe.directionFor(commit * 10, commit))
        assertEquals(-1, PlannerSwipe.directionFor(-commit, commit))
        assertEquals(-1, PlannerSwipe.directionFor(-commit * 10, commit))
    }

    @Test
    fun `tint intensity ramps from 0 at the threshold to 1 at the travel limit`() {
        val commit = PlannerSwipe.COMMIT_DP
        val max = PlannerSwipe.MAX_DP
        assertEquals(0f, PlannerSwipe.intensityFor(commit, commit, max))
        assertEquals(1f, PlannerSwipe.intensityFor(max, commit, max))
        assertEquals(1f, PlannerSwipe.intensityFor(-9999f, commit, max))
        assertEquals(0f, PlannerSwipe.intensityFor(commit - 5f, commit, max))
    }
}
