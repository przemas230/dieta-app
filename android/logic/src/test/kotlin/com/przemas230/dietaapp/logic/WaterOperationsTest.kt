package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WaterOperationsTest {
    @Test
    fun `tapping a droplet above the current level sets the count to its position`() {
        assertEquals(1, WaterOperations.tapDroplet(current = 0, index = 0))
        assertEquals(6, WaterOperations.tapDroplet(current = 3, index = 5))
        assertEquals(8, WaterOperations.tapDroplet(current = 0, index = 7))
    }

    @Test
    fun `tapping the droplet exactly at the current level steps back by one`() {
        assertEquals(0, WaterOperations.tapDroplet(current = 1, index = 0))
        assertEquals(4, WaterOperations.tapDroplet(current = 5, index = 4))
    }

    @Test
    fun `tapping a droplet below the current level jumps down to its position, not just back one`() {
        assertEquals(2, WaterOperations.tapDroplet(current = 6, index = 1))
    }
}
