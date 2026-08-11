package com.przemas230.dietaapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.PublicProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** "12 lutego 2026" -- port of index.html's formatLastLogin (toLocaleDateString pl-PL). */
private fun formatLastLogin(millis: Long?): String {
    if (millis == null) return "brak danych"
    return SimpleDateFormat("d MMMM yyyy", Locale("pl", "PL")).format(Date(millis))
}

/**
 * FR-76: "👥 Przeglądaj użytkowników" -- list of every real (non-anonymous)
 * account that has ever signed in, newest login first, port of
 * openUserListModal (index.html:3097-3122) as a full screen instead of a
 * web modal (consistent with every other Android destination).
 */
@Composable
fun UserListScreen(onBack: () -> Unit, onOpenProfile: (uid: String, displayName: String) -> Unit, viewModel: UserListViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = onBack, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text("← Wróć")
        }
        Text(
            "👥 Użytkownicy społeczności",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        when (val current = state) {
            is UserListState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wczytywanie…")
            }
            is UserListState.Unavailable -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Lista użytkowników jest teraz niedostępna (brak połączenia z chmurą albo\n" +
                        "reguły Firestore nie zostały jeszcze skonfigurowane).",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(24.dp),
                )
            }
            is UserListState.Loaded -> {
                if (current.users.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nikt jeszcze nie zalogował się na prawdziwe konto.")
                    }
                } else {
                    LazyColumn {
                        items(current.users, key = PublicProfile::uid) { user ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenProfile(user.uid, user.displayName) }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                            ) {
                                Text(user.displayName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "Ostatnio zalogowany: ${formatLastLogin(user.lastLoginAtMillis)}",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
