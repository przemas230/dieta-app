package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PantryDisplayTest {
    @Test
    fun `polishPluralCategory picks one for 1 and -1`() {
        assertEquals("one", PantryDisplay.polishPluralCategory(1))
        assertEquals("one", PantryDisplay.polishPluralCategory(-1))
    }

    @Test
    fun `polishPluralCategory picks few for 2-4 except the 12-14 exception`() {
        assertEquals("few", PantryDisplay.polishPluralCategory(2))
        assertEquals("few", PantryDisplay.polishPluralCategory(3))
        assertEquals("few", PantryDisplay.polishPluralCategory(4))
        assertEquals("few", PantryDisplay.polishPluralCategory(22))
        assertEquals("few", PantryDisplay.polishPluralCategory(24))
        assertEquals("many", PantryDisplay.polishPluralCategory(12))
        assertEquals("many", PantryDisplay.polishPluralCategory(13))
        assertEquals("many", PantryDisplay.polishPluralCategory(14))
        assertEquals("many", PantryDisplay.polishPluralCategory(112))
    }

    @Test
    fun `polishPluralCategory picks many for 0, 5-21, and 25+`() {
        assertEquals("many", PantryDisplay.polishPluralCategory(0))
        assertEquals("many", PantryDisplay.polishPluralCategory(5))
        assertEquals("many", PantryDisplay.polishPluralCategory(11))
        assertEquals("many", PantryDisplay.polishPluralCategory(21))
        assertEquals("many", PantryDisplay.polishPluralCategory(25))
    }

    @Test
    fun `displayName agrees jajka with the given quantity`() {
        assertEquals("jajko", PantryDisplay.displayName("jajka", 1))
        assertEquals("jajka", PantryDisplay.displayName("jajka", 3))
        assertEquals("jajek", PantryDisplay.displayName("jajka", 5))
        assertEquals("jajek", PantryDisplay.displayName("jajka", 13))
    }

    @Test
    fun `displayName with a null quantity returns the singular form`() {
        assertEquals("jajko", PantryDisplay.displayName("jajka", null))
    }

    @Test
    fun `displayName falls back to the canon name unchanged when not in the table`() {
        assertEquals("nieznany-produkt", PantryDisplay.displayName("nieznany-produkt", 5))
    }

    @Test
    fun `isCountUnit recognizes szt variants and rejects weight-volume units`() {
        assertEquals(true, PantryDisplay.isCountUnit("szt."))
        assertEquals(true, PantryDisplay.isCountUnit("Szt"))
        assertEquals(false, PantryDisplay.isCountUnit("g"))
        assertEquals(false, PantryDisplay.isCountUnit("ml"))
    }
}
