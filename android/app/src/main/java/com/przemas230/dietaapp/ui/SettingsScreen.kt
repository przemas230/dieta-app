package com.przemas230.dietaapp.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.przemas230.dietaapp.R
import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.logic.AppThemes
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.UiScale
import kotlin.math.roundToInt

/** FR-71: the 4 pill tabs, in index.html's own order. */
private enum class SettingsTab(val emoji: String, val label: String) {
    KONTO("👤", "Konto"),
    WYGLAD("🎨", "Wygląd"),
    PRZYPOMNIENIA("💧", "Przypomnienia"),
    ULUBIONE("⭐", "Ulubione"),
}

/**
 * Ustawienia — FR-71's 4 topic tabs (pill row, same visual style as the
 * Przepisy category pills), each showing only its own cards instead of one
 * long scrolling list. `AppUpdateCard` has no web-tab equivalent (Android's
 * own APK-update checker, nothing like it exists in the PWA, which updates
 * via its Service Worker instead -- FR-53, N/D for native) so it lives above
 * the tabs rather than being force-fit into one of the four.
 */
@Composable
fun SettingsScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    appUpdateViewModel: AppUpdateViewModel = viewModel(),
    uiScaleViewModel: UiScaleViewModel = viewModel(),
    swipeRatingStyleViewModel: SwipeRatingStyleViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel(),
    effectiveUiScale: Double = 1.0,
) {
    // FR-71: always starts on Konto -- plain remember (no key/ViewModel
    // backing), so leaving and re-entering the Ustawienia screen discards it,
    // matching "stan poprzednio wybranej zakładki nie jest pamiętany między otwarciami".
    var selectedTab by remember { mutableStateOf(SettingsTab.KONTO) }
    // FR-71: "zmiana zakładek nie resetuje niezapisanych zmian w formularzu
    // profilu" -- switching selectedTab normally removes the other branches'
    // composables from the tree entirely (Compose doesn't keep `remember`
    // state for content that stops being emitted), which would silently
    // discard anything typed into ProfileCard the moment you leave Konto.
    // SaveableStateHolder is the standard fix (the same mechanism
    // ViewPager/Pager tabs use): each tab's rememberSaveable state is
    // stashed when its branch stops being composed and restored intact when
    // you come back, without keeping all 4 tabs permanently composed.
    val tabStateHolder = rememberSaveableStateHolder()

    Column(modifier = Modifier.fillMaxSize()) {
        AppUpdateCard(appUpdateViewModel, modifier = Modifier.padding(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(SettingsTab.entries) { tab ->
                FilterChip(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    label = { Text("${tab.emoji} ${tab.label}") },
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            tabStateHolder.SaveableStateProvider(selectedTab.name) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    when (selectedTab) {
                        SettingsTab.KONTO -> {
                            // FR-71: "Twoja nazwa w aplikacji" lives physically at
                            // the top of ProfileCard itself now, not a separate card
                            // above it (that was the pre-FR-71 layout).
                            ProfileCard(profileViewModel)
                            CloudAccountCard(authViewModel)
                        }
                        SettingsTab.WYGLAD -> {
                            ThemeCard(themeViewModel)
                            UiScaleCard(uiScaleViewModel, effectiveUiScale)
                            SwipeRatingStyleCard(swipeRatingStyleViewModel)
                        }
                        SettingsTab.PRZYPOMNIENIA -> {
                            Text(
                                "Przypomnienia o piciu wody i diagnostyka powiadomień jeszcze nie działają w aplikacji na Androida (FR-38/FR-39).",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        SettingsTab.ULUBIONE -> {
                            Text(
                                "Ulubione składniki jeszcze nie działają w aplikacji na Androida (FR-32).",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * FR-48: one-to-one port of index.html's `renderThemePicker` -- a grid of
 * pill buttons, each with the theme's own swatch color as a small dot plus
 * its label, same 11 themes/order/labels as `THEMES` there. Tapping applies
 * immediately (no separate "Zapisz"), matching "Zmiana motywu jest
 * natychmiastowa" (FR-48's first acceptance criterion) -- the second
 * criterion ("wszystkie zaokrąglone przyciski mają promień 16px") is already
 * satisfied for free by Material3's default filled/outlined Button shape
 * (a full pill at this button height, i.e. >16dp corner radius), so nothing
 * extra was needed there. FR-49/FR-63's structural, non-palette differences
 * (Polaroid card tilt+sharp corners, Kafelki accent stripe) live in
 * RecipeListScreen.kt's RecipeCard, reading LocalDietaThemeId.
 */
@Composable
private fun ThemeCard(viewModel: ThemeViewModel) {
    val themeId by viewModel.themeId.collectAsState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("🎨 Motyw kolorystyczny", style = MaterialTheme.typography.titleMedium)
            ThemeGrid(selectedId = themeId, onSelect = { viewModel.setTheme(it) })
            Text(
                "Motywy „Polaroid” i „Kafelki” zauważalnie zmieniają też kształt kart przepisów, nie tylko kolory.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemeGrid(selectedId: String, onSelect: (String) -> Unit) {
    // FlowRow (not a plain Row+chunked) -- same fix as PlanPickerDialog in
    // RecipeListScreen.kt (FR-18/19/20): 11 pill buttons never fit one row,
    // and a non-wrapping Row would squash the overflowing ones instead of
    // flowing to a new line.
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppThemes.ALL.forEach { theme ->
            FilterChip(
                selected = theme.id == selectedId,
                onClick = { onSelect(theme.id) },
                label = { Text(theme.label) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color(theme.swatch)),
                    )
                },
            )
        }
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
private fun AppUpdateCard(viewModel: AppUpdateViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()

    Card(modifier = modifier.fillMaxWidth()) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileCard(viewModel: ProfileViewModel) {
    val profile by viewModel.profile.collectAsState()
    val displayName by viewModel.displayName.collectAsState()

    // FR-71: rememberSaveable (not plain remember) so SettingsScreen's
    // SaveableStateHolder can restore in-progress edits after a tab switch
    // and back -- see the "zmiana zakładek nie resetuje..." comment there.
    var sex by rememberSaveable(profile) { mutableStateOf(profile.sex) }
    var age by rememberSaveable(profile) { mutableStateOf(if (profile.configured) profile.age.toString() else "") }
    var height by rememberSaveable(profile) { mutableStateOf(if (profile.configured) profile.heightCm.toString() else "") }
    var weight by rememberSaveable(profile) { mutableStateOf(if (profile.configured) profile.weightKg.toString() else "") }
    var targetWeight by rememberSaveable(profile) { mutableStateOf(if (profile.configured) profile.targetWeightKg.toString() else "") }
    var activity by rememberSaveable(profile) { mutableStateOf(profile.activity) }
    var goal by rememberSaveable(profile) { mutableStateOf(profile.goal) }
    var glutenFree by rememberSaveable(profile) { mutableStateOf(profile.glutenFree) }
    var lactoseFree by rememberSaveable(profile) { mutableStateOf(profile.lactoseFree) }
    var strictLowGI by rememberSaveable(profile) { mutableStateOf(profile.strictLowGI) }
    var activityMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var goalMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var resultText by rememberSaveable { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("⚙️ Twój profil", style = MaterialTheme.typography.titleMedium)

            // FR-65/FR-71: saves on every keystroke (own ViewModel field, no
            // "Zapisz" button, untouched by "Domyślne" below) -- physically
            // the first field in this card, per FR-71's acceptance criteria.
            OutlinedTextField(
                value = displayName,
                onValueChange = { viewModel.setDisplayName(it) },
                label = { Text("Twoja nazwa w aplikacji") },
                placeholder = { Text("np. Przemek (opcjonalnie)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

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

/**
 * FR-69 (e-mail/hasło) + FR-73: real, non-anonymous sign-in and the account
 * status the rest of the app keys sync off of (CloudSyncCoordinator).
 * Anonymous is always the fallback state (AuthViewModel re-signs-in
 * anonymously right after signOut()), so this card only ever needs to
 * offer "sign in/up" (Anonymous) or "sign out" (SignedIn) -- never a
 * "log out completely" option, matching FR-69's "nigdy nie zostaje w
 * stanie niezalogowany całkowicie".
 */
@Composable
private fun CloudAccountCard(viewModel: AuthViewModel) {
    val authState by viewModel.state.collectAsState()
    val busy by viewModel.busy.collectAsState()
    val error by viewModel.error.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    // FR-69 (Google slice): requestIdToken needs the project's "web" OAuth
    // client (client_type 3 in google-services.json) -- the google-services
    // Gradle plugin auto-generates this string resource from that same
    // entry, so it's always in sync with whatever google-services.json is
    // currently checked in, no hardcoded client ID to go stale.
    val googleSignInClient = remember {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, options)
    }
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.signInWithGoogleIdToken(idToken)
            } else {
                viewModel.reportError("Google nie zwróciło tokenu logowania.")
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                // User closed the account picker / backed out -- not an error, stay silent.
                com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> {}
                // Most common real-world cause: this build's SHA-1 signing
                // fingerprint isn't registered against the Firebase project's
                // Android app yet (Firebase Console → Project settings →
                // Android app → "Add fingerprint") -- surfaced to the user
                // instead of a bare status code, since "DEVELOPER_ERROR"
                // alone means nothing to them.
                com.google.android.gms.common.api.CommonStatusCodes.DEVELOPER_ERROR ->
                    viewModel.reportError(
                        "Logowanie Google nie jest jeszcze skonfigurowane dla tego builda " +
                            "(brak zarejestrowanego odcisku SHA-1 w konsoli Firebase).",
                    )
                else -> viewModel.reportError("Nie udało się zalogować przez Google (${e.statusCode}).")
            }
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("☁️ Konto w chmurze", style = MaterialTheme.typography.titleMedium)
            when (val s = authState) {
                is AuthState.Unavailable -> Text(
                    "Chmura niedostępna: ${s.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
                is AuthState.Loading -> CircularProgressIndicator()
                is AuthState.SignedIn -> {
                    Text("Zalogowano jako: ${s.email ?: s.uid}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Profil, spiżarnia, motyw, skala interfejsu, styl oceniania i nazwa w aplikacji " +
                            "synchronizują się automatycznie między urządzeniami zalogowanymi tym kontem.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    TextButton(onClick = { viewModel.signOut() }) { Text("🚪 Wyloguj się z tego urządzenia") }
                }
                is AuthState.Anonymous -> {
                    Text(
                        "Nie jesteś zalogowany — dane zostają wyłącznie na tym urządzeniu. Zaloguj się " +
                            "albo załóż konto, żeby synchronizować je między urządzeniami.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Button(
                        onClick = { googleSignInLauncher.launch(googleSignInClient.signInIntent) },
                        enabled = !busy,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Zaloguj przez Google") }
                    Text("— albo e-mailem i hasłem —", style = MaterialTheme.typography.bodySmall)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Hasło") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    val canSubmit = !busy && email.isNotBlank() && password.length >= 6
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.signIn(email, password) },
                            enabled = canSubmit,
                            modifier = Modifier.weight(1f),
                        ) { Text("Zaloguj się") }
                        TextButton(
                            onClick = { viewModel.signUp(email, password) },
                            enabled = canSubmit,
                            modifier = Modifier.weight(1f),
                        ) { Text("Załóż konto") }
                    }
                    if (busy) CircularProgressIndicator()
                    error?.let {
                        Text(
                            "Błąd: $it",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
        }
    }
}
