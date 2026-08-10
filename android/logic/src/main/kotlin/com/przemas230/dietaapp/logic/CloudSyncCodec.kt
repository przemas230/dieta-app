package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.data.SpiceLevel

/**
 * FR-73: converts the Android app's currently-syncable state to/from plain
 * `Map<String, Any?>` shapes -- the native representation the Firestore SDK
 * reads/writes directly (`DocumentSnapshot.data`/`DocumentReference.set`),
 * so the app module doesn't need a JSON round-trip on top. Deliberately a
 * pure Kotlin file (no Firestore/Android import) so the encode/decode pairs
 * are unit-testable here -- see CloudSyncCodecTest.
 *
 * Scope is FR-73's ORIGINAL field list intersected with what Android
 * actually has built: displayName, profile, pantry, theme, uiScale,
 * swipeRatingStyle. Fields FR-73 also lists that don't exist as Android
 * state yet (favorites, favIngredients, myRecipes, recipeReviews/Rating,
 * customTiles, communityRecipesEnabled) are simply not encoded -- nothing
 * to sync for a feature that isn't ported. Semantics are FR-73's original
 * "last cloud write wins the whole document" (decode replaces local state
 * wholesale) -- NOT FR-78's later per-item 3-way merge, which is a separate,
 * bigger port.
 */
object CloudSyncCodec {
    fun encodeProfile(profile: Profile): Map<String, Any?> = mapOf(
        "sex" to profile.sex.name,
        "age" to profile.age,
        "heightCm" to profile.heightCm,
        "weightKg" to profile.weightKg,
        "targetWeightKg" to profile.targetWeightKg,
        "activity" to profile.activity.name,
        "goal" to profile.goal.name,
        "glutenFree" to profile.glutenFree,
        "lactoseFree" to profile.lactoseFree,
        "strictLowGI" to profile.strictLowGI,
        "configured" to profile.configured,
    )

    /** Returns null on missing/corrupt data so callers can skip applying it, instead of crashing on a bad remote doc. */
    fun decodeProfile(map: Map<*, *>?): Profile? {
        if (map == null) return null
        return try {
            Profile(
                sex = enumFrom<Sex>(map["sex"]) ?: Sex.KOBIETA,
                age = numberFrom(map["age"])?.toInt() ?: Profile().age,
                heightCm = numberFrom(map["heightCm"])?.toInt() ?: Profile().heightCm,
                weightKg = numberFrom(map["weightKg"]) ?: Profile().weightKg,
                targetWeightKg = numberFrom(map["targetWeightKg"]) ?: Profile().targetWeightKg,
                activity = enumFrom<ActivityLevel>(map["activity"]) ?: ActivityLevel.LEKKO_AKTYWNY,
                goal = enumFrom<Goal>(map["goal"]) ?: Goal.REDUKCJA,
                glutenFree = map["glutenFree"] as? Boolean ?: false,
                lactoseFree = map["lactoseFree"] as? Boolean ?: false,
                strictLowGI = map["strictLowGI"] as? Boolean ?: true,
                configured = map["configured"] as? Boolean ?: false,
            )
        } catch (e: Exception) {
            null
        }
    }

    fun encodePantry(items: Map<String, PantryItem>): Map<String, Any?> =
        items.mapValues { (_, item) ->
            when (item) {
                is PantryItem.Product -> mapOf(
                    "type" to "product",
                    "category" to item.category.name,
                    "quantity" to item.quantity,
                    "unit" to item.unit,
                )
                is PantryItem.Spice -> mapOf(
                    "type" to "spice",
                    "category" to item.category.name,
                    "level" to item.level.name,
                )
            }
        }

    fun decodePantry(map: Map<*, *>?): Map<String, PantryItem>? {
        if (map == null) return null
        val result = LinkedHashMap<String, PantryItem>()
        map.forEach { (rawKey, rawValue) ->
            val name = rawKey as? String ?: return@forEach
            val entry = rawValue as? Map<*, *> ?: return@forEach
            val category = enumFrom<PantryCategory>(entry["category"]) ?: PantryCategory.INNE
            val item: PantryItem? = when (entry["type"] as? String) {
                "spice" -> enumFrom<SpiceLevel>(entry["level"])?.let { PantryItem.Spice(name, category, it) }
                "product" -> {
                    val qty = numberFrom(entry["quantity"])
                    val unit = entry["unit"] as? String
                    if (qty != null && unit != null) PantryItem.Product(name, category, qty, unit) else null
                }
                else -> null
            }
            if (item != null) result[name] = item
        }
        return result
    }

    /** The full syncable-state document -- what gets pushed to `users/{uid}` with SetOptions.merge(). */
    fun encodeAll(
        displayName: String,
        profile: Profile,
        pantry: Map<String, PantryItem>,
        themeId: String,
        uiScale: Double?,
        swipeRatingStyle: String,
    ): Map<String, Any?> = mapOf(
        "displayName" to displayName,
        "profile" to encodeProfile(profile),
        "pantry" to encodePantry(pantry),
        "theme" to themeId,
        "uiScale" to uiScale,
        "swipeRatingStyle" to swipeRatingStyle,
    )

    private inline fun <reified T : Enum<T>> enumFrom(raw: Any?): T? {
        val name = raw as? String ?: return null
        return try {
            enumValueOf<T>(name)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun numberFrom(raw: Any?): Double? = (raw as? Number)?.toDouble()
}
