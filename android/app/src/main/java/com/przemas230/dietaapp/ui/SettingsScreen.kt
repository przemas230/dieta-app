package com.przemas230.dietaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.logic.ProfileCalculations

/**
 * Ustawienia — profile card (FR-6) on top, Firebase connectivity smoke test
 * below (android/README.md "Co dalej"). Tabs (Konto/Wygląd/Przypomnienia/
 * Ulubione from index.html) are FR-71, not done yet — everything lives on
 * one scrollable screen until then.
 */
@Composable
fun SettingsScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    firebaseTestViewModel: FirebaseTestViewModel = viewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfileCard(profileViewModel)
        FirebaseTestCard(firebaseTestViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileCard(viewModel: ProfileViewModel) {
    val profile by viewModel.profile.collectAsState()

    var sex by remember(profile) { mutableStateOf(profile.sex) }
    var age by remember(profile) { mutableStateOf(if (profile.configured) profile.age.toString() else "") }
    var height by remember(profile) { mutableStateOf(if (profile.configured) profile.heightCm.toString() else "") }
    var weight by remember(profile) { mutableStateOf(if (profile.configured) profile.weightKg.toString() else "") }
    var targetWeight by remember(profile) { mutableStateOf(if (profile.configured) profile.targetWeightKg.toString() else "") }
    var activity by remember(profile) { mutableStateOf(profile.activity) }
    var goal by remember(profile) { mutableStateOf(profile.goal) }
    var activityMenuExpanded by remember { mutableStateOf(false) }
    var goalMenuExpanded by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("⚙️ Twój profil", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { sex = Sex.KOBIETA }) {
                    Text(if (sex == Sex.KOBIETA) "● Kobieta" else "○ Kobieta")
                }
                TextButton(onClick = { sex = Sex.MEZCZYZNA }) {
                    Text(if (sex == Sex.MEZCZYZNA) "● Mężczyzna" else "○ Mężczyzna")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Wiek (lata)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Wzrost (cm)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Waga obecna (kg)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = targetWeight,
                    onValueChange = { targetWeight = it },
                    label = { Text("Waga docelowa (kg)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }

            ExposedDropdownMenuBox(expanded = activityMenuExpanded, onExpandedChange = { activityMenuExpanded = it }) {
                OutlinedTextField(
                    value = activity.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Aktywność fizyczna") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                )
                DropdownMenu(expanded = activityMenuExpanded, onDismissRequest = { activityMenuExpanded = false }) {
                    ActivityLevel.entries.forEach { level ->
                        DropdownMenuItem(text = { Text(level.label) }, onClick = {
                            activity = level
                            activityMenuExpanded = false
                        })
                    }
                }
            }

            ExposedDropdownMenuBox(expanded = goalMenuExpanded, onExpandedChange = { goalMenuExpanded = it }) {
                OutlinedTextField(
                    value = goal.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cel") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = goalMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                )
                DropdownMenu(expanded = goalMenuExpanded, onDismissRequest = { goalMenuExpanded = false }) {
                    Goal.entries.forEach { g ->
                        DropdownMenuItem(text = { Text(g.label) }, onClick = {
                            goal = g
                            goalMenuExpanded = false
                        })
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val saved = Profile(
                        sex = sex,
                        age = age.toIntOrNull() ?: Profile().age,
                        heightCm = height.toIntOrNull() ?: Profile().heightCm,
                        weightKg = weight.toDoubleOrNull() ?: Profile().weightKg,
                        targetWeightKg = targetWeight.toDoubleOrNull() ?: Profile().targetWeightKg,
                        activity = activity,
                        goal = goal,
                    )
                    viewModel.save(saved)
                    val t = ProfileCalculations.calcTargets(saved)
                    resultText = "Dopasowano: ${t.daily} kcal/dzień (śniadanie ${t.sniadania}, " +
                        "II śniadanie ${t.drugie}, obiad ${t.obiady}, kolacja ${t.kolacje}, " +
                        "deser/przekąska ${t.deser})."
                }) {
                    Text("Zapisz i dopasuj dietę")
                }
                TextButton(onClick = {
                    viewModel.resetToDefault()
                    resultText = "Przywrócono ustawienia domyślne."
                }) {
                    Text("Domyślne")
                }
            }

            if (resultText.isNotEmpty()) {
                Text(resultText, style = MaterialTheme.typography.bodySmall)
            } else if (!profile.configured) {
                Text(
                    "👋 Uzupełnij swoje dane powyżej i zapisz, żeby dopasować dietę do siebie.",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun FirebaseTestCard(viewModel: FirebaseTestViewModel) {
    val state by viewModel.uiState.collectAsState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("Test połączenia z Firebase", style = MaterialTheme.typography.titleMedium)
            Text(
                "Logowanie anonimowe + zapis i odczyt pola \"debugPing\" w " +
                    "users/{uid} — ten sam projekt Firebase co wersja webowa " +
                    "(dieta-app-323b4).",
                style = MaterialTheme.typography.bodySmall,
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
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}
