package com.przemas230.dietaapp.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.UiScale
import kotlin.math.roundToInt

/**
 * Ustawienia — aktualizacja aplikacji, profil (FR-6), skalowanie interfejsu
 * (FR-14) i test połączenia z Firebase (android/README.md "Co dalej"). Tabs
 * (Konto/Wygląd/Przypomnienia/Ulubione from index.html) are FR-71, not done
 * yet — everything lives on one scrollable screen until then.
 */
@Composable
fun SettingsScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    firebaseTestViewModel: FirebaseTestViewModel = viewModel(),
    appUpdateViewModel: AppUpdateViewModel = viewModel(),
    uiScaleViewModel: UiScaleViewModel = viewModel(),
    swipeRatingStyleViewModel: SwipeRatingStyleViewModel = viewModel(),
    effectiveUiScale: Double = 1.0,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AppUpdateCard(appUpdateViewModel)
        DisplayNameCard(profileViewModel)
        ProfileCard(profileViewModel)
        UiScaleCard(uiScaleViewModel, effectiveUiScale)
        SwipeRatingStyleCard(swipeRatingStyleViewModel)
        FirebaseTestCard(firebaseTestViewModel)
    }
}

@Composable
private fun UiScaleCard(viewModel: UiScaleViewModel, effectiveScale: Double) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("🔍 Rozmiar aplikacji", style = MaterialTheme.typography.titleMedium)
                Text("${(effectiveScale * 100).roundToInt()}%", style = MaterialTheme.typography.titleMedium)
            }
            Slider(
                value = effectiveScale.toFloat(),
                onValueChange = { viewModel.setScale(it.toDouble()) },
                valueRange = UiScale.MIN.toFloat()..UiScale.MAX.toFloat(),
                steps = ((UiScale.MAX - UiScale.MIN) / UiScale.STEP).roundToInt() - 1,
            )
            Text(
                "Jeśli elementy aplikacji wyglądają za duże (albo za małe), dostosuj tutaj. Wartość " +
                    "początkowa jest dobierana automatycznie na podstawie szerokości ekranu, ale to tylko " +
                    "przybliżenie (aplikacja nie ma dostępu do systemowego ustawienia \"Rozmiar wyświetlacza\") " +
                    "— swobodnie ją zmień, jeśli nie pasuje.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

/** FR-61: two-way toggle for the FR-55/56 swipe-drag feedback style, independent of the color theme (FR-48, not done yet). */
@Composable
private fun SwipeRatingStyleCard(viewModel: SwipeRatingStyleViewModel) {
    val style by viewModel.style.collectAsState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("🎈 Styl oceniania kart przesunięciem", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { viewModel.setStyle(SwipeRatingStyle.BALLOON) }) {
                    Text(if (style == SwipeRatingStyle.BALLOON) "● Balonowa czcionka" else "○ Balonowa czcionka")
                }
                TextButton(onClick = { viewModel.setStyle(SwipeRatingStyle.GLOW) }) {
                    Text(if (style == SwipeRatingStyle.GLOW) "● Kolorowa karta" else "○ Kolorowa karta")
                }
            }
            Text(
                "Wpływa wyłącznie na kartę podczas samego przesuwania (FR-55) — karta w spoczynku wygląda tak samo w obu stylach.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun AppUpdateCard(viewModel: AppUpdateViewModel) {
    val state by viewModel.state.collectAsState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("🔄 Aktualizacja aplikacji", style = MaterialTheme.typography.titleMedium)
            Text(
                "Zainstalowana wersja: ${viewModel.installedVersionName}",
                style = MaterialTheme.typography.bodySmall,
            )

            Button(
                onClick = { viewModel.checkForUpdate() },
                enabled = state !is UpdateState.Checking && state !is UpdateState.Downloading,
            ) {
                Text("Sprawdź aktualizację")
            }

            when (val s = state) {
                is UpdateState.Checking -> {
                    CircularProgressIndicator()
                    Text("Sprawdzanie…", style = MaterialTheme.typography.bodySmall)
                }
                is UpdateState.Downloading -> {
                    CircularProgressIndicator()
                    Text("Pobieranie aktualizacji…", style = MaterialTheme.typography.bodySmall)
                }
                is UpdateState.UpToDate -> Text(
                    "Masz najnowszą wersję (${s.versionName}).",
                    style = MaterialTheme.typography.bodySmall,
                )
                is UpdateState.UpdateAvailable -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Dostępna aktualizacja: wersja ${s.versionName}.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Button(onClick = { viewModel.downloadAndInstall(s.apkUrl) }) {
                        Text("Pobierz i zainstaluj")
                    }
                }
                is UpdateState.Error -> Text(
                    "Błąd: ${s.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
                UpdateState.Idle -> {}
            }
        }
    }
}

/**
 * FR-65: independent of the diet-profile form below (own OutlinedTextField,
 * own ViewModel field) -- port of index.html's "👤 Konto" card setDisplayName
 * input, minus the tab grouping (FR-71, not done yet). Saves on every
 * keystroke, no "Zapisz" button, and untouched by ProfileCard's "Domyślne"
 * reset.
 */
@Composable
private fun DisplayNameCard(viewModel: ProfileViewModel) {
    val displayName by viewModel.displayName.collectAsState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("👤 Konto", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = displayName,
                onValueChange = { viewModel.setDisplayName(it) },
                label = { Text("Twoja nazwa w aplikacji") },
                placeholder = { Text("np. Przemek (opcjonalnie)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
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
    var glutenFree by remember(profile) { mutableStateOf(profile.glutenFree) }
    var lactoseFree by remember(profile) { mutableStateOf(profile.lactoseFree) }
    var strictLowGI by remember(profile) { mutableStateOf(profile.strictLowGI) }
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

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { glutenFree = !glutenFree }) {
                Checkbox(checked = glutenFree, onCheckedChange = { glutenFree = it })
                Text(
                    "Ukryj dania zawierające gluten (pieczywo, kasze glutenowe) — filtr orientacyjny",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { lactoseFree = !lactoseFree }) {
                Checkbox(checked = lactoseFree, onCheckedChange = { lactoseFree = it })
                Text(
                    "Ukryj dania z nabiałem bez wyraźnej wersji „bez laktozy”",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { strictLowGI = !strictLowGI }) {
                Checkbox(checked = strictLowGI, onCheckedChange = { strictLowGI = it })
                Text(
                    "Trzymaj się niskiego indeksu glikemicznego",
                    style = MaterialTheme.typography.bodySmall,
                )
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
                        glutenFree = glutenFree,
                        lactoseFree = lactoseFree,
                        strictLowGI = strictLowGI,
                    )
                    viewModel.save(saved)
                    val t = ProfileCalculations.calcTargets(saved)
                    val m = ProfileCalculations.calcMacroTargets(saved).daily
                    resultText = "Dopasowano: ${t.daily} kcal/dzień (śniadanie ${t.sniadania}, " +
                        "II śniadanie ${t.drugie}, obiad ${t.obiady}, kolacja ${t.kolacje}, " +
                        "deser/przekąska ${t.deser}). Makra na dzień: ${m.protein} g białka, " +
                        "${m.carbs} g węglowodanów, ${m.fat} g tłuszczu."
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
