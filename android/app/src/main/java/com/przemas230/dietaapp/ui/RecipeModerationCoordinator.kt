package com.przemas230.dietaapp.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.przemas230.dietaapp.logic.CommunityRecipeOperations
import kotlinx.coroutines.tasks.await

/**
 * Only this account can approve/reject community recipe submissions from
 * inside the app (user request, 2026-08-11) -- everyone else still only
 * sees their OWN submissions' status. This is a client-side convenience
 * check only; the real enforcement is the Firestore security rule (see
 * `docs/FIREBASE_MIGRATION_PLAN.md`'s `recipes/{recipeId}` rule, which must
 * grant this exact email `request.auth.token.email` read/update access --
 * pasting that updated rule into the Firebase console is a manual step the
 * user still needs to do, same as every other Firestore-rules-dependent
 * feature in this app; until then this card safely shows an empty pending
 * list instead of crashing or leaking data, per CommunityCoordinator's own
 * "best-effort" convention.
 */
const val RECIPE_MODERATOR_EMAIL = "przemas230@gmail.com"

/**
 * FR-76/v2: the "Moje przepisy" + moderator-approval counterpart to
 * CommunityCoordinator -- two independent Firestore listeners on the same
 * public `recipes` collection:
 * 1. `authorUid == uid`, for ANY signed-in user -- feeds "Moje przepisy"'s
 *    per-recipe pending/approved/rejected badge in Ustawienia.
 * 2. `status == "pending"`, ONLY while signed in as [RECIPE_MODERATOR_EMAIL]
 *    -- feeds the moderation card's approve/reject list.
 *
 * Renders nothing; called once from DietaAppRoot next to CommunityCoordinator.
 */
@Composable
fun RecipeModerationCoordinator(authViewModel: AuthViewModel, viewModel: RecipeModerationViewModel) {
    val authState by authViewModel.state.collectAsState()
    val signedIn = authState as? AuthState.SignedIn
    val uid = signedIn?.uid
    val isModerator = signedIn?.email == RECIPE_MODERATOR_EMAIL

    DisposableEffect(uid) {
        if (uid == null) {
            viewModel.replaceMyRecipeStatuses(emptyMap())
            return@DisposableEffect onDispose {}
        }
        val registration = FirebaseFirestore.getInstance().collection("recipes")
            .whereEqualTo("authorUid", uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener
                viewModel.replaceMyRecipeStatuses(
                    snapshot.documents.associate { it.id to (it.getString("status") ?: "pending") },
                )
            }
        onDispose { registration.remove() }
    }

    DisposableEffect(isModerator) {
        if (!isModerator) {
            viewModel.replacePendingRecipes(emptyList())
            return@DisposableEffect onDispose {}
        }
        val registration = FirebaseFirestore.getInstance().collection("recipes")
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener
                val recipes = snapshot.documents.mapNotNull { doc ->
                    doc.data?.let { CommunityRecipeOperations.sanitizeCommunityRecipeDoc(it, doc.id) }
                }
                viewModel.replacePendingRecipes(recipes)
            }
        onDispose { registration.remove() }
    }
}

/**
 * Returns the failure (e.g. `PERMISSION_DENIED` when the Firestore rule from
 * `docs/FIREBASE_MIGRATION_PLAN.md` hasn't been pasted into the console yet)
 * instead of swallowing it -- a silent catch here made denied writes
 * indistinguishable from "the tap didn't register" from the moderator's POV.
 * See [RecipeModerationCard] for how the caller surfaces this to the user.
 */
suspend fun approveRecipe(recipeId: String): Result<Unit> = runCatching {
    FirebaseFirestore.getInstance().collection("recipes").document(recipeId)
        .update("status", "approved").await()
    Unit
}.onFailure { Log.w("RecipeModeration", "approveRecipe($recipeId) failed", it) }

/** Sets `status = "rejected"` rather than deleting -- keeps the doc around so the author's "Moje przepisy" card can show why it didn't get published. */
suspend fun rejectRecipe(recipeId: String): Result<Unit> = runCatching {
    FirebaseFirestore.getInstance().collection("recipes").document(recipeId)
        .update("status", "rejected").await()
    Unit
}.onFailure { Log.w("RecipeModeration", "rejectRecipe($recipeId) failed", it) }
