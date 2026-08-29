package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.EatenDay
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.data.SpiceLevel
import com.przemas230.dietaapp.data.WeightEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * FR-98: "export → wipe → import → everything came back", as a test that runs
 * on every build instead of once by hand.
 *
 * This is the guard the feature was missing. A backup that silently drops a
 * field is worse than no backup, because it looks like it worked — you only
 * find out when you need it. The payload is assembled exactly the way the
 * app assembles it (CloudSyncCodec.encodeAll plus the same extra fields
 * LocalPersistenceCoordinator adds), wrapped, read back, and decoded field
 * by field.
 *
 * The JSON text step itself is not re-implemented here (that needs
 * `org.json`, i.e. Android) — instead the payload is checked for being
 * JSON-SAFE, which is where that step can actually lose data: a future field
 * holding an enum, a data class or a Set would encode "successfully" and
 * come back useless.
 */
class BackupRoundTripTest {

    private val profile = Profile(
        sex = Sex.MEZCZYZNA,
        age = 41,
        heightCm = 183,
        weightKg = 88.5,
        targetWeightKg = 80.0,
        activity = ActivityLevel.UMIARKOWANIE_AKTYWNY,
        goal = Goal.REDUKCJA,
        glutenFree = true,
        lactoseFree = true,
        strictLowGI = true,
        configured = true,
    )

    private val pantry = mapOf(
        "jajka" to PantryItem.Product("jajka", PantryCategory.NABIAL, 8.0, "szt."),
        "sól" to PantryItem.Spice("sól", PantryCategory.PRZYPRAWY, SpiceLevel.DUZO),
    )

    private val weekPlan: WeekPlan = mapOf(
        0 to mapOf("sniadania" to PlannedMeal("S1", 1.5, true)),
        3 to mapOf("obiady" to PlannedMeal("O2", 1.0, false)),
    )

    private val eatenDays = mapOf(
        "2026-08-29" to EatenDay(
            entries = mapOf(
                // FR-105: a partial portion has to survive a backup, not
                // silently come back as a whole one.
                "obiady" to EatenEntry(done = true, kcal = 640, name = "Zupa", portion = 0.25),
                "sniadania" to EatenEntry(done = true, kcal = 320, name = "Owsianka"),
            ),
            snacks = listOf(Snack("s1", "Banan", 95)),
        ),
    )

    /** Exactly what LocalPersistenceCoordinator writes to LocalStateStore -- the backup payload IS that file. */
    private fun buildPayload(): Map<String, Any?> = CloudSyncCodec.encodeAll(
        displayName = "Przemek",
        profile = profile,
        pantry = pantry,
        // FR-102: products deleted for good.
        pantryHidden = setOf("chleb", "mleko kokosowe"),
        themeId = "clinic",
        uiScale = 1.15,
        swipeRatingStyle = "GLOW",
        favIngredients = setOf("jajka", "szpinak"),
        recipeRating = mapOf("R1" to RecipeRating.LIKE),
        cooked = mapOf("R1" to listOf(com.przemas230.dietaapp.data.CookEntry(1_756_000_000_000L, 4))),
        shopping = mapOf(
            "jajka|count" to ShoppingItem("jajka", "count", 6.0, true, mapOf("R1" to 6.0)),
        ),
        weekPlan = weekPlan,
        eatenDays = eatenDays,
        waterCount = 5,
    ) + mapOf(
        "favorites" to CloudSyncCodec.encodeFavIngredients(setOf("R1", "R2")),
        "weights" to CloudSyncCodec.encodeWeights(listOf(WeightEntry("2026-08-01", 90.0), WeightEntry("2026-08-29", 88.5))),
        "waterHistory" to CloudSyncCodec.encodeDateIntMap(mapOf("2026-08-28" to 7)),
        "communityRecipesEnabled" to true,
        "remainingKcalFillEnabled" to true,
        "fastingEnabled" to true,
        "fastingWindowStart" to 11,
        "fastingWindowEnd" to 19,
    )

    @Test
    fun `every field survives export and import`() {
        val exported = BackupEnvelope.wrap(buildPayload(), "2026-08-29T09:49:51Z")
        val result = BackupEnvelope.read(exported)
        assertTrue(result is BackupEnvelope.Result.Ok, "well-formed backup must read back")
        val data = (result as BackupEnvelope.Result.Ok).data
        assertEquals("2026-08-29T09:49:51Z", result.exportedAt)

        assertEquals("Przemek", data["displayName"])
        assertEquals(profile, CloudSyncCodec.decodeProfile(data["profile"] as? Map<*, *>))
        assertEquals(pantry, CloudSyncCodec.decodePantry(data["pantry"] as? Map<*, *>))
        assertEquals(setOf("chleb", "mleko kokosowe"), CloudSyncCodec.decodePantryHidden(data["pantryHidden"] as? Map<*, *>))
        assertEquals("clinic", data["theme"])
        assertEquals(1.15, data["uiScale"])
        assertEquals("GLOW", data["swipeRatingStyle"])
        assertEquals(setOf("jajka", "szpinak"), CloudSyncCodec.decodeFavIngredients(data["favIngredients"] as? Map<*, *>))
        assertEquals(setOf("R1", "R2"), CloudSyncCodec.decodeFavIngredients(data["favorites"] as? Map<*, *>))
        assertEquals(mapOf("R1" to RecipeRating.LIKE), CloudSyncCodec.decodeRecipeRating(data["recipeRating"] as? Map<*, *>))
        assertEquals(
            mapOf("jajka|count" to ShoppingItem("jajka", "count", 6.0, true, mapOf("R1" to 6.0))),
            CloudSyncCodec.decodeShopping(data["shopping"] as? Map<*, *>),
        )
        assertEquals(
            weekPlan,
            CloudSyncCodec.decodeWeekPlan(
                data["planner"] as? Map<*, *>,
                data["plannerScale"] as? Map<*, *>,
                data["plannerLeftover"] as? Map<*, *>,
            ),
        )
        assertEquals(2, CloudSyncCodec.decodeWeights(data["weights"] as? List<*>)?.size)
        assertEquals(mapOf("2026-08-28" to 7), CloudSyncCodec.decodeDateIntMap(data["waterHistory"] as? Map<*, *>))
        assertEquals(true, data["communityRecipesEnabled"])
        assertEquals(true, data["remainingKcalFillEnabled"])
        assertEquals(true, data["fastingEnabled"])
        assertEquals(11, data["fastingWindowStart"])
        assertEquals(19, data["fastingWindowEnd"])
    }

    @Test
    fun `a partial portion comes back as the same fraction, not a whole one`() {
        val exported = BackupEnvelope.wrap(buildPayload(), "2026-08-29T09:49:51Z")
        val data = (BackupEnvelope.read(exported) as BackupEnvelope.Result.Ok).data
        val days = CloudSyncCodec.decodeEaten(data["eaten"] as? Map<*, *>)!!
        val entries = days.getValue("2026-08-29").entries
        assertEquals(0.25, EatenOperations.portionOf(entries, "obiady"))
        assertEquals(1.0, EatenOperations.portionOf(entries, "sniadania"))
        // And the kcal total the app would show for that restored day.
        assertEquals(160 + 320, EatenOperations.dailyEatenKcal(entries))
        assertEquals(listOf(Snack("s1", "Banan", 95)), days.getValue("2026-08-29").snacks)
    }

    @Test
    fun `the whole payload is JSON-safe`() {
        // The step this test does not re-run (Map -> JSON text -> Map, which
        // needs Android's org.json) can only lose data if something in here
        // isn't a JSON type. This is that check.
        val unsafe = BackupEnvelope.jsonUnsafePaths(buildPayload())
        assertTrue(unsafe.isEmpty(), "these would not survive JSON: $unsafe")
    }

    @Test
    fun `a file that is not our backup is refused, and nothing is read from it`() {
        assertEquals(BackupEnvelope.Result.NotABackup, BackupEnvelope.read(null))
        assertEquals(BackupEnvelope.Result.NotABackup, BackupEnvelope.read(mapOf("hello" to "world")))
        assertEquals(
            BackupEnvelope.Result.NotABackup,
            BackupEnvelope.read(mapOf("format" to "dieta-app-backup")),  // no data
        )
    }

    @Test
    fun `a backup from a newer app version is refused with its version number`() {
        val fromTheFuture = BackupEnvelope.wrap(emptyMap(), "2027-01-01T00:00:00Z") + mapOf("version" to 99)
        assertEquals(BackupEnvelope.Result.TooNew(99), BackupEnvelope.read(fromTheFuture))
    }

    @Test
    fun `an older backup missing newer fields still restores what it does have`() {
        // The compatibility direction that must keep working: a file written
        // before FR-102/FR-105 existed has no pantryHidden and no portion.
        val old = BackupEnvelope.wrap(
            mapOf("displayName" to "Stary", "theme" to "metro"),
            "2026-01-01T00:00:00Z",
        )
        val data = (BackupEnvelope.read(old) as BackupEnvelope.Result.Ok).data
        assertEquals("Stary", data["displayName"])
        assertEquals("metro", data["theme"])
        // Absent, not garbage -- the reader applies only the keys it finds.
        assertEquals(null, data["pantryHidden"])
    }

    @Test
    fun `the suggested file name carries the day it was taken`() {
        assertEquals("dieta-app-kopia-2026-08-29.json", BackupEnvelope.suggestedFileName("2026-08-29"))
    }

    @Test
    fun `the JSON-safety check actually catches the types that would break`() {
        // Guards the guard: an assertion that can never fail is worse than no
        // assertion, so the detector gets its own test with the exact things
        // it exists to catch -- an enum, a data class and a Set.
        assertTrue(BackupEnvelope.jsonUnsafePaths(mapOf("a" to 1, "b" to listOf("x", true, null))).isEmpty())
        assertEquals(
            listOf("data.sex (Sex)"),
            BackupEnvelope.jsonUnsafePaths(mapOf("sex" to Sex.KOBIETA)),
        )
        assertEquals(
            listOf("data.profile (Profile)"),
            BackupEnvelope.jsonUnsafePaths(mapOf("profile" to profile)),
        )
        // A Set is not a List, so it does not survive -- the exact class name
        // varies by construction, hence matching on the path only.
        val setPaths = BackupEnvelope.jsonUnsafePaths(mapOf("hidden" to setOf("chleb")))
        assertEquals(1, setPaths.size)
        assertTrue(setPaths.single().startsWith("data.hidden ("), setPaths.toString())
        assertEquals(
            listOf("data.days[1].when (Instant)"),
            BackupEnvelope.jsonUnsafePaths(mapOf("days" to listOf("ok", mapOf("when" to java.time.Instant.EPOCH)))),
        )
    }
}
