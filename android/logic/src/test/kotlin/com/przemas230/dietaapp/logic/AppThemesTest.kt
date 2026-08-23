package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AppThemesTest {

    @Test
    fun `has the 11 themes from index_html in the same order, plus the Android-only Klinika day and night themes`() {
        // FR-87: "clinic"/"clinic_dark" are a deliberate, documented
        // exception to the otherwise 1:1 index.html parity (see
        // android/PARITY.md's FR-87 note) -- the only themes with their own
        // font/shape/layout treatment, not just a ported palette, so they
        // don't have a web counterpart (yet). Every other id below must
        // still match index.html exactly.
        val ids = AppThemes.ALL.map { it.id }
        assertEquals(
            listOf("teal", "light", "pink", "dark", "harvest", "citrus", "mint", "berry", "polaroid", "fluent", "metro", "clinic", "clinic_dark"),
            ids,
        )
    }

    @Test
    fun `byId falls back to the default teal theme for an unknown id`() {
        assertEquals("teal", AppThemes.byId("nope").id)
        assertEquals(AppThemes.DEFAULT_ID, AppThemes.byId("nope").id)
    }

    @Test
    fun `byId finds a known theme`() {
        assertEquals("metro", AppThemes.byId("metro").id)
    }

    @Test
    fun `spot-checked hex values match index_html's CSS custom properties`() {
        val teal = AppThemes.byId("teal")
        assertEquals(0xFF1B5E3FL, teal.teal)
        assertEquals(0xFFF7F6F2L, teal.bg)

        val dark = AppThemes.byId("dark")
        assertEquals(0xFF0D0D0DL, dark.bg)
        assertEquals(0xFF1B5E3FL, dark.teal)
        assertEquals(0xFF1B5E3FL, dark.metaColor)

        val berry = AppThemes.byId("berry")
        assertEquals(0xFF241A24L, berry.card)
        assertEquals(0xFF0D0710L, berry.metaColor)

        val metro = AppThemes.byId("metro")
        assertEquals(0xFFD80073L, metro.honey)
        assertEquals(0xFF00ABA9L, metro.plum)

        val fluent = AppThemes.byId("fluent")
        assertEquals(0xFF5B8DB8L, fluent.plum)

        val polaroid = AppThemes.byId("polaroid")
        assertEquals(0xFF2E2A22L, polaroid.text)
    }

    @Test
    fun `only dark, berry and clinic_dark are marked as dark themes`() {
        val darkIds = AppThemes.ALL.filter { it.isDark }.map { it.id }
        assertEquals(listOf("dark", "berry", "clinic_dark"), darkIds)
        assertFalse(AppThemes.byId("teal").isDark)
        assertTrue(AppThemes.byId("dark").isDark)
    }

    @Test
    fun `isClinicFamily recognizes both Klinika variants and nothing else`() {
        assertTrue(AppThemes.isClinicFamily("clinic"))
        assertTrue(AppThemes.isClinicFamily("clinic_dark"))
        assertFalse(AppThemes.isClinicFamily("dark"))
        assertFalse(AppThemes.isClinicFamily("teal"))
    }

    @Test
    fun `dark theme's swatch is its background, not its teal accent, unlike every other theme`() {
        val dark = AppThemes.byId("dark")
        assertEquals(dark.bg, dark.swatch)
        AppThemes.ALL.filter { it.id != "dark" }.forEach { theme ->
            assertEquals(theme.teal, theme.swatch, "theme '${theme.id}' expected swatch == teal")
        }
    }
}
