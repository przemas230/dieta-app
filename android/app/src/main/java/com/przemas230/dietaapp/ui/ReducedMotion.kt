package com.przemas230.dietaapp.ui

import android.content.Context
import android.provider.Settings

/**
 * FR-50: Android's equivalent of the web app's `prefers-reduced-motion`
 * media query -- the system-wide "Remove animations" developer option
 * (Ustawienia -> Opcje programistyczne -> Skala animacji...) sets this to
 * 0 when the user wants no animations anywhere. There's no Compose-level
 * API for it (Compose animations aren't tied to the window animator scale
 * the way Activity transitions are), so call sites that author their own
 * AnimatedVisibility/animation specs check this explicitly and swap in
 * EnterTransition.None/ExitTransition.None.
 */
fun isReducedMotionEnabled(context: Context): Boolean = try {
    Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
} catch (e: Settings.SettingNotFoundException) {
    false
}
