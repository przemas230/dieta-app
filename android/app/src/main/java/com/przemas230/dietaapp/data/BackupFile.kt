package com.przemas230.dietaapp.data

import android.content.Context
import com.przemas230.dietaapp.logic.BackupEnvelope
import org.json.JSONObject

/**
 * FR-98 (ported to Android 2026-08-29): the Android half of the backup file
 * — JSON text in and out, nothing else.
 *
 * Everything that involves a DECISION (is this our file, is it too new, what
 * does the envelope look like) lives in [BackupEnvelope] in the `logic`
 * module, where it is unit-tested without Android. What is left here is the
 * part that genuinely cannot move: `org.json` and the fact that the payload
 * is whatever [LocalStateStore] holds.
 *
 * The payload is deliberately the SAME map LocalStateStore already keeps —
 * itself the same field set the cloud round-trips. A backup covering
 * anything more or less than that would, by definition, disagree with what
 * signing in on a second device transfers.
 *
 * Not a 1:1 port of the web version: there is no blob download on Android,
 * so the UI drives this through the Storage Access Framework
 * (`ACTION_CREATE_DOCUMENT`/`ACTION_OPEN_DOCUMENT`, see SettingsScreen). The
 * FILE FORMAT is identical, so a backup written on the phone restores in the
 * browser and vice versa.
 */
object BackupFile {
    /** Result of reading a user-picked file. Adds the one failure mode [BackupEnvelope] can't see: text that isn't JSON at all. */
    sealed interface ParseResult {
        data class Ok(val data: Map<String, Any?>, val exportedAt: String?) : ParseResult
        data object NotABackup : ParseResult
        data object Unreadable : ParseResult
        data class TooNew(val version: Int) : ParseResult
    }

    fun suggestedFileName(todayKey: String): String = BackupEnvelope.suggestedFileName(todayKey)

    /** The full backup document as pretty-printed JSON, or null when there is nothing saved yet. */
    fun buildJson(context: Context, exportedAtIso: String): String? {
        val data = LocalStateStore.load(context) ?: return null
        val envelope = BackupEnvelope.wrap(data, exportedAtIso)
        return LocalStateStore.mapToJsonObject(envelope).toString(2)
    }

    fun parse(text: String): ParseResult {
        val root = try {
            LocalStateStore.jsonObjectToMapPublic(JSONObject(text))
        } catch (e: Exception) {
            return ParseResult.Unreadable
        }
        return when (val result = BackupEnvelope.read(root)) {
            is BackupEnvelope.Result.Ok -> ParseResult.Ok(result.data, result.exportedAt)
            BackupEnvelope.Result.NotABackup -> ParseResult.NotABackup
            is BackupEnvelope.Result.TooNew -> ParseResult.TooNew(result.version)
        }
    }
}
