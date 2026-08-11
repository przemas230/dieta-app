package com.przemas230.dietaapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.Recipe
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun formatLastLogin(millis: Long?): String {
    if (millis == null) return "brak danych"
    return SimpleDateFormat("d MMMM yyyy", Locale("pl", "PL")).format(Date(millis))
}

/**
 * FR-76: port of openUserProfileModal (index.html:3123-3159) -- pseudonim,
 * data ostatniego logowania, zatwierdzone przepisy społeczności, oceniane
 * przepisy. Deliberately NEVER shows email/diet profile/pantry/favorites.
 */
@Composable
fun UserProfileScreen(
    uid: String,
    fallbackDisplayName: String?,
    onBack: () -> Unit,
    viewModel: UserProfileViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(uid) { viewModel.load(uid, fallbackDisplayName) }

    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = onBack, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text("← Wróć")
        }
        when (val current = state) {
            is UserProfileState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wczytywanie…")
            }
            is UserProfileState.Unavailable -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Ten profil jest teraz niedostępny (brak połączenia z chmurą albo\n" +
                        "reguły Firestore nie zostały jeszcze skonfigurowane).",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(24.dp),
                )
            }
            is UserProfileState.Loaded -> {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    item {
                        Text(current.profile.displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "Ostatnio zalogowany: ${formatLastLogin(current.profile.lastLoginAtMillis)}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            "🌍 Zatwierdzone przepisy społeczności",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                        )
                    }
                    if (current.approvedRecipes.isEmpty()) {
                        item { Text("Brak zatwierdzonych przepisów.", style = MaterialTheme.typography.bodySmall) }
                    } else {
                        items(current.approvedRecipes, key = Recipe::id) { recipe ->
                            Text("• ${recipe.name}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Text(
                            "⭐ Oceniane przepisy",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                    if (current.reviewedRecipes.isEmpty()) {
                        item { Text("Brak ocenionych przepisów.", style = MaterialTheme.typography.bodySmall) }
                    } else {
                        items(current.reviewedRecipes, key = { it.recipeId }) { reviewed ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    "${"★".repeat(reviewed.stars)}${"☆".repeat(5 - reviewed.stars)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                val comment = reviewed.comment
                                if (comment != null) {
                                    Text(comment, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
