package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HeaderScrollBehaviorTest {
    @Test
    fun `scrolledDown is true when the item index advances`() {
        assertTrue(HeaderScrollBehavior.scrolledDown(prevIndex = 0, prevOffset = 100, currIndex = 1, currOffset = 0))
        assertFalse(HeaderScrollBehavior.scrolledDown(prevIndex = 1, prevOffset = 0, currIndex = 0, currOffset = 100))
    }

    @Test
    fun `scrolledDown is true when the same index's offset grows`() {
        assertTrue(HeaderScrollBehavior.scrolledDown(prevIndex = 2, prevOffset = 50, currIndex = 2, currOffset = 120))
        assertFalse(HeaderScrollBehavior.scrolledDown(prevIndex = 2, prevOffset = 120, currIndex = 2, currOffset = 50))
    }

    @Test
    fun `scrolledUp is the mirror image of scrolledDown`() {
        assertTrue(HeaderScrollBehavior.scrolledUp(prevIndex = 1, prevOffset = 0, currIndex = 0, currOffset = 100))
        assertTrue(HeaderScrollBehavior.scrolledUp(prevIndex = 2, prevOffset = 120, currIndex = 2, currOffset = 50))
        assertFalse(HeaderScrollBehavior.scrolledUp(prevIndex = 0, prevOffset = 100, currIndex = 1, currOffset = 0))
    }

    @Test
    fun `no movement is neither up nor down`() {
        assertFalse(HeaderScrollBehavior.scrolledDown(prevIndex = 1, prevOffset = 40, currIndex = 1, currOffset = 40))
        assertFalse(HeaderScrollBehavior.scrolledUp(prevIndex = 1, prevOffset = 40, currIndex = 1, currOffset = 40))
    }

    @Test
    fun `isNearTop requires both the first item and a small offset`() {
        assertTrue(HeaderScrollBehavior.isNearTop(index = 0, offset = 0, nearTopPx = 60))
        assertTrue(HeaderScrollBehavior.isNearTop(index = 0, offset = 60, nearTopPx = 60))
        assertFalse(HeaderScrollBehavior.isNearTop(index = 0, offset = 61, nearTopPx = 60))
        assertFalse(HeaderScrollBehavior.isNearTop(index = 1, offset = 0, nearTopPx = 60))
    }
}
