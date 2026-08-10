package com.przemas230.dietaapp.logic

import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DishIdeaGeneratorTest {

    @Test
    fun `returns null with fewer than 2 favorites`() {
        assertNull(DishIdeaGenerator.generate("obiady", emptySet()))
        assertNull(DishIdeaGenerator.generate("obiady", setOf("cebula")))
    }

    @Test
    fun `combines two favorites into a templated name`() {
        val idea = DishIdeaGenerator.generate("sniadania", setOf("jajka", "awokado"), Random(1))
        assertNotNull(idea)
        assertTrue(idea!!.name.contains(idea.aClean))
        assertTrue(idea.name.contains(idea.bClean))
        assertTrue(setOf(idea.aClean, idea.bClean) == setOf("jajka", "awokado"))
    }

    @Test
    fun `unknown category falls back to obiady templates`() {
        val idea = DishIdeaGenerator.generate("nieznana", setOf("a", "b"), Random(1))
        assertNotNull(idea)
        // obiady templates all contain "z {a}" style wording distinct enough to not crash -- just assert it picked a non-blank name.
        assertTrue(idea!!.name.isNotBlank())
    }

    @Test
    fun `is deterministic for a fixed seed`() {
        val favorites = setOf("cebula", "pomidor", "ser")
        val a = DishIdeaGenerator.generate("obiady", favorites, Random(99))
        val b = DishIdeaGenerator.generate("obiady", favorites, Random(99))
        assertEquals(a, b)
    }
}
