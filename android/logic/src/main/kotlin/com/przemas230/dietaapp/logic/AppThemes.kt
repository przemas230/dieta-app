package com.przemas230.dietaapp.logic

/**
 * FR-48: one full palette -- a one-to-one port of index.html's per-theme
 * `:root[data-theme="..."]` custom-property blocks. Colors are `0xFFRRGGBB`
 * Longs (ready for `androidx.compose.ui.graphics.Color(Long)` in the `ui`
 * module, which this pure-Kotlin `logic` module deliberately doesn't depend
 * on -- see logic/build.gradle.kts). `isDark` mirrors the presence of
 * `color-scheme: dark` in that theme's CSS block (only "dark" and "berry").
 * Font pairs and per-theme animation easing curves are NOT ported yet --
 * see android/PARITY.md's FR-48 note for why.
 */
data class AppThemeDef(
    val id: String,
    val label: String,
    val swatch: Long,
    val metaColor: Long,
    val teal: Long,
    val tealDark: Long,
    val tealPale: Long,
    val honey: Long,
    val honeyPale: Long,
    val honeyDark: Long,
    val plum: Long,
    val plumPale: Long,
    val bg: Long,
    val card: Long,
    val text: Long,
    val muted: Long,
    val line: Long,
    val danger: Long,
    val dangerPale: Long,
    val starOff: Long,
    val isDark: Boolean,
)

object AppThemes {
    const val DEFAULT_ID = "teal"

    /** Same order as index.html's THEMES array, so the picker UI matches the web app. */
    val ALL: List<AppThemeDef> = listOf(
        AppThemeDef(
            id = "teal", label = "Zielony (domyślny)", swatch = 0xFF1F6B5C, metaColor = 0xFF1F6B5C,
            teal = 0xFF1F6B5C, tealDark = 0xFF123D34, tealPale = 0xFFEAF3EE,
            honey = 0xFFC98A3E, honeyPale = 0xFFFBF0DC, honeyDark = 0xFF8A6417,
            plum = 0xFF7C4F63, plumPale = 0xFFF3E9ED,
            bg = 0xFFFAF7F0, card = 0xFFFFFFFF, text = 0xFF22302B, muted = 0xFF64756C, line = 0xFFE3DECF,
            danger = 0xFFB54F3F, dangerPale = 0xFFF6E3E0, starOff = 0xFFD8D2C2, isDark = false,
        ),
        AppThemeDef(
            id = "light", label = "Jasny", swatch = 0xFF2C6FE0, metaColor = 0xFF2C6FE0,
            teal = 0xFF2C6FE0, tealDark = 0xFF1B4A99, tealPale = 0xFFE9F0FD,
            honey = 0xFFE0932E, honeyPale = 0xFFFDF1E1, honeyDark = 0xFF8A5817,
            plum = 0xFF7A5FC7, plumPale = 0xFFEFEBFB,
            bg = 0xFFF5F7FA, card = 0xFFFFFFFF, text = 0xFF1E2733, muted = 0xFF5D6B7A, line = 0xFFE2E7EE,
            danger = 0xFFD14343, dangerPale = 0xFFFBE5E5, starOff = 0xFFD7DCE3, isDark = false,
        ),
        AppThemeDef(
            id = "pink", label = "Różowy", swatch = 0xFFD6497B, metaColor = 0xFFD6497B,
            teal = 0xFFD6497B, tealDark = 0xFF93315A, tealPale = 0xFFFCE9F0,
            honey = 0xFFE0932E, honeyPale = 0xFFFDF1E1, honeyDark = 0xFF8A5817,
            plum = 0xFFA24F82, plumPale = 0xFFF6E7F0,
            bg = 0xFFFFF6FA, card = 0xFFFFFFFF, text = 0xFF3A2530, muted = 0xFF8A6C78, line = 0xFFF1D9E4,
            danger = 0xFFC4415A, dangerPale = 0xFFFBE3E8, starOff = 0xFFEAD3DC, isDark = false,
        ),
        AppThemeDef(
            id = "dark", label = "Ciemny", swatch = 0xFF151A18, metaColor = 0xFF0D211C,
            teal = 0xFF2F9078, tealDark = 0xFF8FE0C7, tealPale = 0xFF163830,
            honey = 0xFFE0A559, honeyPale = 0xFF3A2C15, honeyDark = 0xFFF0CE93,
            plum = 0xFF8C5D78, plumPale = 0xFF3A2A33,
            bg = 0xFF151A18, card = 0xFF1E2523, text = 0xFFE7EEE9, muted = 0xFF93A39B, line = 0xFF313B37,
            danger = 0xFFE5786B, dangerPale = 0xFF3A211D, starOff = 0xFF4A554F, isDark = true,
        ),
        AppThemeDef(
            id = "harvest", label = "Zbiory", swatch = 0xFFB5592E, metaColor = 0xFFB5592E,
            teal = 0xFFB5592E, tealDark = 0xFF7A3A1A, tealPale = 0xFFF5E4D8,
            honey = 0xFF7C8C3F, honeyPale = 0xFFEEF1DE, honeyDark = 0xFF4E5A26,
            plum = 0xFFA6763F, plumPale = 0xFFF2E6D3,
            bg = 0xFFFBF3E7, card = 0xFFFFFFFF, text = 0xFF3B2A1E, muted = 0xFF8A7462, line = 0xFFE8DCC8,
            danger = 0xFFB5432E, dangerPale = 0xFFF5E0DA, starOff = 0xFFE0D0BC, isDark = false,
        ),
        AppThemeDef(
            id = "citrus", label = "Cytrusowy", swatch = 0xFF4C9A2A, metaColor = 0xFF4C9A2A,
            teal = 0xFF4C9A2A, tealDark = 0xFF2E5F19, tealPale = 0xFFE8F3DE,
            honey = 0xFFF2A93B, honeyPale = 0xFFFEF2DC, honeyDark = 0xFF96650E,
            plum = 0xFFE85C4A, plumPale = 0xFFFBE3DE,
            bg = 0xFFFFFCF0, card = 0xFFFFFFFF, text = 0xFF2B3A1F, muted = 0xFF6B7A5A, line = 0xFFE9E8C9,
            danger = 0xFFD1483A, dangerPale = 0xFFFBE1DC, starOff = 0xFFE3E0B8, isDark = false,
        ),
        AppThemeDef(
            id = "mint", label = "Miętowy", swatch = 0xFF1B7A7A, metaColor = 0xFF1B7A7A,
            teal = 0xFF1B7A7A, tealDark = 0xFF0F4F4F, tealPale = 0xFFE3F3F1,
            honey = 0xFFE8916B, honeyPale = 0xFFFBEAE1, honeyDark = 0xFFA85A3B,
            plum = 0xFF5C7FB0, plumPale = 0xFFE7EDF6,
            bg = 0xFFF2FAF9, card = 0xFFFFFFFF, text = 0xFF1D3335, muted = 0xFF62807E, line = 0xFFDCEEEC,
            danger = 0xFFD1594A, dangerPale = 0xFFFAE3DE, starOff = 0xFFCFE6E3, isDark = false,
        ),
        AppThemeDef(
            id = "berry", label = "Jagodowa noc", swatch = 0xFF7A2F55, metaColor = 0xFF0D0710,
            teal = 0xFF7A2F55, tealDark = 0xFFF0A9C6, tealPale = 0xFF341022,
            honey = 0xFFD9A94E, honeyPale = 0xFF3A2C15, honeyDark = 0xFFF0CE93,
            plum = 0xFF9B7FC7, plumPale = 0xFF2A2438,
            bg = 0xFF170F16, card = 0xFF241A24, text = 0xFFF0E6ED, muted = 0xFFB39AAE, line = 0xFF3A2E38,
            danger = 0xFFE5786B, dangerPale = 0xFF3A211D, starOff = 0xFF4A3A46, isDark = true,
        ),
        AppThemeDef(
            id = "polaroid", label = "Polaroid", swatch = 0xFFC1503A, metaColor = 0xFFC1503A,
            teal = 0xFFC1503A, tealDark = 0xFF8A3323, tealPale = 0xFFF3E3DC,
            honey = 0xFFD9A73B, honeyPale = 0xFFFBF1DC, honeyDark = 0xFF8C6A1C,
            plum = 0xFF4A6B7A, plumPale = 0xFFE7EEF0,
            bg = 0xFFECE7DC, card = 0xFFFFFFFF, text = 0xFF2E2A22, muted = 0xFF8A8272, line = 0xFFD9D2C0,
            danger = 0xFFB5432E, dangerPale = 0xFFF5DFDA, starOff = 0xFFDCD5C2, isDark = false,
        ),
        AppThemeDef(
            id = "fluent", label = "Fluent", swatch = 0xFF0F6CBD, metaColor = 0xFF0F6CBD,
            teal = 0xFF0F6CBD, tealDark = 0xFF0A4A82, tealPale = 0xFFE8F1FA,
            honey = 0xFF8764B8, honeyPale = 0xFFF1ECFA, honeyDark = 0xFF5C4189,
            plum = 0xFF5B8DB8, plumPale = 0xFFEAF1F7,
            bg = 0xFFF3F3F3, card = 0xFFFFFFFF, text = 0xFF1B1B1B, muted = 0xFF5C5C5C, line = 0xFFE0E0E0,
            danger = 0xFFC42B1C, dangerPale = 0xFFFDE7E4, starOff = 0xFFD6D6D6, isDark = false,
        ),
        AppThemeDef(
            id = "metro", label = "Kafelki", swatch = 0xFF2D89EF, metaColor = 0xFF2D89EF,
            teal = 0xFF2D89EF, tealDark = 0xFF1B5FAD, tealPale = 0xFFE3EFFC,
            honey = 0xFFD80073, honeyPale = 0xFFFCE3EF, honeyDark = 0xFF9C0053,
            plum = 0xFF00ABA9, plumPale = 0xFFE0F5F4,
            bg = 0xFFF0F0F0, card = 0xFFFFFFFF, text = 0xFF1D1D1D, muted = 0xFF6B6B6B, line = 0xFFDADADA,
            danger = 0xFFE51400, dangerPale = 0xFFFBE0DC, starOff = 0xFFD2D2D2, isDark = false,
        ),
    )

    fun byId(id: String): AppThemeDef = ALL.find { it.id == id } ?: ALL.first { it.id == DEFAULT_ID }
}
