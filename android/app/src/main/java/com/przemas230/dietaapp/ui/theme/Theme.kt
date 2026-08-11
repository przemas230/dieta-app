package com.przemas230.dietaapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.przemas230.dietaapp.logic.AppThemeDef
import com.przemas230.dietaapp.logic.AppThemes

/**
 * FR-48: which theme id (`AppThemes.ALL`) is currently active -- read by
 * FR-49/FR-63's per-theme card shape (RecipeCard in RecipeListScreen.kt)
 * without threading a themeId parameter through every screen that doesn't
 * otherwise need it.
 */
val LocalDietaThemeId = compositionLocalOf { AppThemes.DEFAULT_ID }

/**
 * FR-48: one-to-one port of index.html's CSS custom-property -> Material3
 * ColorScheme mapping. `--teal` is the header/primary-button background with
 * white foreground text in every theme (see index.html's `header{...
 * color:#fff}` / `.btn.primary{background:var(--teal);color:#fff}`), so
 * onPrimary/onSecondary/onTertiary/onError are always white, independent of
 * light vs. dark theme. Font pairs and per-theme animation easing curves are
 * NOT ported -- see android/PARITY.md's FR-48 note.
 */
private fun colorSchemeFor(def: AppThemeDef): ColorScheme {
    val primary = Color(def.teal)
    val onPrimary = Color.White
    val primaryContainer = Color(def.tealPale)
    val onPrimaryContainer = Color(def.tealDark)
    val secondary = Color(def.honey)
    val onSecondary = Color.White
    val secondaryContainer = Color(def.honeyPale)
    val onSecondaryContainer = Color(def.honeyDark)
    val tertiary = Color(def.plum)
    val onTertiary = Color.White
    val tertiaryContainer = Color(def.plumPale)
    val onTertiaryContainer = Color(def.plum)
    val background = Color(def.bg)
    val onBackground = Color(def.text)
    val surface = Color(def.card)
    val onSurface = Color(def.text)
    val surfaceVariant = Color(def.line)
    val onSurfaceVariant = Color(def.muted)
    val outline = Color(def.line)
    val error = Color(def.danger)
    val onError = Color.White
    val errorContainer = Color(def.dangerPale)
    val onErrorContainer = Color(def.danger)

    return if (def.isDark) {
        darkColorScheme(
            primary = primary, onPrimary = onPrimary, primaryContainer = primaryContainer, onPrimaryContainer = onPrimaryContainer,
            secondary = secondary, onSecondary = onSecondary, secondaryContainer = secondaryContainer, onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary, onTertiary = onTertiary, tertiaryContainer = tertiaryContainer, onTertiaryContainer = onTertiaryContainer,
            background = background, onBackground = onBackground,
            surface = surface, onSurface = onSurface,
            surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            error = error, onError = onError, errorContainer = errorContainer, onErrorContainer = onErrorContainer,
        )
    } else {
        lightColorScheme(
            primary = primary, onPrimary = onPrimary, primaryContainer = primaryContainer, onPrimaryContainer = onPrimaryContainer,
            secondary = secondary, onSecondary = onSecondary, secondaryContainer = secondaryContainer, onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary, onTertiary = onTertiary, tertiaryContainer = tertiaryContainer, onTertiaryContainer = onTertiaryContainer,
            background = background, onBackground = onBackground,
            surface = surface, onSurface = onSurface,
            surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            error = error, onError = onError, errorContainer = errorContainer, onErrorContainer = onErrorContainer,
        )
    }
}

@Composable
fun DietaAppTheme(themeId: String = AppThemes.DEFAULT_ID, content: @Composable () -> Unit) {
    val def = remember(themeId) { AppThemes.byId(themeId) }
    CompositionLocalProvider(LocalDietaThemeId provides def.id) {
        MaterialTheme(
            colorScheme = colorSchemeFor(def),
            typography = Typography,
            // Faza 1 planu wizualnego dopracowania (2026-08-11) -- patrz Shapes.kt.
            shapes = AppShapes,
            content = content,
        )
    }
}
