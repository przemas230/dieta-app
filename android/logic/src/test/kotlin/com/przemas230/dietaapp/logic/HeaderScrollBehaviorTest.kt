package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HeaderScrollBehaviorTest {
    @Test
    fun `isNearTop requires both the first item and a small offset`() {
        assertTrue(HeaderScrollBehavior.isNearTop(index = 0, offset = 0, nearTopPx = 60))
        assertTrue(HeaderScrollBehavior.isNearTop(index = 0, offset = 60, nearTopPx = 60))
        assertFalse(HeaderScrollBehavior.isNearTop(index = 0, offset = 61, nearTopPx = 60))
        assertFalse(HeaderScrollBehavior.isNearTop(index = 1, offset = 0, nearTopPx = 60))
    }
}
