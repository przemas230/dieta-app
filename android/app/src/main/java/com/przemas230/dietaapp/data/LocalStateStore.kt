package com.przemas230.dietaapp.data

import android.content.Context
import java.io.File
import org.json.JSONArray
import org.json.JSONObject

/**
 * Local, on-device persistence for the app's state -- the Android
 * equivalent of index.html's `localStorage.setItem(STORE_KEY,
 * JSON.stringify(state))` (`saveState()`). Reuses CloudSyncCodec's
 * Map<String, Any?> encode/decode pairs (see LocalPersistenceCoordinator)
 * so the same field shapes serve both local persistence and cloud sync --
 * one codec, two destinations, not two codecs to keep in sync with each
 * other AND with index.html.
 *
 * Deliberately independent of sign-in state -- unlike CloudSyncCoordinator
 * (which only runs for a real, signed-in account), this runs for EVERY
 * user including anonymous ones, since before this the app lost every
 * single local change (pantry, shopping list, planner, profile, everything)
 * on every restart. A plain JSON file in `context.filesDir` rather than
 * SharedPreferences, since the payload is one nested document, not a flat
 * set of key-value settings.
 */
object LocalStateStore {
    private const val FILE_NAME = "local_state.json"

    /** fileName defaults to the main local-state file; CloudSyncCoordinator reuses this same JSON plumbing under a different file name for its sync baseline (see CloudSyncBaselineStore). */
    fun load(context: Context, fileName: String = FILE_NAME): Map<String, Any?>? {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return null
        return try {
            val text = file.readText(Charsets.UTF_8)
            jsonObjectToMap(JSONObject(text))
        } catch (e: Exception) {
            // Corrupt/unreadable file -- treat as "nothing saved yet" rather than crash the app.
            null
        }
    }

    fun save(context: Context, data: Map<String, Any?>, fileName: String = FILE_NAME) {
        val file = File(context.filesDir, fileName)
        try {
            file.writeText(mapToJson(data).toString(), Charsets.UTF_8)
        } catch (e: Exception) {
            // Disk full/permission issue -- next change will retry; not worth crashing over.
        }
    }

    /** Deletes the given file if present -- used by CloudSyncBaselineStore.clear() so a stale on-disk baseline can't outlive an intentional local-data reset. */
    fun delete(context: Context, fileName: String = FILE_NAME) {
        try {
            File(context.filesDir, fileName).delete()
        } catch (e: Exception) {
            // Nothing to do -- worst case the file lingers and gets overwritten next save.
        }
    }

    /**
     * FR-98: the same JSON conversion the local-state file uses, exposed so
     * [BackupFile] can wrap/unwrap exactly that shape instead of growing a
     * second, subtly different encoder for the same data.
     */
    fun mapToJsonObject(map: Map<*, *>): JSONObject = mapToJson(map)

    /** FR-98: see [mapToJsonObject] -- the decode half. */
    fun jsonObjectToMapPublic(obj: JSONObject): Map<String, Any?> = jsonObjectToMap(obj)

    private fun mapToJson(map: Map<*, *>): JSONObject {
        val obj = JSONObject()
        map.forEach { (k, v) -> obj.put(k.toString(), anyToJson(v)) }
        return obj
    }

    private fun anyToJson(value: Any?): Any = when (value) {
        null -> JSONObject.NULL
        is Map<*, *> -> mapToJson(value)
        is List<*> -> JSONArray().apply { value.forEach { put(anyToJson(it)) } }
        else -> value
    }

    private fun jsonObjectToMap(obj: JSONObject): Map<String, Any?> {
        val result = LinkedHashMap<String, Any?>()
        val keys = obj.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            result[key] = jsonToAny(obj.opt(key))
        }
        return result
    }

    private fun jsonToAny(value: Any?): Any? = when {
        value == null || value == JSONObject.NULL -> null
        value is JSONObject -> jsonObjectToMap(value)
        value is JSONArray -> (0 until value.length()).map { jsonToAny(value.opt(it)) }
        else -> value
    }
}
