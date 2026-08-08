package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class FirebaseTestUiState(
    val isLoading: Boolean = false,
    val uid: String? = null,
    val lastPingValue: String? = null,
    val error: String? = null,
)

/**
 * End-to-end check that this Android project really reaches the same
 * Firestore project as the web app: anonymous sign-in, then a round-trip
 * write/read of a "debugPing" field on users/{uid} — mirrors the web app's
 * onAuthStateChanged anonymous-sign-in bootstrap in index.html.
 *
 * Firebase instances are fetched lazily inside runTest()'s try/catch rather
 * than as class properties, so opening this screen before
 * android/app/google-services.json exists shows a friendly Polish error
 * instead of crashing the app (FirebaseAuth.getInstance() throws if no
 * default FirebaseApp was initialized).
 */
class FirebaseTestViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FirebaseTestUiState())
    val uiState: StateFlow<FirebaseTestUiState> = _uiState.asStateFlow()

    fun runTest() {
        viewModelScope.launch {
            _uiState.value = FirebaseTestUiState(isLoading = true)
            try {
                val auth = FirebaseAuth.getInstance()
                val db = FirebaseFirestore.getInstance()

                val user = auth.currentUser ?: auth.signInAnonymously().await().user
                val uid = requireNotNull(user?.uid) { "Brak UID po zalogowaniu" }

                val pingValue = System.currentTimeMillis().toString()
                db.collection("users").document(uid)
                    .set(mapOf("debugPing" to pingValue), SetOptions.merge())
                    .await()

                val snapshot = db.collection("users").document(uid).get().await()
                val readBack = snapshot.getString("debugPing")

                _uiState.value = FirebaseTestUiState(
                    isLoading = false,
                    uid = uid,
                    lastPingValue = readBack,
                    error = if (readBack != pingValue) {
                        "Zapisano, ale odczytana wartość nie zgadza się z zapisaną."
                    } else {
                        null
                    },
                )
            } catch (e: Exception) {
                _uiState.value = FirebaseTestUiState(isLoading = false, error = e.message ?: e.toString())
            }
        }
    }
}
