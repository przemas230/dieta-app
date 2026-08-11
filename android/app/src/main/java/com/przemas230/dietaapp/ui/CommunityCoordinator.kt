package com.przemas230.dietaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.przemas230.dietaapp.data.RecipeReview
import com.przemas230.dietaapp.logic.CommunityRecipeOperations
import kotlinx.coroutines.tasks.await

private const val ANONYMOUS_DISPLAY_NAME = "Anonimowy użytkownik"

/**
 * FR-68/76/77: the "social" counterpart to CloudSyncCoordinator -- talks to
 * the PUBLIC `recipes`/`publicProfiles` Firestore collections (not the
 * private `users/{uid}` doc FR-73/78 use) and is a no-op unless AuthState is
 * SignedIn (never for anonymous accounts, mirroring index.html's own
 * `!currentFbUser.isAnonymous` guard on every function ported here). Every
 * write is best-effort (caught exception, never surfaced to the user) --
 * offline or not-yet-pasted Firestore security rules must never crash the
 * app or block local state, matching FR-76/77's "safe, expected 'not yet
 * configured' state" acceptance criteria.
 *
 * Renders nothing -- called once from DietaAppRoot next to CloudSyncCoordinator.
 */
@Composable
fun CommunityCoordinator(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    recipeViewModel: RecipeViewModel,
    commentsViewModel: RecipeCommentsViewModel,
) {
    val authState by authViewModel.state.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    val myRecipes by recipeViewModel.myRecipes.collectAsState()
    val communityRecipesEnabled by recipeViewModel.communityRecipesEnabled.collectAsState()
    val reviews by recipeViewModel.reviews.collectAsState()

    val uid = (authState as? AuthState.SignedIn)?.uid
    val currentDisplayName = rememberUpdatedState(displayName)

    // touchPublicProfile(): upserts this device's public nickname + last
    // login timestamp once per sign-in (index.html:3040-3046).
    LaunchedEffect(uid) {
        if (uid == null) return@LaunchedEffect
        try {
            FirebaseFirestore.getInstance().collection("publicProfiles").document(uid)
                .set(
                    mapOf(
                        "displayName" to currentDisplayName.value,
                        "lastLoginAt" to FieldValue.serverTimestamp(),
                    ),
                    SetOptions.merge(),
                )
                .await()
        } catch (e: Exception) {
            // Best-effort, see class doc.
        }
    }

    // pushCommunityRecipe()/deleteCommunityRecipe() (index.html:3047-3059),
    // ported reactively (diffing myRecipes against a baseline) instead of
    // hooking RecipeViewModel.addCustomRecipe/removeCustomRecipe directly.
    // `publishBaseline == null` means "just (re)started observing this
    // account" -- adopts whatever custom recipes already exist WITHOUT
    // re-publishing them, so an already-"approved" recipe doesn't get its
    // status silently reset back to "pending" every time the app restarts.
    var publishBaseline by remember(uid) { mutableStateOf<Set<String>?>(null) }
    LaunchedEffect(uid, myRecipes) {
        if (uid == null) return@LaunchedEffect
        val currentIds = myRecipes.map { it.id }.toSet()
        val baseline = publishBaseline
        if (baseline == null) {
            publishBaseline = currentIds
            return@LaunchedEffect
        }
        val db = FirebaseFirestore.getInstance()
        myRecipes.filter { it.id !in baseline }.forEach { recipe ->
            try {
                db.collection("recipes").document(recipe.id).set(
                    mapOf(
                        "cat" to recipe.cat,
                        "name" to recipe.name,
                        "time" to recipe.time,
                        "kcal" to recipe.kcal,
                        "ingredients" to recipe.ingredients,
                        "method" to recipe.method,
                        "protein" to recipe.protein,
                        "carbs" to recipe.carbs,
                        "fat" to recipe.fat,
                        "fiber" to recipe.fiber,
                        "gi" to recipe.gi,
                        "gl" to recipe.gl,
                        "authorUid" to uid,
                        "authorDisplayName" to currentDisplayName.value.ifBlank { ANONYMOUS_DISPLAY_NAME },
                        "status" to "pending",
                        "createdAt" to FieldValue.serverTimestamp(),
                    ),
                ).await()
            } catch (e: Exception) {
                // Best-effort, see class doc.
            }
        }
        (baseline - currentIds).forEach { removedId ->
            try {
                db.collection("recipes").document(removedId).delete().await()
            } catch (e: Exception) {
                // Best-effort, see class doc.
            }
        }
        publishBaseline = currentIds
    }

    // refreshCommunityRecipesSubscription() (index.html:3079-3091): live
    // "status == approved" set while signed in AND the toggle is on,
    // otherwise cleared -- RecipeViewModel.recompute() folds this (deduped
    // against myRecipes) into the visible recipe list.
    val shouldListen = uid != null && communityRecipesEnabled
    DisposableEffect(shouldListen) {
        if (!shouldListen) {
            recipeViewModel.replaceCommunityRecipes(emptyList())
            return@DisposableEffect onDispose {}
        }
        val registration = FirebaseFirestore.getInstance().collection("recipes")
            .whereEqualTo("status", "approved")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener
                val recipes = snapshot.documents.mapNotNull { doc ->
                    doc.data?.let { CommunityRecipeOperations.sanitizeCommunityRecipeDoc(it, doc.id) }
                }
                recipeViewModel.replaceCommunityRecipes(recipes)
            }
        onDispose { registration.remove() }
    }

    // pushRecipeRating()/deleteRecipeRating() (index.html:3060-3076), same
    // "adopt a baseline first, then diff" reactive pattern as recipe
    // publishing above -- avoids re-writing every review (with a fresh
    // createdAt) on every app restart.
    var reviewBaseline by remember(uid) { mutableStateOf<Map<String, RecipeReview>?>(null) }
    LaunchedEffect(uid, reviews) {
        if (uid == null) return@LaunchedEffect
        val baseline = reviewBaseline
        if (baseline == null) {
            reviewBaseline = reviews
            return@LaunchedEffect
        }
        val db = FirebaseFirestore.getInstance()
        reviews.filter { (id, review) -> baseline[id] != review }.forEach { (recipeId, review) ->
            val payload = mapOf(
                "stars" to review.stars,
                "comment" to (review.comment ?: ""),
                "displayName" to currentDisplayName.value.ifBlank { ANONYMOUS_DISPLAY_NAME },
                "createdAt" to FieldValue.serverTimestamp(),
            )
            try {
                db.collection("recipes").document(recipeId).collection("ratings").document(uid).set(payload).await()
                db.collection("publicProfiles").document(uid).collection("reviewedRecipes").document(recipeId).set(payload).await()
                commentsViewModel.invalidate(recipeId)
            } catch (e: Exception) {
                // Best-effort, see class doc.
            }
        }
        (baseline.keys - reviews.keys).forEach { removedId ->
            try {
                db.collection("recipes").document(removedId).collection("ratings").document(uid).delete().await()
                db.collection("publicProfiles").document(uid).collection("reviewedRecipes").document(removedId).delete().await()
                commentsViewModel.invalidate(removedId)
            } catch (e: Exception) {
                // Best-effort, see class doc.
            }
        }
        reviewBaseline = reviews
    }
}
