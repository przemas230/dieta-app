package com.przemas230.dietaapp.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Local persistence for the Spiżarnia (pantry) tab -- SharedPreferences +
 * org.json, the same "no extra library" choice as RecipeRepository (this is
 * I/O glue at the Android boundary, not business logic -- the actual pantry
 * mutation rules are already unit-tested in PantryOperationsTest, logic/).
 *
 * Without this, PantryViewModel's state lived only in memory: backgrounding
 * the app long enough for Android to reclaim it (or a manual force-stop)
 * silently wiped the whole pantry on next launch, which is what made it
 * read as "doesn't work at all" rather than "not yet synced to the cloud"
 * (that's the separate, much bigger FR-73/FR-78).
 */
object PantryStore {
    private const val PREFS_NAME = "dieta_app_prefs"
    private const val KEY_PANTRY = "pantry_v1"

    /** FR-102: canonical names the user deleted from the Spiżarnia for good (see PantryOperations.visibleTileNames). */
    private const val KEY_HIDDEN = "pantry_hidden_v1"

    fun load(context: Context): Map<String, PantryItem> {
        val raw = prefs(context).getString(KEY_PANTRY, null) ?: return emptyMap()
        return try {
            parse(raw)
        } catch (e: Exception) {
            // Corrupt/unrecognized data (e.g. an older schema) -- start fresh
            // rather than crashing the whole app on launch.
            emptyMap()
        }
    }

    fun save(context: Context, items: Map<String, PantryItem>) {
        prefs(context).edit().putString(KEY_PANTRY, serialize(items)).apply()
    }

    /** FR-102: persisted separately from [load]'s stock map -- a hidden tile usually has no stock entry left to hang off. */
    fun loadHidden(context: Context): Set<String> {
        val raw = prefs(context).getString(KEY_HIDDEN, null) ?: return emptySet()
        return try {
            val array = JSONArray(raw)
            (0 until array.length()).map { array.optString(it) }.filter { it.isNotEmpty() }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun saveHidden(context: Context, hidden: Set<String>) {
        val array = JSONArray()
        hidden.forEach { array.put(it) }
        prefs(context).edit().putString(KEY_HIDDEN, array.toString()).apply()
    }

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun serialize(items: Map<String, PantryItem>): String {
        val array = JSONArray()
        items.values.forEach { item ->
            val obj = JSONObject()
            obj.put("name", item.name)
            obj.put("category", item.category.name)
            when (item) {
                is PantryItem.Product -> {
                    obj.put("type", "product")
                    obj.put("quantity", item.quantity)
                    obj.put("unit", item.unit)
                    if (item.stepOverride != null) obj.put("stepOverride", item.stepOverride)
                }
                is PantryItem.Spice -> {
                    obj.put("type", "spice")
                    obj.put("level", item.level.name)
                }
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun parse(raw: String): Map<String, PantryItem> {
        val array = JSONArray(raw)
        val result = LinkedHashMap<String, PantryItem>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val name = obj.getString("name")
            val category = PantryCategory.valueOf(obj.getString("category"))
            val item: PantryItem = when (obj.getString("type")) {
                "spice" -> PantryItem.Spice(name, category, SpiceLevel.valueOf(obj.getString("level")))
                else -> PantryItem.Product(
                    name,
                    category,
                    obj.getDouble("quantity"),
                    obj.getString("unit"),
                    stepOverride = if (obj.has("stepOverride")) obj.getDouble("stepOverride") else null,
                )
            }
            result[name] = item
        }
        return result
    }
}
