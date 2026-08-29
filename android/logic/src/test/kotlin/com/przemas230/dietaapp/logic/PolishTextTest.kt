package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * FR-2/v6 (ported to Android 2026-08-29): searching without Polish
 * diacritics has to find the diacritic-carrying names, in all four search
 * boxes that used to compare raw lowercased strings.
 */
class PolishTextTest {
    @Test
    fun `searchKey folds every Polish diacritic and lowercases`() {
        assertEquals("zolty ser", PolishText.searchKey("Żółty ser"))
        assertEquals("brokul", PolishText.searchKey("Brokuł"))
        assertEquals("acelnoszz", PolishText.searchKey("ąćęłńóśźż"))
    }

    @Test
    fun `a query typed without diacritics finds the real name`() {
        assertTrue(PolishText.contains("żółty ser", "zolty"))
        assertTrue(PolishText.contains("Brokuł na parze", "brokul"))
        assertTrue(PolishText.startsWith("Żurek staropolski", "zurek"))
    }

    @Test
    fun `a query typed WITH diacritics still finds it`() {
        assertTrue(PolishText.contains("zolty ser", "żółty"))
        assertTrue(PolishText.contains("Żółty ser", "żółty"))
    }

    @Test
    fun `unrelated text still does not match`() {
        assertFalse(PolishText.contains("żółty ser", "mleko"))
    }

    @Test
    fun `text without diacritics is returned untouched apart from case`() {
        // The fast path -- worth pinning down, since this runs over the whole
        // recipe list on every keystroke.
        assertEquals("mleko 2%", PolishText.searchKey("Mleko 2%"))
    }
}
