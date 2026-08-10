package com.przemas230.dietaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.przemas230.dietaapp.logic.CloudSyncCodec
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

/**
 * FR-73: pushes the currently-syncable Android state (profile, display
 * name, pantry, theme, UI scale, swipe-rating style -- see CloudSyncCodec's
 * doc comment for why this subset) to `users/{uid}` in Firestore whenever
 * it changes, debounced 1.5s like index.html's scheduleCloudPush(), and
 * applies incoming remote changes back onto the same ViewModels the rest of
 * the app already reads from. Only active while signed in to a real
 * (non-anonymous) account -- AuthViewModel's Anonymous/Unavailable states
 * leave this a no-op, matching FR-73's "anonymous never syncs" criterion.
 *
 * Semantics are FR-73's ORIGINAL "last cloud write wins the whole document"
 * (an incoming snapshot replaces local state field-by-field, no per-item
 * merge or conflict UI) -- FR-78's later 3-way merge is a separate,
 * follow-up port, not attempted here.
 *
 * Renders nothing; called once from DietaAppRoot alongside the other
 * shared-ViewModel wiring.
 */
@Composable
fun CloudSyncCoordinator(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    pantryViewModel: PantryViewModel,
    themeViewModel: ThemeViewModel,
    uiScaleViewModel: UiScaleViewModel,
    swipeRatingStyleViewModel: SwipeRatingStyleViewModel,
) {
    val authState by authViewModel.state.collectAsState()
    val profile by profileViewModel.profile.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    val themeId by themeViewModel.themeId.collectAsState()
    val uiScale by uiScaleViewModel.uiScale.collectAsState()
    val swipeStyle by swipeRatingStyleViewModel.style.collectAsState()

    val uid = (authState as? AuthState.SignedIn)?.uid

    // Set right after CloudSyncCoordinator itself applies an incoming
    // remote snapshot, so that recomposition's own PUSH effect (below)
    // doesn't immediately echo the very data it just received back to
    // Firestore. Cleared again once consumed.
    var suppressNextPush by remember { mutableStateOf(false) }

    LaunchedEffect(uid, profile, displayName, pantryItems, themeId, uiScale, swipeStyle) {
        if (uid == null) return@LaunchedEffect
        if (suppressNextPush) {
            suppressNextPush = false
            return@LaunchedEffect
        }
        delay(1500)
        val data = CloudSyncCodec.encodeAll(displayName, profile, pantryItems, themeId, uiScale, swipeStyle.name)
        try {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(data, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            // Offline or transient failure -- Firestore's own offline cache
            // will retry the write once connectivity returns; the next
            // local change will also naturally re-attempt a push.
        }
    }

    DisposableEffect(uid) {
        if (uid == null) return@DisposableEffect onDispose {}
        val registration = FirebaseFirestore.getInstance().collection("users").document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener
                // A snapshot event for our own not-yet-server-acknowledged
                // write -- it already matches local state (that's what we
                // just wrote), applying it again would be a no-op at best
                // and a wasted extra local write at worst.
                if (snapshot.metadata.hasPendingWrites()) return@addSnapshotListener
                val data = snapshot.data ?: return@addSnapshotListener

                var appliedAnything = false
                (data["displayName"] as? String)?.let {
                    if (it != displayName) { profileViewModel.setDisplayName(it); appliedAnything = true }
                }
                CloudSyncCodec.decodeProfile(data["profile"] as? Map<*, *>)?.let {
                    if (it != profile) { profileViewModel.save(it); appliedAnything = true }
                }
                CloudSyncCodec.decodePantry(data["pantry"] as? Map<*, *>)?.let {
                    if (it != pantryItems) { pantryViewModel.replaceAll(it); appliedAnything = true }
                }
                (data["theme"] as? String)?.let {
                    if (it != themeId) { themeViewModel.setTheme(it); appliedAnything = true }
                }
                (data["uiScale"] as? Number)?.toDouble()?.let {
                    if (it != uiScale) { uiScaleViewModel.setScale(it); appliedAnything = true }
                }
                (data["swipeRatingStyle"] as? String)?.let { raw ->
                    val style = SwipeRatingStyle.entries.find { it.name == raw }
                    if (style != null && style != swipeStyle) { swipeRatingStyleViewModel.setStyle(style); appliedAnything = true }
                }
                if (appliedAnything) suppressNextPush = true
            }
        onDispose { registration.remove() }
    }
}
