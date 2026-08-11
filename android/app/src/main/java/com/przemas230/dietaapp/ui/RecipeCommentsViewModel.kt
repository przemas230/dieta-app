package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.przemas230.dietaapp.data.RecipeComment
import com.przemas230.dietaapp.logic.CommunityRecipeOperations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val FIRST_PAGE_SIZE = 3L
private const val MORE_PAGE_SIZE = 10L

data class CommentsPageState(
    val comments: List<RecipeComment> = emptyList(),
    val lastSnapshot: DocumentSnapshot? = null,
    val exhausted: Boolean = false,
    val loading: Boolean = false,
    // True only when the FIRST page failed to load (offline, Firestore
    // unavailable, rules not yet pasted) -- a failed "load more" keeps
    // whatever comments are already shown instead of hiding them.
    val unavailable: Boolean = false,
)

/**
 * FR-77: paginated reader for `recipes/{id}/ratings` -- one entry per
 * recipeId, first page of 3 then +10 per "Pokaż więcej", mirroring
 * index.html's `commentsPageState`/`loadRecipeComments` (index.html:4676,
 * 4680-4715). Purely a reader: saving/deleting THIS device's own review is
 * CommunityCoordinator's job, which also calls [invalidate] here so an
 * already-expanded thread reflects it immediately (FR-77 acceptance
 * criterion). Reading doesn't require a real (non-anonymous) account --
 * Firestore's security rules only require `request.auth != null`, which is
 * always true (the app is always signed into at least an anonymous user).
 */
class RecipeCommentsViewModel : ViewModel() {
    private val _pages = MutableStateFlow<Map<String, CommentsPageState>>(emptyMap())
    val pages: StateFlow<Map<String, CommentsPageState>> = _pages.asStateFlow()

    private fun update(recipeId: String, transform: (CommentsPageState) -> CommentsPageState) {
        val current = _pages.value[recipeId] ?: CommentsPageState()
        _pages.value = _pages.value + (recipeId to transform(current))
    }

    /** No-op if this recipe's first page is already loaded/loading -- safe to call on every expand. */
    fun loadFirstPage(recipeId: String) {
        val state = _pages.value[recipeId]
        if (state != null && (state.comments.isNotEmpty() || state.loading || state.unavailable)) return
        load(recipeId, FIRST_PAGE_SIZE, append = false)
    }

    fun loadMore(recipeId: String) {
        val state = _pages.value[recipeId] ?: return
        if (state.exhausted || state.loading) return
        load(recipeId, MORE_PAGE_SIZE, append = true)
    }

    /** Clears the cached page for [recipeId] so the next loadFirstPage() re-fetches from scratch. */
    fun invalidate(recipeId: String) {
        _pages.value = _pages.value - recipeId
    }

    private fun load(recipeId: String, pageSize: Long, append: Boolean) {
        update(recipeId) { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                var query: Query = FirebaseFirestore.getInstance()
                    .collection("recipes").document(recipeId).collection("ratings")
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(pageSize)
                val lastSnapshot = _pages.value[recipeId]?.lastSnapshot
                if (append && lastSnapshot != null) query = query.startAfter(lastSnapshot)
                val snapshot = query.get().await()
                val newComments = snapshot.documents.map { doc ->
                    val createdAtMillis = (doc.get("createdAt") as? Timestamp)?.toDate()?.time
                    CommunityRecipeOperations.sanitizeRatingDoc(doc.data ?: emptyMap(), doc.id, createdAtMillis)
                }
                update(recipeId) { current ->
                    current.copy(
                        comments = if (append) current.comments + newComments else newComments,
                        lastSnapshot = snapshot.documents.lastOrNull() ?: current.lastSnapshot,
                        exhausted = newComments.size.toLong() < pageSize,
                        loading = false,
                        unavailable = false,
                    )
                }
            } catch (e: Exception) {
                update(recipeId) { current -> current.copy(loading = false, unavailable = current.comments.isEmpty()) }
            }
        }
    }
}
