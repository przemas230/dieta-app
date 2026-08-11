package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.przemas230.dietaapp.data.PublicProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class UserListState {
    data object Loading : UserListState()
    data class Loaded(val users: List<PublicProfile>) : UserListState()
    data object Unavailable : UserListState()
}

/** FR-76: "👥 Przeglądaj użytkowników" -- port of openUserListModal (index.html:3097-3122). */
class UserListViewModel : ViewModel() {
    private val _state = MutableStateFlow<UserListState>(UserListState.Loading)
    val state: StateFlow<UserListState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UserListState.Loading
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance().collection("publicProfiles")
                    .orderBy("lastLoginAt", Query.Direction.DESCENDING)
                    .limit(50)
                    .get()
                    .await()
                val users = snapshot.documents.map { doc ->
                    PublicProfile(
                        uid = doc.id,
                        displayName = doc.getString("displayName")?.takeIf { it.isNotBlank() } ?: "Anonimowy użytkownik",
                        lastLoginAtMillis = (doc.get("lastLoginAt") as? Timestamp)?.toDate()?.time,
                    )
                }
                _state.value = UserListState.Loaded(users)
            } catch (e: Exception) {
                _state.value = UserListState.Unavailable
            }
        }
    }
}
