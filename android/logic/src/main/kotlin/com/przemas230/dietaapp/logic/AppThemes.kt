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
    // Requested 2026-08-25 (Web FR-87/v8, ported here): "Klinika" is now the
    // default theme for fresh installs -- matches index.html's `theme:"clinic"`
    // default (was "teal" on both platforms before).
    const val DEFAULT_ID = "clinic"

    /** Same order as index.html's THEMES array, so the picker UI matches the web app. */
    val ALL: List<AppThemeDef> = listOf(
        AppThemeDef(
            id = "teal", label = "Zielony (domyślny)", swatch = 0xFF1B5E3F, metaColor = 0xFF1B5E3F,
            teal = 0xFF1B5E3F, tealDark = 0xFF123423, tealPale = 0xFFE7F2EC,
            honey = 0xFFF5A623, honeyPale = 0xFFFDF0DC, honeyDark = 0xFF8A5C10,
            plum = 0xFF7C4F63, plumPale = 0xFFF3E9ED,
            bg = 0xFFF7F6F2, card = 0xFFFFFFFF, text = 0xFF22302B, muted = 0xFF64756C, line = 0xFFE3DECF,
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
            id = "dark", label = "Ciemny", swatch = 0xFF0D0D0D, metaColor = 0xFF1B5E3F,
            teal = 0xFF1B5E3F, tealDark = 0xFF8FE0C7, tealPale = 0xFF163830,
            honey = 0xFFF5A623, honeyPale = 0xFF3A2C15, honeyDark = 0xFFF0CE93,
            plum = 0xFF8C5D78, plumPale = 0xFF3A2A33,
            bg = 0xFF0D0D0D, card = 0xFF1A1A1A, text = 0xFFF2F2F2, muted = 0xFFA0A0A0, line = 0xFF2A2A2A,
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
        // FR-87: motyw "Klinika" -- jedyny, ktory ma tez wlasny font i uklad
        // (nie tylko paleta), patrz android/app/.../ui/theme/ClinicTheme.kt.
        // Paleta v2 (2026-08-23): przestrojona na dokladny odpowiednik "sage +
        // stone" z diet-chef-pro-75 (Lovable) -- kazda wartosc ponizej to
        // realny OKLCH token z tamtego projektu (src/styles.css), przeliczony
        // na sRGB (patrz uwaga w PARITY.md). honey/plum nie maja tam wprost
        // odpowiednika (Lovable jest celowo niemal monochromatyczne: kremowe
        // tlo + jedna zielen szalwiowa) -- dobrane jako stonowane, spojne z
        // reszta palety warianty (ciepla glina / przygaszony blekit) tylko po
        // to, zeby dwie kategorie posilkow obok siebie dalej dalo sie odroznic.
        AppThemeDef(
            id = "clinic", label = "Klinika", swatch = 0xFF6DA480, metaColor = 0xFF6DA480,
            teal = 0xFF6DA480, tealDark = 0xFF346B49, tealPale = 0xFFD9EEDF,
            honey = 0xFFC69B78, honeyPale = 0xFFF8E8DB, honeyDark = 0xFF7E522C,
            plum = 0xFF7A90B4, plumPale = 0xFFDDE5F2,
            bg = 0xFFF9F7F5, card = 0xFFFFFFFF, text = 0xFF1E1B16, muted = 0xFF766B59, line = 0xFFE3DFD8,
            danger = 0xFFE7000B, dangerPale = 0xFFFFE2DD, starOff = 0xFFC9C3BA, isDark = false,
        ),
        // FR-87/v2: "Klinika (noc)" -- dark-mode sibling of "clinic", on
        // explicit request ("zrob klinika dzien i noc motyw taki jak w
        // propozycji"). Same sage accent as the light version (diet-chef-
        // pro-75's own .dark block leaves --primary untouched too), every
        // other value swapped for that project's dark OKLCH tokens --
        // container/text roles inverted the same way "dark"/"berry" already
        // do it above (dark container bg, LIGHT text on top of it).
        AppThemeDef(
            id = "clinic_dark", label = "Klinika (noc)", swatch = 0xFF6DA480, metaColor = 0xFF6DA480,
            teal = 0xFF6DA480, tealDark = 0xFFB0DABD, tealPale = 0xFF1D3425,
            honey = 0xFFC69B78, honeyPale = 0xFF392A1E, honeyDark = 0xFFE7C7AE,
            plum = 0xFF7A90B4, plumPale = 0xFF252E3D,
            bg = 0xFF1E1B16, card = 0xFF2B2823, text = 0xFFFEFAF3, muted = 0xFFABA397, line = 0xFF36322D,
            danger = 0xFFFF6467, dangerPale = 0xFF421B1B, starOff = 0xFF534C41, isDark = true,
        ),
        // Requested 2026-08-26 ("dodaj też kilka innych schematów
        // kolorystycznych dla motywów klinika i klinika noc") -- two more
        // Klinika-family looks, day+night each. Same hex values as
        // index.html's [data-clinic-palette="ocean"/"terracotta"] CSS
        // blocks, so the two platforms actually match. Plain new AppThemeDef
        // entries (not a separate "palette" attribute like the web CSS
        // needed) since Android's theme system is already one flat,
        // independently-swappable list -- no structural selectors to
        // duplicate here, unlike index.html's dozens of
        // :is([data-theme="clinic"], [data-theme="clinic_dark"]) rules.
        AppThemeDef(
            id = "clinic_ocean", label = "Klinika Ocean", swatch = 0xFF2E86AB, metaColor = 0xFF2E86AB,
            teal = 0xFF2E86AB, tealDark = 0xFF1B5A73, tealPale = 0xFFD6EAF2,
            honey = 0xFF5FA8A0, honeyPale = 0xFFDFF2EF, honeyDark = 0xFF2F6E67,
            plum = 0xFF5C7FA6, plumPale = 0xFFDCE6F2,
            bg = 0xFFF4F8FA, card = 0xFFFFFFFF, text = 0xFF132630, muted = 0xFF5C7480, line = 0xFFDCE6EA,
            danger = 0xFFE7000B, dangerPale = 0xFFFFE2DD, starOff = 0xFFC4D3D8, isDark = false,
        ),
        AppThemeDef(
            id = "clinic_ocean_dark", label = "Klinika Ocean (noc)", swatch = 0xFF4FB3D9, metaColor = 0xFF4FB3D9,
            teal = 0xFF4FB3D9, tealDark = 0xFFBEE6F2, tealPale = 0xFF173947,
            honey = 0xFF5FA8A0, honeyPale = 0xFF1B3B37, honeyDark = 0xFF9AD6CC,
            plum = 0xFF7FA0C7, plumPale = 0xFF22303F,
            bg = 0xFF0F1B21, card = 0xFF16262E, text = 0xFFEAF4F7, muted = 0xFF9CB3BC, line = 0xFF223339,
            danger = 0xFFFF6467, dangerPale = 0xFF421B1B, starOff = 0xFF42565C, isDark = true,
        ),
        AppThemeDef(
            id = "clinic_terracotta", label = "Klinika Terakota", swatch = 0xFFC1613F, metaColor = 0xFFC1613F,
            teal = 0xFFC1613F, tealDark = 0xFF8A3F23, tealPale = 0xFFF3DED3,
            honey = 0xFFD4A24C, honeyPale = 0xFFF7EAD2, honeyDark = 0xFF8C6420,
            plum = 0xFFA8785F, plumPale = 0xFFEDE0D8,
            bg = 0xFFFBF6F1, card = 0xFFFFFFFF, text = 0xFF2B1E16, muted = 0xFF8A7768, line = 0xFFE8DCD0,
            danger = 0xFFE7000B, dangerPale = 0xFFFFE2DD, starOff = 0xFFD8C9BC, isDark = false,
        ),
        AppThemeDef(
            id = "clinic_terracotta_dark", label = "Klinika Terakota (noc)", swatch = 0xFFE08556, metaColor = 0xFFE08556,
            teal = 0xFFE08556, tealDark = 0xFFF5C7A8, tealPale = 0xFF3A2117,
            honey = 0xFFD4A24C, honeyPale = 0xFF332510, honeyDark = 0xFFEBC985,
            plum = 0xFFC29C86, plumPale = 0xFF2E2620,
            bg = 0xFF1D1611, card = 0xFF29201A, text = 0xFFF7EEE6, muted = 0xFFB8A594, line = 0xFF382C24,
            danger = 0xFFFF6467, dangerPale = 0xFF421B1B, starOff = 0xFF54453A, isDark = true,
        ),
    )

    fun byId(id: String): AppThemeDef = ALL.find { it.id == id } ?: ALL.first { it.id == DEFAULT_ID }

    /**
     * FR-87/v2: all Klinika variants ("clinic"/"clinic_dark" plus the
     * 2026-08-26 "ocean"/"terracotta" siblings) share the same font/shape/
     * layout treatment (see ClinicTheme.kt and every
     * `LocalDietaThemeId.current`-branching screen) -- centralized here so a
     * future new Klinika variant only needs updating in one place instead of
     * at every one of those call sites.
     */
    fun isClinicFamily(themeId: String): Boolean = themeId in CLINIC_FAMILY_IDS

    private val CLINIC_FAMILY_IDS = setOf(
        "clinic", "clinic_dark",
        "clinic_ocean", "clinic_ocean_dark",
        "clinic_terracotta", "clinic_terracotta_dark",
    )
}
