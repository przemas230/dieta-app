package com.przemas230.dietaapp.logic

/**
 * FR-98: the backup file's envelope and its validation rules, with no JSON
 * and no Android in sight.
 *
 * Split out of the app module's `BackupFile` so the decisions that actually
 * matter — is this our file, is it too new, what came back — can be tested
 * in the same plain-JVM module as the rest of the logic. `BackupFile` keeps
 * only the two things that genuinely need Android: turning this map into
 * JSON text and back, and the Storage Access Framework plumbing.
 *
 * Wire-compatible with index.html's `buildBackupPayload()`/import handler:
 * `{format, version, exportedAt, data:{...}}`. A backup written on the phone
 * has to open in the browser and vice versa, which is most of the point of
 * having it on both.
 */
object BackupEnvelope {
    const val FORMAT = "dieta-app-backup"
    const val VERSION = 1

    /** What reading a user-picked file produced. Each case gets its own message, so "wrong file" never reads as "corrupt". */
    sealed interface Result {
        data class Ok(val data: Map<String, Any?>, val exportedAt: String?) : Result

        /** Parsed fine, but it isn't one of our backups (e.g. some other app's export). */
        data object NotABackup : Result

        /** Written by a newer app version than this one knows how to read. */
        data class TooNew(val version: Int) : Result
    }

    /** Wraps a state snapshot in the envelope. [exportedAtIso] is passed in rather than read from the clock so this stays pure. */
    fun wrap(data: Map<String, Any?>, exportedAtIso: String): Map<String, Any?> = mapOf(
        "format" to FORMAT,
        "version" to VERSION,
        "exportedAt" to exportedAtIso,
        "data" to data,
    )

    /**
     * Validates a parsed backup document.
     *
     * Deliberately tolerant about the CONTENTS of `data` and strict about the
     * envelope: an older backup simply lacks whatever fields were added
     * since, and that has to keep working (the reader applies only the keys
     * it knows). A missing or wrong `format`, or a version from the future,
     * is a different thing — that file cannot be trusted at all, so nothing
     * is applied.
     */
    fun read(root: Map<*, *>?): Result {
        if (root == null) return Result.NotABackup
        if (root["format"] != FORMAT) return Result.NotABackup
        val version = (root["version"] as? Number)?.toInt() ?: 1
        if (version > VERSION) return Result.TooNew(version)
        val data = root["data"] as? Map<*, *> ?: return Result.NotABackup
        @Suppress("UNCHECKED_CAST")
        return Result.Ok(
            data = data.entries.mapNotNull { (k, v) -> (k as? String)?.let { it to v } }.toMap(),
            exportedAt = (root["exportedAt"] as? String)?.takeIf { it.isNotEmpty() },
        )
    }

    /** File name the save dialog is pre-filled with, e.g. `dieta-app-kopia-2026-08-29.json`. */
    fun suggestedFileName(todayKey: String): String = "dieta-app-kopia-$todayKey.json"

    /**
     * Whether a value can survive the JSON round trip this file goes through.
     *
     * Worth checking explicitly: everything here is assembled as
     * `Map<String, Any?>`, so nothing stops a future field from putting an
     * enum, a data class or a `Set` into the payload. It would compile,
     * encode "successfully", and come back as a useless string — the exact
     * shape of failure a backup must never have, because it looks like it
     * worked.
     */
    fun isJsonSafe(value: Any?): Boolean = when (value) {
        null, is String, is Boolean, is Int, is Long, is Double, is Float -> true
        is Map<*, *> -> value.all { (k, v) -> k is String && isJsonSafe(v) }
        is List<*> -> value.all { isJsonSafe(it) }
        else -> false
    }

    /** Every path inside [value] whose type would not survive JSON — empty means the payload is safe. */
    fun jsonUnsafePaths(value: Any?, path: String = "data"): List<String> = when {
        isJsonSafe(value) && value !is Map<*, *> && value !is List<*> -> emptyList()
        value is Map<*, *> -> value.entries.flatMap { (k, v) ->
            if (k !is String) listOf("$path.<non-string key: $k>") else jsonUnsafePaths(v, "$path.$k")
        }
        value is List<*> -> value.flatMapIndexed { i, v -> jsonUnsafePaths(v, "$path[$i]") }
        else -> listOf("$path (${value?.let { it::class.simpleName }})")
    }
}
