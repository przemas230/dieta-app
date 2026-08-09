package com.przemas230.dietaapp.logic

import kotlin.test.Test
import kotlin.test.assertEquals

class UiScaleTest {
    @Test
    fun `wide screens (420dp or more) get full scale`() {
        assertEquals(1.0, UiScale.detectDefault(420))
        assertEquals(1.0, UiScale.detectDefault(500))
    }

    @Test
    fun `narrow screens (360dp or less) get the minimum 0-75`() {
        assertEquals(0.75, UiScale.detectDefault(360))
        assertEquals(0.75, UiScale.detectDefault(300))
    }

    @Test
    fun `mid-range width interpolates and rounds to the nearest 0-05 step`() {
        assertEquals(0.9, UiScale.detectDefault(390))
        assertEquals(0.9, UiScale.detectDefault(400))
        assertEquals(0.85, UiScale.detectDefault(380))
    }
}
