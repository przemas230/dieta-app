package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * FR-69 (e-mail/hasło slice) + FR-73: mirrors index.html's onAuthStateChanged
 * bootstrap -- the app is ALWAYS signed in to *some* Firebase user (anonymous
 * by default, matching FR-69's "użytkownik nigdy nie zostaje w stanie
 * niezalogowany całkowicie"), and cloud sync (CloudSyncCoordinator) only
 * activates once that user is a real, non-anonymous account.
 */
sealed class AuthState {
    data object Loading : AuthState()
    data class Anonymous(val uid: String) : AuthState()
    data class SignedIn(val uid: String, val email: String?) : AuthState()
    data class Unavailable(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var auth: FirebaseAuth? = null
    private val authStateListener = FirebaseAuth.AuthStateListener { a ->
        val user = a.currentUser
        when {
            user == null -> {
                // Signed out (FR-79) or very first launch -- re-bootstrap
                // anonymously so the app is never left with no user at all,
                // same as index.html's own onAuthStateChanged.
                _state.value = AuthState.Loading
                a.signInAnonymously()
            }
            user.isAnonymous -> _state.value = AuthState.Anonymous(user.uid)
            else -> _state.value = AuthState.SignedIn(user.uid, user.email)
        }
    }

    init {
        try {
            val instance = FirebaseAuth.getInstance()
            auth = instance
            instance.addAuthStateListener(authStateListener)
        } catch (e: IllegalStateException) {
            val notInitialized = e.message?.contains("FirebaseApp", ignoreCase = true) == true
            _state.value = AuthState.Unavailable(
                if (notInitialized) {
                    "Brak pliku konfiguracyjnego Firebase (android/app/google-services.json)."
                } else {
                    "Nie udało się połączyć z Firebase: ${e.message ?: e.toString()}"
                },
            )
        }
    }

    fun signUp(email: String, password: String) {
        val a = auth ?: return
        _busy.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                a.createUserWithEmailAndPassword(email.trim(), password).await()
            } catch (e: Exception) {
                _error.value = e.message ?: "Nie udało się założyć konta."
            } finally {
                _busy.value = false
            }
        }
    }

    fun signIn(email: String, password: String) {
        val a = auth ?: return
        _busy.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                a.signInWithEmailAndPassword(email.trim(), password).await()
            } catch (e: Exception) {
                _error.value = e.message ?: "Nie udało się zalogować."
            } finally {
                _busy.value = false
            }
        }
    }

    /**
     * FR-69 (Google slice): exchanges a Google ID token (obtained by the
     * caller via GoogleSignInClient's own sign-in intent -- Firebase Auth
     * has no Android UI of its own for this, unlike email/password) for a
     * Firebase credential. Requires the app's SHA-1 signing fingerprint to
     * be registered against this Firebase project's Android app (Firebase
     * Console → Project settings → your Android app → "Add fingerprint")
     * -- without it, Google's sign-in flow itself fails before ever
     * reaching this method (surfaced by the caller as a DEVELOPER_ERROR).
     */
    fun signInWithGoogleIdToken(idToken: String) {
        val a = auth ?: return
        _busy.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                a.signInWithCredential(credential).await()
            } catch (e: Exception) {
                _error.value = e.message ?: "Nie udało się zalogować przez Google."
            } finally {
                _busy.value = false
            }
        }
    }

    /** Lets Compose-side code (e.g. Google Sign-In's own ActivityResult flow) surface an error through the same channel as signIn/signUp. */
    fun reportError(message: String) {
        _error.value = message
    }

    /** FR-79: signs out of the real account -- authStateListener above immediately re-signs-in anonymously. */
    fun signOut() {
        _error.value = null
        auth?.signOut()
    }

    override fun onCleared() {
        auth?.removeAuthStateListener(authStateListener)
        super.onCleared()
    }
}
