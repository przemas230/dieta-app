package com.przemas230.dietaapp.logic

/**
 * FR-99 (2026-08-29): what a horizontal swipe on a "Dzisiejszy Planer" meal
 * card means, as a pure function of how far it travelled -- the exact port
 * of index.html's `pdSwipeAction(dx)`.
 *
 * The previous gesture (FR-87/v14) had two outcomes: any right swipe =
 * eaten, any left swipe = not eaten. That carried one bit of information
 * while the Planer actually tracks three separate things (cooked / eaten /
 * how much of it), which is why the user reported it as "coś nie do końca
 * łapię" -- there was no way to say "I made it but haven't eaten it yet",
 * and no way to correct an accidental one either. The DISTANCE now picks
 * the action:
 *
 * ```
 *  ->  short   COOKED   cook-history entry + pantry subtraction
 *  ->  long    EATEN    whole portion
 *  <-  short   HALF     half a portion (half the kcal)
 *  <-  long    RESET    not eaten + undo today's cook entry
 * ```
 *
 * Kept here (rather than inline in PlannerScreen) so the thresholds and
 * the four outcomes are unit-testable without Compose, and so the live
 * drag label, the live tint and the release handler physically cannot
 * disagree about what the current drag means.
 */
object PlannerSwipe {
    /** Distance (in dp) past which a swipe commits its "short" action. */
    const val SHORT_DP = 36f

    /** Distance (in dp) past which a swipe commits its "long" action instead. */
    const val LONG_DP = 105f

    /** How far a card is allowed to visually travel -- a bit past LONG_DP so the strongest action still has room to ramp up. */
    const val MAX_DP = 130f

    enum class Action(val label: String) {
        COOKED("🍳 Zrobione"),
        EATEN("🍴 Zjedzone"),
        HALF("½ Zjedzone w połowie"),
        RESET("↩️ Cofnij wszystko"),
    }

    /**
     * [dx] and the thresholds must be in the SAME unit (px on Android, CSS
     * px on web) -- the caller converts dp to px once and passes both.
     * Returns null inside the dead zone, i.e. releasing there does nothing.
     */
    fun actionFor(dx: Float, shortThreshold: Float, longThreshold: Float): Action? = when {
        dx >= longThreshold -> Action.EATEN
        dx >= shortThreshold -> Action.COOKED
        dx <= -longThreshold -> Action.RESET
        dx <= -shortThreshold -> Action.HALF
        else -> null
    }

    /**
     * 0..1 ramp of how far INTO the current action's own band the drag has
     * travelled, for the live background tint -- so a card visibly "locks
     * in" deeper as the finger keeps going, instead of the colour jumping
     * between two flat states at the thresholds.
     */
    fun intensityFor(dx: Float, action: Action, shortThreshold: Float, longThreshold: Float, maxTravel: Float): Float {
        val isLong = action == Action.EATEN || action == Action.RESET
        val base = if (isLong) longThreshold else shortThreshold
        val span = if (isLong) (maxTravel - longThreshold) else (longThreshold - shortThreshold)
        if (span <= 0f) return 1f
        return ((kotlin.math.abs(dx) - base) / span).coerceIn(0f, 1f)
    }
}
