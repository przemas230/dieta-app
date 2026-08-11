package com.przemas230.dietaapp.data

import android.content.Context

/**
 * On-device cache of CloudSyncCoordinator's `lastKnownFields` -- "the last
 * value this device and Firestore agreed on, per field" -- so it survives
 * app restarts instead of resetting to empty every time the process dies.
 *
 * **Real bug this fixes (2026-08-11)**: without this, `lastKnownFields` was
 * pure in-memory Compose state. Sequence that lost data: user edits their
 * profile, the 1.5s debounced push hasn't fired yet, and the app is closed
 * (or the process dies) before it does. On next launch, the local edit is
 * correctly restored from `LocalStateStore` into the ViewModel -- but
 * `CloudSyncCoordinator` starts with an EMPTY `lastKnownFields`, so the
 * first incoming Firestore snapshot (still holding the OLD, pre-edit value)
 * looks "different from both what we last pushed (nothing yet) and what we
 * have now" and gets silently applied, overwriting the user's fresh edit
 * with the stale cloud value -- exactly the "nie zapamiętuje mi że jestem
 * mężczyzną, przełącza na kobietę" / "cel wrócił do domyślnego" the user
 * reported. Persisting the baseline means the app can tell "this differs
 * from Firestore because I have a genuine unsynced local edit" apart from
 * "this differs from Firestore because I don't know anything yet" (a truly
 * fresh device/sign-in, where pulling the cloud's data IS correct).
 *
 * Scoped by uid (stored alongside the fields) so switching accounts on the
 * same device starts with a clean baseline instead of comparing against a
 * previous account's data.
 *
 * **Bug fixed 2026-08-11** ("za każdym razem wraca mi jakaś defaultowa
 * dieta, mimo że jestem zalogowany do konta Google"): [clear] didn't exist,
 * so "Wyczyść dane lokalne" (FR-79, MainActivity's `onClearLocalData`) reset
 * every ViewModel back to fresh-install defaults but left this file
 * untouched. On the next sign-in to the SAME account, the persisted baseline
 * still held the account's real (pre-clear) data, so CloudSyncCoordinator's
 * pull condition (`incoming != lastKnownFields[key]`) saw the incoming
 * snapshot matching what it already "knew" and never re-applied it onto the
 * now-default local state -- permanently, since nothing ever invalidated
 * that baseline again on its own. index.html avoids this by nulling its
 * equivalent (`state._lastSyncedSnapshot`) on every sign-out; [clear] is
 * that same reset for Android, wired into `onClearLocalData`.
 */
object CloudSyncBaselineStore {
    private const val FILE_NAME = "cloud_sync_baseline.json"

    fun load(context: Context, uid: String): Map<String, Any?>? {
        val stored = LocalStateStore.load(context, FILE_NAME) ?: return null
        if (stored["uid"] != uid) return null
        @Suppress("UNCHECKED_CAST")
        return stored["fields"] as? Map<String, Any?>
    }

    fun save(context: Context, uid: String, fields: Map<String, Any?>) {
        LocalStateStore.save(context, mapOf("uid" to uid, "fields" to fields), FILE_NAME)
    }

    /** Wipes the on-device baseline entirely -- called when local data is deliberately reset, so a future sign-in (even to the same account) treats Firestore's data as genuinely new instead of "already known". */
    fun clear(context: Context) {
        LocalStateStore.delete(context, FILE_NAME)
    }
}
