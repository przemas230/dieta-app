package com.przemas230.dietaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * First real content behind the Ustawienia action in the top bar — for now
 * just the Firebase connectivity smoke test (android/README.md "Co dalej"
 * krok 2). Profil, motyw, wylogowanie itd. (FR-6, FR-79...) dochodzą w
 * kolejnych krokach, dopiero gdy to potwierdzi, że Firestore realnie działa
 * z tym projektem Android.
 */
@Composable
fun SettingsScreen(viewModel: FirebaseTestViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Test połączenia z Firebase", style = MaterialTheme.typography.titleMedium)
        Text(
            "Logowanie anonimowe + zapis i odczyt pola \"debugPing\" w " +
                "users/{uid} — ten sam projekt Firebase co wersja webowa " +
                "(dieta-app-323b4).",
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )

        Button(onClick = { viewModel.runTest() }, enabled = !state.isLoading) {
            Text(if (state.isLoading) "Testowanie…" else "Testuj Firebase")
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }

        state.uid?.let { uid ->
            Text("UID: $uid", style = MaterialTheme.typography.bodySmall)
        }
        state.lastPingValue?.let { value ->
            Text("Odczytana wartość debugPing: $value", style = MaterialTheme.typography.bodySmall)
        }
        state.error?.let { error ->
            Text(
                "Błąd: $error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}
