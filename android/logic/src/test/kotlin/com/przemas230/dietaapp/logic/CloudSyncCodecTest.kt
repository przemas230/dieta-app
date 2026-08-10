package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.data.SpiceLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CloudSyncCodecTest {

    @Test
    fun `profile round-trips through encode-decode`() {
        val profile = Profile(
            sex = Sex.MEZCZYZNA, age = 42, heightCm = 180, weightKg = 82.5, targetWeightKg = 78.0,
            activity = ActivityLevel.BARDZO_AKTYWNY, goal = Goal.BUDOWANIE,
            glutenFree = true, lactoseFree = false, strictLowGI = false, configured = true,
        )
        val decoded = CloudSyncCodec.decodeProfile(CloudSyncCodec.encodeProfile(profile))
        assertEquals(profile, decoded)
    }

    @Test
    fun `decodeProfile falls back to defaults for missing fields and null for missing map`() {
        assertNull(CloudSyncCodec.decodeProfile(null))
        val partial = CloudSyncCodec.decodeProfile(mapOf("age" to 30L))
        assertEquals(30, partial?.age)
        assertEquals(Profile().heightCm, partial?.heightCm)
    }

    @Test
    fun `decodeProfile handles Firestore Long instead of Int for numeric fields`() {
        // Firestore's SDK commonly returns whole numbers as java.lang.Long, not Int/Double.
        val map = mapOf("age" to 25L, "heightCm" to 170L, "weightKg" to 65L, "targetWeightKg" to 60L)
        val decoded = CloudSyncCodec.decodeProfile(map)
        assertEquals(25, decoded?.age)
        assertEquals(170, decoded?.heightCm)
        assertEquals(65.0, decoded?.weightKg)
        assertEquals(60.0, decoded?.targetWeightKg)
    }

    @Test
    fun `decodeProfile ignores an unrecognized enum value and falls back to default`() {
        val decoded = CloudSyncCodec.decodeProfile(mapOf("sex" to "NIEZNANE"))
        assertEquals(Sex.KOBIETA, decoded?.sex)
    }

    @Test
    fun `pantry round-trips a product and a spice through encode-decode`() {
        val pantry = mapOf(
            "mąka" to PantryItem.Product("mąka", PantryCategory.ZBOZOWE, 500.0, "g"),
            "sól" to PantryItem.Spice("sól", PantryCategory.PRZYPRAWY, SpiceLevel.WYSTARCZY),
        )
        val decoded = CloudSyncCodec.decodePantry(CloudSyncCodec.encodePantry(pantry))
        assertEquals(pantry, decoded)
    }

    @Test
    fun `decodePantry skips an entry with an unrecognized type and keeps the rest`() {
        val map = mapOf(
            "dobry" to mapOf("type" to "product", "category" to "WARZYWA", "quantity" to 1.0, "unit" to "szt."),
            "zly" to mapOf("type" to "cos-innego"),
        )
        val decoded = CloudSyncCodec.decodePantry(map)
        assertEquals(1, decoded?.size)
        assertEquals("dobry", decoded?.keys?.first())
    }

    @Test
    fun `decodePantry returns null for a missing map`() {
        assertNull(CloudSyncCodec.decodePantry(null))
    }

    @Test
    fun `encodeAll produces the expected top-level keys`() {
        val data = CloudSyncCodec.encodeAll(
            displayName = "Przemek",
            profile = Profile(),
            pantry = emptyMap(),
            themeId = "metro",
            uiScale = 1.1,
            swipeRatingStyle = "GLOW",
        )
        assertEquals(setOf("displayName", "profile", "pantry", "theme", "uiScale", "swipeRatingStyle"), data.keys)
        assertEquals("Przemek", data["displayName"])
        assertEquals("metro", data["theme"])
        assertEquals(1.1, data["uiScale"])
    }
}
