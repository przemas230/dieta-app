package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.przemas230.dietaapp.data.PublicProfile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.logic.CommunityRecipeOperations
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ReviewedRecipeSummary(val recipeId: String, val stars: Int, val comment: String?)

sealed class UserProfileState {
    data object Loading : UserProfileState()
    data class Loaded(
        val profile: PublicProfile,
        val approvedRecipes: List<Recipe>,
        val reviewedRecipes: List<ReviewedRecipeSummary>,
    ) : UserProfileState()
    data object Unavailable : UserProfileState()
}

/**
 * FR-76: port of openUserProfileModal (index.html:3123-3159) -- nickname,
 * last login, approved community recipes, reviewed recipes. Deliberately
 * NEVER shows email/diet profile/pantry/favorites, per FR-76's own
 * acceptance criteria on what a public profile may reveal.
 */
class UserProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow<UserProfileState>(UserProfileState.Loading)
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    fun load(uid: String, fallbackDisplayName: String?) {
        _state.value = UserProfileState.Loading
        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                coroutineScope {
                    val profileDeferred = async { db.collection("publicProfiles").document(uid).get().await() }
                    val recipesDeferred = async {
                        db.collection("recipes")
                            .whereEqualTo("authorUid", uid)
                            .whereEqualTo("status", "approved")
                            .get().await()
                    }
                    val reviewedDeferred = async {
                        db.collection("publicProfiles").document(uid).collection("reviewedRecipes")
                            .orderBy("createdAt", Query.Direction.DESCENDING)
                            .limit(20)
                            .get().await()
                    }
                    val profileDoc = profileDeferred.await()
                    val recipesSnap = recipesDeferred.await()
                    val reviewedSnap = reviewedDeferred.await()

                    val profile = PublicProfile(
                        uid = uid,
                        displayName = profileDoc.getString("displayName")?.takeIf { it.isNotBlank() }
                            ?: fallbackDisplayName?.takeIf { it.isNotBlank() }
                            ?: "Anonimowy użytkownik",
                        lastLoginAtMillis = (profileDoc.get("lastLoginAt") as? Timestamp)?.toDate()?.time,
                    )
                    val approvedRecipes = recipesSnap.documents.mapNotNull { doc ->
                        doc.data?.let { CommunityRecipeOperations.sanitizeCommunityRecipeDoc(it, doc.id) }
                    }
                    val reviewedRecipes = reviewedSnap.documents.map { doc ->
                        val stars = ((doc.getLong("stars") ?: 1L).toInt()).coerceIn(1, 5)
                        val comment = doc.getString("comment")?.takeIf { it.isNotBlank() }
                        ReviewedRecipeSummary(doc.id, stars, comment)
                    }
                    _state.value = UserProfileState.Loaded(profile, approvedRecipes, reviewedRecipes)
                }
            } catch (e: Exception) {
                _state.value = UserProfileState.Unavailable
            }
        }
    }
}
