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

    // ---- FR-103/v3: a tap that drifted must not step the stage ----

    private val commit = PlannerSwipe.COMMIT_DP
    private val definite = PlannerSwipe.DEFINITE_DP

    @Test
    fun `a quick short movement is treated as a tap, not a step`() {
        // 40 dp in 90 ms -- a finger sliding during an ordinary tap.
        assertEquals(0, PlannerSwipe.commitDirection(40f, 90L, commit, definite))
        assertEquals(0, PlannerSwipe.commitDirection(-40f, 90L, commit, definite))
    }

    @Test
    fun `the same short distance IS a step when the finger lingered`() {
        // Deliberate short drag: same 40 dp, but 300 ms on screen.
        assertEquals(1, PlannerSwipe.commitDirection(40f, 300L, commit, definite))
        assertEquals(-1, PlannerSwipe.commitDirection(-40f, 300L, commit, definite))
    }

    @Test
    fun `a fast flick still works, however brief`() {
        // This is why time alone cannot be the rule: a decisive flick is over
        // in under 100 ms, and swallowing it would break the main gesture.
        assertEquals(1, PlannerSwipe.commitDirection(120f, 60L, commit, definite))
        assertEquals(-1, PlannerSwipe.commitDirection(-120f, 60L, commit, definite))
    }

    @Test
    fun `below the commit threshold nothing happens, however long the press`() {
        assertEquals(0, PlannerSwipe.commitDirection(10f, 5_000L, commit, definite))
    }

    @Test
    fun `the tap guard only narrows -- it never commits where distance alone would not`() {
        for (dx in listOf(-200f, -61f, -31f, -5f, 0f, 5f, 31f, 61f, 200f)) {
            for (ms in listOf(0L, 100L, 149L, 150L, 1_000L)) {
                val guarded = PlannerSwipe.commitDirection(dx, ms, commit, definite)
                val plain = PlannerSwipe.directionFor(dx, commit)
                if (guarded != 0) assertEquals(plain, guarded)
            }
        }
    }
}
