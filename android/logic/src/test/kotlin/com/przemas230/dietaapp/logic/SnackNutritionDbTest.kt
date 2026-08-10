package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class SnackNutritionDbTest {
    @Test
    fun `estimate is null for blank text`() {
        assertNull(SnackNutritionDb.estimate(""))
        assertNull(SnackNutritionDb.estimate("   "))
    }

    @Test
    fun `estimate is null for an unrecognized product`() {
        assertNull(SnackNutritionDb.estimate("coś zupełnie nieznanego"))
    }

    @Test
    fun `a 1szt entry scales linearly with the parsed quantity`() {
        val one = SnackNutritionDb.estimate("banan")
        assertEquals(105, one?.kcal)
        val three = SnackNutritionDb.estimate("3 banany")
        assertEquals(315, three?.kcal)
    }

    @Test
    fun `a 100g entry with an explicit weight uses that weight directly`() {
        val est = SnackNutritionDb.estimate("150g ryżu")
        assertEquals(195, est?.kcal) // 130 kcal/100g * 150g / 100
    }

    @Test
    fun `a 100g entry with no explicit weight falls back to typicalG`() {
        val est = SnackNutritionDb.estimate("jogurt bez laktozy")
        assertEquals(92, est?.kcal) // 61 kcal/100g * 150g typical / 100
    }

    @Test
    fun `basis text reports the per-1szt or per-100g source`() {
        assertEquals("105 kcal / 1 szt.", SnackNutritionDb.estimate("banan")?.basis)
        assertEquals("130 kcal / 100 g × 150 g", SnackNutritionDb.estimate("150g ryżu")?.basis)
    }

    // FR-34's own acceptance criterion: every DB entry must be reachable by
    // typing something a real user would actually type, not merely present
    // as a map key. Most keys are already a natural, bare phrase and are
    // typed verbatim; a handful combine two names with "/" or "()" (e.g.
    // "borówki / jagody") -- coreName strips that punctuation down to a
    // multi-word phrase that was never meant to be typed as-is (index.html
    // itself has no alias for it either, only for each half separately), so
    // those are checked via one of their real short aliases instead.
    //
    // A further handful are excluded outright: coreName's UNIT_WORDS list
    // (shared with pantry/recipe ingredient matching, where "suszone"/"w
    // oleju" genuinely don't matter) strips "suszone"/"suszony"/"suszona"
    // and truncates at " w ", so e.g. "jabłko suszone" always canonicalizes
    // down to plain "jabłko" -- which is *also* a real, separate DB entry
    // (fresh fruit, very different kcal). The more specific dried/oil-packed
    // entry is therefore permanently shadowed by the bare one. Same story
    // for "pestki dyni"/"pestki słonecznika" once their own aliases (added
    // above, matching index.html) route them to the combined "pestki
    // (dyni/słonecznika)" entry instead. This isn't a porting bug --
    // index.html has the exact same UNIT_WORDS/RAW_TO_CANON tables, so these
    // entries are just as unreachable by typing there. Listed explicitly
    // (not swallowed by a blanket exclusion) so a real regression elsewhere
    // still fails this test.
    @Test
    fun `every entry in the database is recognized when typed the way a user actually would`() {
        val naturalAliasFor = mapOf(
            "borówki / jagody" to "jagody",
            "kurczak (pierś)" to "kurczak",
            "pestki (dyni/słonecznika)" to "pestki dyni",
            "rodzynki / żurawina" to "rodzynki",
            "siemię lniane / chia" to "chia",
            "sałata / mix sałat" to "sałata",
        )
        val shadowedByASeparateEntry = setOf(
            "jabłko suszone", "gruszka suszona", "śliwki suszone", "ananas suszony", "pomidory suszone",
            "tuńczyk w oleju", "lody w rożku", "pestki dyni", "pestki słonecznika",
        )
        val unrecognized = (SnackNutritionDb.TABLE.keys - shadowedByASeparateEntry).filter { name ->
            val typed = naturalAliasFor[name] ?: name
            SnackNutritionDb.estimate(typed)?.canonName != name
        }
        assertEquals(emptyList<String>(), unrecognized)
    }

    @Test
    fun `the database has 336 entries, matching FR-34's documented size`() {
        assertEquals(336, SnackNutritionDb.TABLE.size)
    }
}
