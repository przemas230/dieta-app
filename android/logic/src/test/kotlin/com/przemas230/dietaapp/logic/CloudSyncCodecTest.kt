package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.ActivityLogEntry
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeReview
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.data.SpiceLevel
import com.przemas230.dietaapp.data.WeightEntry
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
        val map = mapOf("age" to 25L, "height" to 170L, "weight" to 65L, "targetWeight" to 60L)
        val decoded = CloudSyncCodec.decodeProfile(map)
        assertEquals(25, decoded?.age)
        assertEquals(170, decoded?.heightCm)
        assertEquals(65.0, decoded?.weightKg)
        assertEquals(60.0, decoded?.targetWeightKg)
    }

    @Test
    fun `decodeProfile ignores an unrecognized value and falls back to default`() {
        val decoded = CloudSyncCodec.decodeProfile(mapOf("sex" to "NIEZNANE"))
        assertEquals(Sex.KOBIETA, decoded?.sex)
    }

    @Test
    fun `decodeProfile reads index_html's actual field names and value formats`() {
        // Regression test for the 2026-08-24 bug where Android wrote/read
        // heightCm/weightKg/targetWeightKg and Kotlin enum names
        // (MEZCZYZNA/LEKKO_AKTYWNY/BUDOWANIE), while index.html has always
        // written height/weight/targetWeight and "m"/"k",
        // "1.2".."1.725", "loss"/"maintain"/"gain" -- Android silently never
        // read a web-authored profile edit because of this mismatch.
        val webProfile = mapOf(
            "sex" to "m", "age" to 37L, "height" to 178L, "weight" to 74.0, "targetWeight" to 78.0,
            "activity" to "1.375", "goal" to "gain",
            "glutenFree" to false, "lactoseFree" to false, "strictLowGI" to false, "custom" to true, "configured" to true,
        )
        val decoded = CloudSyncCodec.decodeProfile(webProfile)
        assertEquals(Sex.MEZCZYZNA, decoded?.sex)
        assertEquals(178, decoded?.heightCm)
        assertEquals(74.0, decoded?.weightKg)
        assertEquals(78.0, decoded?.targetWeightKg)
        assertEquals(ActivityLevel.LEKKO_AKTYWNY, decoded?.activity)
        assertEquals(Goal.BUDOWANIE, decoded?.goal)
    }

    @Test
    fun `encodeProfile writes index_html's exact field names and value formats`() {
        val profile = Profile(sex = Sex.KOBIETA, activity = ActivityLevel.UMIARKOWANIE_AKTYWNY, goal = Goal.REDUKCJA)
        val encoded = CloudSyncCodec.encodeProfile(profile)
        assertEquals("k", encoded["sex"])
        assertEquals("1.55", encoded["activity"])
        assertEquals("loss", encoded["goal"])
        assertEquals(profile.heightCm, encoded["height"])
        assertEquals(profile.weightKg, encoded["weight"])
        assertEquals(profile.targetWeightKg, encoded["targetWeight"])
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
            "dobry" to mapOf("type" to "product", "cat" to "Warzywa", "qty" to 1.0, "unitCat" to "count"),
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
    fun `encodePantry uses web-compatible field names (cat label, qty, unitCat) not Android-internal ones`() {
        val pantry = mapOf("mleko" to PantryItem.Product("mleko", PantryCategory.NABIAL, 500.0, "ml"))
        val encoded = CloudSyncCodec.encodePantry(pantry)
        val entry = encoded["mleko"] as Map<*, *>
        assertEquals("Nabiał", entry["cat"])
        assertEquals(500.0, entry["qty"])
        assertEquals("volume", entry["unitCat"])
        assertEquals(null, entry["category"])
        assertEquals(null, entry["quantity"])
        assertEquals(null, entry["unit"])
    }

    @Test
    fun `favIngredients round-trips through encode-decode, dropping false values on decode`() {
        val favs = setOf("cebula", "pomidor")
        val decoded = CloudSyncCodec.decodeFavIngredients(CloudSyncCodec.encodeFavIngredients(favs))
        assertEquals(favs, decoded)
        assertEquals(setOf("a"), CloudSyncCodec.decodeFavIngredients(mapOf("a" to true, "b" to false)))
    }

    @Test
    fun `recipeRating round-trips through encode-decode using lowercase web strings`() {
        val ratings = mapOf("r1" to RecipeRating.LIKE, "r2" to RecipeRating.DISLIKE)
        val encoded = CloudSyncCodec.encodeRecipeRating(ratings)
        assertEquals("like", encoded["r1"])
        assertEquals("dislike", encoded["r2"])
        assertEquals(ratings, CloudSyncCodec.decodeRecipeRating(encoded))
    }

    @Test
    fun `cooked round-trips through encode-decode with ISO date strings`() {
        val cooked = mapOf(
            "r1" to listOf(com.przemas230.dietaapp.data.CookEntry(1_700_000_000_000L, 4), com.przemas230.dietaapp.data.CookEntry(1_700_100_000_000L, null)),
        )
        val encoded = CloudSyncCodec.encodeCooked(cooked)
        val firstEntry = (encoded["r1"] as List<*>)[0] as Map<*, *>
        assertEquals(true, (firstEntry["date"] as String).startsWith("20"))
        assertEquals(cooked, CloudSyncCodec.decodeCooked(encoded))
    }

    @Test
    fun `shopping round-trips through encode-decode renaming quantity to qty`() {
        val items = mapOf(
            "cebula|count" to com.przemas230.dietaapp.data.ShoppingItem("cebula", "count", 3.0, checked = true, contributions = mapOf("r1" to 3.0)),
        )
        val encoded = CloudSyncCodec.encodeShopping(items)
        val entry = encoded["cebula|count"] as Map<*, *>
        assertEquals(3.0, entry["qty"])
        assertEquals(items, CloudSyncCodec.decodeShopping(encoded))
    }

    @Test
    fun `weekPlan fans out into three parallel maps and re-merges on decode`() {
        val plan: WeekPlan = mapOf(0 to mapOf("obiady" to com.przemas230.dietaapp.data.PlannedMeal("r1", 1.5, isLeftover = true)))
        val planner = CloudSyncCodec.encodePlanner(plan)
        val scale = CloudSyncCodec.encodePlannerScale(plan)
        val leftover = CloudSyncCodec.encodePlannerLeftover(plan)
        assertEquals("r1", (planner["0"] as Map<*, *>)["obiady"])
        assertEquals(1.5, (scale["0"] as Map<*, *>)["obiady"])
        assertEquals(true, (leftover["0"] as Map<*, *>)["obiady"])
        assertEquals(plan, CloudSyncCodec.decodeWeekPlan(planner, scale, leftover))
    }

    @Test
    fun `eaten round-trips every date present, not just today`() {
        val day1 = com.przemas230.dietaapp.data.EatenDay(
            entries = mapOf("obiady" to com.przemas230.dietaapp.data.EatenEntry(true, 400, "Kotlet")),
            snacks = listOf(com.przemas230.dietaapp.data.Snack("s1", "banan", 105)),
        )
        val day2 = com.przemas230.dietaapp.data.EatenDay(
            entries = mapOf("sniadania" to com.przemas230.dietaapp.data.EatenEntry(true, 320, "Owsianka")),
        )
        val days = mapOf("2026-08-10" to day1, "2026-08-11" to day2)
        val decoded = CloudSyncCodec.decodeEaten(CloudSyncCodec.encodeEaten(days))
        assertEquals(days, decoded)
        assertNull(CloudSyncCodec.decodeEaten(null))
    }

    @Test
    fun `water only decodes when the remote date matches today (UTC)`() {
        val encoded = CloudSyncCodec.encodeWater(5)
        assertEquals(5, CloudSyncCodec.decodeWater(encoded))
        assertNull(CloudSyncCodec.decodeWater(mapOf("date" to "2000-01-01", "count" to 3)))
    }

    @Test
    fun `myRecipes round-trips a custom recipe and skips built-in ones`() {
        val custom = Recipe("custom-1", "obiady", "Test", "10 min", 300, listOf("a", "b"), "Zrób.", 20.0, 30.0, 10.0, null, null, null, source = "custom")
        val builtin = Recipe("r1", "obiady", "Wbudowany", "10 min", 300, listOf("a"), "Zrób.", null, null, null, null, null, null)
        val encoded = CloudSyncCodec.encodeMyRecipes(listOf(custom, builtin))
        assertEquals(1, encoded.size)
        assertEquals(listOf(custom), CloudSyncCodec.decodeMyRecipes(encoded))
    }

    @Test
    fun `reviews round-trips through encode-decode with an ISO date`() {
        val reviews = mapOf("r1" to RecipeReview(4, "Pyszne", 1_700_000_000_000L))
        val encoded = CloudSyncCodec.encodeReviews(reviews)
        val entry = encoded["r1"] as Map<*, *>
        assertEquals(true, (entry["at"] as String).startsWith("20"))
        assertEquals(reviews, CloudSyncCodec.decodeReviews(encoded))
    }

    @Test
    fun `weights round-trips through encode-decode`() {
        val entries = listOf(WeightEntry("2026-08-10", 68.5))
        assertEquals(entries, CloudSyncCodec.decodeWeights(CloudSyncCodec.encodeWeights(entries)))
    }

    @Test
    fun `date-int map round-trips and skips malformed entries`() {
        val history = mapOf("2026-08-09" to 1480, "2026-08-10" to 1500)
        assertEquals(history, CloudSyncCodec.decodeDateIntMap(CloudSyncCodec.encodeDateIntMap(history)))
        assertEquals(emptyMap<String, Int>(), CloudSyncCodec.decodeDateIntMap(mapOf("bad" to "not-a-number")))
        assertNull(CloudSyncCodec.decodeDateIntMap(null))
    }

    @Test
    fun `activity log round-trips and skips malformed entries`() {
        val entries = listOf(
            ActivityLogEntry(1_000L, "pantry_add", "Dodano do spiżarni: cebula"),
            ActivityLogEntry(2_000L, "shopping_remove", "Usunięto z listy zakupów: Zupa"),
        )
        assertEquals(entries, CloudSyncCodec.decodeActivityLog(CloudSyncCodec.encodeActivityLog(entries)))
        assertEquals(emptyList<ActivityLogEntry>(), CloudSyncCodec.decodeActivityLog(listOf(mapOf("ts" to 1L))))
        assertNull(CloudSyncCodec.decodeActivityLog(null))
    }

    @Test
    fun `activity log decodes web's ISO-string ts and legacy numeric ts alike`() {
        // index.html's addLog() writes ts via `new Date().toISOString()` -- a
        // string, not a number. Regression test for the 2026-08-24 bug where
        // decodeActivityLog required a Number and silently dropped every
        // web-authored entry (see encodeActivityLog's doc comment).
        val webAuthored = listOf(mapOf("ts" to "2026-08-08T14:08:03.400Z", "action" to "pantry_add", "detail" to "Spiżarnia: cebula (+1)"))
        val decoded = CloudSyncCodec.decodeActivityLog(webAuthored)
        assertEquals(listOf(ActivityLogEntry(1786198083400L, "pantry_add", "Spiżarnia: cebula (+1)")), decoded)

        // Legacy Android-only numeric ts (from before this fix) must still decode.
        val legacyAndroid = listOf(mapOf("ts" to 1_000L, "action" to "pantry_add", "detail" to "Dodano do spiżarni: cebula"))
        assertEquals(listOf(ActivityLogEntry(1_000L, "pantry_add", "Dodano do spiżarni: cebula")), CloudSyncCodec.decodeActivityLog(legacyAndroid))
    }

    @Test
    fun `encodeAll produces the expected top-level keys`() {
        val data = CloudSyncCodec.encodeAll(
            displayName = "Przemek",
            profile = Profile(),
            pantry = emptyMap(),
            pantryHidden = setOf("chleb"),
            themeId = "metro",
            uiScale = 1.1,
            swipeRatingStyle = "GLOW",
            favIngredients = emptySet(),
            recipeRating = emptyMap(),
            cooked = emptyMap(),
            shopping = emptyMap(),
            weekPlan = emptyMap(),
            eatenDays = emptyMap(),
            waterCount = 0,
        )
        assertEquals(
            setOf(
                "displayName", "profile", "pantry", "pantryHidden", "theme", "uiScale", "swipeRatingStyle",
                "favIngredients", "recipeRating", "cooked", "shopping", "planner", "plannerScale",
                "plannerLeftover", "eaten", "water",
            ),
            data.keys,
        )
        assertEquals("Przemek", data["displayName"])
        // FR-102: the hidden set travels as index.html's {name: true} map shape.
        assertEquals(mapOf("chleb" to true), data["pantryHidden"])
        assertEquals("metro", data["theme"])
        assertEquals(1.1, data["uiScale"])
    }

    @Test
    fun `pantryHidden round-trips as a name to true map and ignores false entries`() {
        val encoded = CloudSyncCodec.encodePantryHidden(setOf("chleb", "mleko"))
        assertEquals(mapOf("chleb" to true, "mleko" to true), encoded)
        assertEquals(setOf("chleb", "mleko"), CloudSyncCodec.decodePantryHidden(encoded))
        // index.html can leave a value behind as `false` instead of deleting
        // the key -- that means "not hidden", not "hidden".
        assertEquals(setOf("chleb"), CloudSyncCodec.decodePantryHidden(mapOf("chleb" to true, "mleko" to false)))
        assertNull(CloudSyncCodec.decodePantryHidden(null))
    }

    @Test
    fun `FR-103 eaten portions round-trip, and an entry without one decodes as a whole portion`() {
        val days = mapOf(
            "2026-08-29" to com.przemas230.dietaapp.data.EatenDay(
                entries = mapOf("obiad" to com.przemas230.dietaapp.data.EatenEntry(true, 600, "Zupa", 0.5)),
            ),
        )
        val encoded = CloudSyncCodec.encodeEaten(days)
        @Suppress("UNCHECKED_CAST")
        val day = encoded.getValue("2026-08-29") as Map<String, Any?>
        @Suppress("UNCHECKED_CAST")
        val entry = day.getValue("obiad") as Map<String, Any?>
        assertEquals(0.5, entry["portion"])
        assertEquals(0.5, CloudSyncCodec.decodeEaten(encoded)!!.getValue("2026-08-29").entries.getValue("obiad").portion)

        val legacy = mapOf("2026-08-28" to mapOf("obiad" to mapOf("done" to true, "kcal" to 500, "name" to "Ryż")))
        assertEquals(1.0, CloudSyncCodec.decodeEaten(legacy)!!.getValue("2026-08-28").entries.getValue("obiad").portion)
    }
}
