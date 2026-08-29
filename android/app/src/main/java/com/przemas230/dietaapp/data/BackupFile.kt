package com.przemas230.dietaapp.data

import android.content.Context
import org.json.JSONObject

/**
 * FR-98 (ported to Android 2026-08-29): the on-disk backup envelope, wire-
 * compatible with the file index.html writes and reads.
 *
 * The payload is deliberately the SAME map [LocalStateStore] already keeps —
 * which is itself the same field set the cloud round-trips. A backup that
 * covered anything more or less than that would, by definition, disagree
 * with what signing in on a second device transfers.
 *
 * The Android port is not a 1:1 rewrite of the web version: there is no blob
 * download here, so the UI drives it through the Storage Access Framework
 * (`ACTION_CREATE_DOCUMENT` / `ACTION_OPEN_DOCUMENT`, see SettingsScreen).
 * The FILE FORMAT is identical though, so a backup written on the phone
 * restores in the browser and vice versa — which is most of the point of
 * having it on both.
 */
object BackupFile {
    const val FORMAT = "dieta-app-backup"
    const val VERSION = 1

    /** What went wrong reading a file the user picked — each maps to its own message, so "wrong file" never reads as "corrupt". */
    sealed interface ParseResult {
        data class Ok(val data: Map<String, Any?>, val exportedAt: String?) : ParseResult

        /** Valid JSON, but not one of our backups (e.g. some other app's export). */
        data object NotABackup : ParseResult

        /** Not JSON at all, or unreadable. */
        data object Unreadable : ParseResult

        /** Written by a newer app version than this one knows how to read. */
        data class TooNew(val version: Int) : ParseResult
    }

    /** File name the picker is pre-filled with, e.g. `dieta-app-kopia-2026-08-29.json` (local day, see AppDates). */
    fun suggestedFileName(todayKey: String): String = "dieta-app-kopia-$todayKey.json"

    /** The full backup document as pretty-printed JSON, or null when there is nothing saved yet. */
    fun buildJson(context: Context, exportedAtIso: String): String? {
        val data = LocalStateStore.load(context) ?: return null
        val root = JSONObject()
        root.put("format", FORMAT)
        root.put("version", VERSION)
        root.put("exportedAt", exportedAtIso)
        root.put("data", LocalStateStore.mapToJsonObject(data))
        return root.toString(2)
    }

    fun parse(text: String): ParseResult {
        val root = try {
            JSONObject(text)
        } catch (e: Exception) {
            return ParseResult.Unreadable
        }
        if (root.optString("format") != FORMAT || !root.has("data")) return ParseResult.NotABackup
        val version = root.optInt("version", 1)
        if (version > VERSION) return ParseResult.TooNew(version)
        val dataObject = root.optJSONObject("data") ?: return ParseResult.NotABackup
        return ParseResult.Ok(
            data = LocalStateStore.jsonObjectToMapPublic(dataObject),
            exportedAt = root.optString("exportedAt").takeIf { it.isNotEmpty() },
        )
    }
}
