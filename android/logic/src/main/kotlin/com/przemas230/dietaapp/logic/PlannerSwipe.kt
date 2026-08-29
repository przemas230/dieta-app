package com.przemas230.dietaapp.logic

/**
 * FR-103 (2026-08-29, REBUILT the same day after live feedback): what a
 * horizontal swipe on a Planer meal row means -- the exact port of
 * index.html's `pdMealStage`/`pdNextStage`.
 *
 * v1 mapped swipe DISTANCE to one of four outcomes (short right = cooked,
 * long right = eaten, short left = half a portion, long left = reset). The
 * user tried it and asked for something that follows the dish's own
 * lifecycle instead: "jedno przesunięcie to zrobione i wtedy podświetla na
 * zielono że gotowe do zjedzenia a drugie takie samo przesunięcie niech
 * skreśla i wyszarza delikatnie jako oznaczenie że zjedzone". So the
 * gesture is now one STEP along a three-state line, and the direction alone
 * decides which way:
 *
 * ```
 *   NONE  --swipe right-->  COOKED  --swipe right-->  EATEN
 *   NONE  <--swipe left--   COOKED  <--swipe left--   EATEN
 * ```
 *
 * Forward steps subtract (pantry on COOKED, kcal on EATEN); backward steps
 * give exactly that back -- "cofnięcie niech cofa odejmowanie zarówno
 * kalorii tak jak teraz jak i rzeczy do spiżarni". Partial portions moved
 * out of the gesture entirely and onto a long-press (FR-105), because a
 * gesture meaning "one step" cannot also mean "62% of a portion" without
 * going back to guessing distances.
 *
 * The stage is DERIVED from the cook history and the eaten record, never
 * stored: both already existed and are still written by the rest of the app
 * (the recipe card's own "✅ Zrobione dzisiaj", the Postęp checkbox), so
 * this stays a view of those two rather than a third source of truth that
 * could disagree with them.
 */
object PlannerSwipe {
    /**
     * How far a drag must travel to count as one step.
     *
     * Deliberately small (user: "żeby to przesuwanie było bardziej czułe"):
     * one step is one step no matter how far the finger goes, so there is no
     * reason to demand a long drag -- only enough to tell a swipe from a tap.
     */
    const val COMMIT_DP = 30f

    /** How far the card is allowed to travel visually before it stops following the finger. */
    const val MAX_DP = 96f

    enum class Stage(val label: String) {
        NONE(""),
        COOKED("🍳 Zrobione"),
        EATEN("🍴 Zjedzone"),
    }

    private val ORDER = listOf(Stage.NONE, Stage.COOKED, Stage.EATEN)

    /**
     * One step from [stage] in [direction] (+1 right, -1 left), or null when
     * there is nowhere further to go that way -- the caller then says
     * "already eaten" / "nothing to undo" instead of letting the card slide
     * with no explanation.
     */
    fun nextStage(stage: Stage, direction: Int): Stage? {
        val next = ORDER.indexOf(stage) + if (direction > 0) 1 else -1
        if (next < 0 || next >= ORDER.size) return null
        return ORDER[next]
    }

    /** The stage a meal is currently at, derived from the two records that already track it. */
    fun stageOf(isEaten: Boolean, isCooked: Boolean): Stage = when {
        isEaten -> Stage.EATEN
        isCooked -> Stage.COOKED
        else -> Stage.NONE
    }

    /** Whether a drag of [dx] px commits a step, and in which direction (0 = not far enough). */
    fun directionFor(dx: Float, commitThreshold: Float): Int = when {
        dx >= commitThreshold -> 1
        dx <= -commitThreshold -> -1
        else -> 0
    }

    /**
     * 0..1 ramp of how far past the commit threshold the drag has travelled,
     * for the live background tint -- so the card visibly "locks in" deeper
     * as the finger keeps going instead of snapping to one flat colour.
     */
    fun intensityFor(dx: Float, commitThreshold: Float, maxTravel: Float): Float {
        val span = maxTravel - commitThreshold
        if (span <= 0f) return 1f
        return ((kotlin.math.abs(dx) - commitThreshold) / span).coerceIn(0f, 1f)
    }
}
