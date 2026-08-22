package com.przemas230.dietaapp.ui

import android.os.Build
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.przemas230.dietaapp.logic.PantryTiles
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.UiScale
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

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
    waterNotificationViewModel: WaterNotificationViewModel = viewModel(),
    // Read fresh from waterViewModel by MainActivity on every recomposition
    // -- needed so "Włącz powiadomienie" can seed the notification with
    // today's actual count instead of always starting it at 0.
    currentWaterCount: Int = 0,
    favoriteIngredientsViewModel: FavoriteIngredientsViewModel = viewModel(),
    // Same reason as currentWaterCount above: PantryTiles.buildTileNames
    // needs the full recipe list, which only MainActivity's PlannerViewModel
    // already has loaded, not something SettingsScreen fetches itself.
    allRecipes: List<com.przemas230.dietaapp.data.Recipe> = emptyList(),
    // 2026-08-11: hoisted (not default viewModel() params) so ProfileCard's
    // post-save "regenerate the week plan + shopping list" prompt acts on
    // the SAME shared instances the Planer/Zakupy tabs themselves show.
    plannerViewModel: PlannerViewModel = viewModel(),
    shoppingViewModel: ShoppingViewModel = viewModel(),
    effectiveUiScale: Double = 1.0,
    // FR-79: resets every local ViewModel to fresh-install defaults --
    // needs to reach ALL of them, not just what this screen otherwise
    // touches, so MainActivity (which already hoists every ViewModel)
    // supplies this rather than SettingsScreen collecting them all itself.
    onClearLocalData: () -> Unit = {},
    // FR-68/76: hoisted (not a default viewModel() param) so this screen
    // shares the same instance MainActivity/CommunityCoordinator use --
    // toggling here must be visible to the recipe list immediately.
    recipeViewModel: RecipeViewModel = viewModel(),
    // FR-76/v2: "Moje przepisy" status + moderator-only approval, see
    // RecipeModerationCoordinator's doc comment.
    recipeModerationViewModel: RecipeModerationViewModel = viewModel(),
    onBrowseUsers: () -> Unit = {},
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
                            ProfileCard(profileViewModel, plannerViewModel, shoppingViewModel, allRecipes)
                            CloudAccountCard(authViewModel, onClearLocalData)
                            CommunityRecipesCard(recipeViewModel, onBrowseUsers)
                            MyRecipesCard(recipeViewModel, recipeModerationViewModel)
                            RecipeModerationCard(authViewModel, recipeModerationViewModel)
                        }
                        SettingsTab.WYGLAD -> {
                            ThemeCard(themeViewModel)
                            UiScaleCard(uiScaleViewModel, effectiveUiScale)
                            SwipeRatingStyleCard(swipeRatingStyleViewModel)
                        }
                        SettingsTab.PRZYPOMNIENIA -> {
                            WaterNotificationCard(waterNotificationViewModel, currentWaterCount)
                            WaterReminderCard(waterNotificationViewModel)
                            WaterNotificationLogCard(waterNotificationViewModel)
                        }
                        SettingsTab.ULUBIONE -> {
                            FavoriteIngredientsCard(favoriteIngredientsViewModel, allRecipes)
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
private fun ProfileCard(
    viewModel: ProfileViewModel,
    plannerViewModel: PlannerViewModel,
    shoppingViewModel: ShoppingViewModel,
    allRecipes: List<com.przemas230.dietaapp.data.Recipe>,
) {
    val profile by viewModel.profile.collectAsState()
    val displayName by viewModel.displayName.collectAsState()
    // 2026-08-11, on explicit user request: after saving a changed profile,
    // offer to regenerate the week plan (and, separately, the shopping
    // list) to match the just-recalculated diet -- port of index.html's
    // saveSettingsBtn handler extension. Two SEPARATE confirmations, not
    // one combined dialog, since generating a new plan and adding it to
    // the shopping list are different-enough consequences (one overwrites
    // the Planer, the other overwrites the Zakupy list).
    var pendingProfileForRegenerate by remember { mutableStateOf<Profile?>(null) }
    var showAddToShoppingConfirm by remember { mutableStateOf(false) }

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
                    pendingProfileForRegenerate = saved
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

    pendingProfileForRegenerate?.let { pendingProfile ->
        AlertDialog(
            onDismissRequest = { pendingProfileForRegenerate = null },
            title = { Text("Zaktualizowano dietę") },
            text = {
                Text(
                    "Czy wygenerować nowy plan posiłków na cały tydzień dopasowany do nowej diety? " +
                        "Nadpisze obecnie zaplanowane dania i wyczyści listę zakupów.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    plannerViewModel.randomizeWeek(pendingProfile)
                    shoppingViewModel.clearAll()
                    pendingProfileForRegenerate = null
                    showAddToShoppingConfirm = true
                }) { Text("Tak, wygeneruj") }
            },
            dismissButton = {
                TextButton(onClick = { pendingProfileForRegenerate = null }) { Text("Nie") }
            },
        )
    }
    if (showAddToShoppingConfirm) {
        AlertDialog(
            onDismissRequest = { showAddToShoppingConfirm = false },
            title = { Text("Nowy plan gotowy") },
            text = { Text("Dodać składniki nowego planu do listy zakupów?") },
            confirmButton = {
                TextButton(onClick = {
                    val recipesById = allRecipes.associateBy { it.id }
                    shoppingViewModel.addWeekPlan(plannerViewModel.weekPlan.value, recipesById)
                    showAddToShoppingConfirm = false
                }) { Text("Tak, dodaj") }
            },
            dismissButton = {
                TextButton(onClick = { showAddToShoppingConfirm = false }) { Text("Nie") }
            },
        )
    }
}

/**
 * FR-68/76: "🌍 Przepisy społeczności" -- a working, persisted/synced toggle
 * (functional regardless of whether the effect is visible yet) plus
 * "👥 Przeglądaj użytkowników". Port of index.html's community-recipes
 * settings card. Deliberately does NOT offer any "dołącz do gospodarstwa"/
 * "udostępnij spiżarnię" form -- that's FR-78's still-unported territory
 * (see FR-68's own acceptance criteria on not promising more than works).
 */
@Composable
private fun CommunityRecipesCard(viewModel: RecipeViewModel, onBrowseUsers: () -> Unit) {
    val enabled by viewModel.communityRecipesEnabled.collectAsState()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("🌍 Przepisy społeczności", style = MaterialTheme.typography.titleMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { viewModel.setCommunityRecipesEnabled(!enabled) },
            ) {
                Checkbox(checked = enabled, onCheckedChange = { viewModel.setCommunityRecipesEnabled(it) })
                Text(
                    "Pokazuj przepisy dodane przez innych użytkowników",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            OutlinedButton(onClick = onBrowseUsers, modifier = Modifier.fillMaxWidth()) {
                Text("👥 Przeglądaj użytkowników")
            }
        }
    }
}

private fun recipeStatusLabel(status: String?): String = when (status) {
    "approved" -> "✅ Zatwierdzony"
    "rejected" -> "❌ Odrzucony"
    "pending" -> "⏳ Czeka na zatwierdzenie"
    else -> "☁️ Synchronizowanie…"
}

/**
 * FR-76/v2 (2026-08-11, user request): every recipe the CURRENT device has
 * added via "📖 Dodaj swój przepis" (`recipeViewModel.myRecipes`, always
 * shown locally regardless of cloud status -- FR-76's own rule), each with
 * its moderation status pulled from Firestore by
 * `RecipeModerationCoordinator` (`recipeModerationViewModel.myRecipeStatuses`,
 * keyed by recipe id). A recipe not yet present in that map means the
 * publish write (`CommunityCoordinator`) hasn't round-tripped yet, or
 * `communityRecipesEnabled`/the Firestore rules aren't set up -- shown as
 * "☁️ Synchronizowanie…" either way rather than guessing which.
 */
@Composable
private fun MyRecipesCard(recipeViewModel: RecipeViewModel, moderationViewModel: RecipeModerationViewModel) {
    val myRecipes by recipeViewModel.myRecipes.collectAsState()
    val statuses by moderationViewModel.myRecipeStatuses.collectAsState()
    if (myRecipes.isEmpty()) return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("🧑‍🍳 Moje przepisy", style = MaterialTheme.typography.titleMedium)
            myRecipes.forEach { recipe ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(recipe.name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Text(
                        recipeStatusLabel(statuses[recipe.id]),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * FR-76/v2 (2026-08-11, user request): "tylko konto przemas230@gmail.com
 * będzie mogło zatwierdzać przepisy" -- visible ONLY while signed in as
 * [RECIPE_MODERATOR_EMAIL] (client-side convenience gate; the Firestore
 * security rule is the real enforcement, see RecipeModerationCoordinator's
 * doc comment). Lists every `status == "pending"` community submission with
 * ✅/❌ actions -- approve sets `status = "approved"` (same effect as the
 * previous manual Firebase-console edit), reject sets `status = "rejected"`
 * rather than deleting, so the author's own MyRecipesCard can show why.
 */
@Composable
private fun RecipeModerationCard(authViewModel: AuthViewModel, moderationViewModel: RecipeModerationViewModel) {
    val authState by authViewModel.state.collectAsState()
    if ((authState as? AuthState.SignedIn)?.email != RECIPE_MODERATOR_EMAIL) return
    val pending by moderationViewModel.pendingRecipes.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("🛡️ Zatwierdzanie przepisów społeczności", style = MaterialTheme.typography.titleMedium)
            if (pending.isEmpty()) {
                Text(
                    "Brak przepisów czekających na zatwierdzenie.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                pending.forEach { recipe ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(recipe.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "Autor: ${recipe.authorDisplayName ?: "?"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                scope.launch {
                                    approveRecipe(recipe.id).onFailure {
                                        Toast.makeText(context, "Nie udało się zatwierdzić: ${it.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }) { Text("✅ Zatwierdź") }
                            OutlinedButton(onClick = {
                                scope.launch {
                                    rejectRecipe(recipe.id).onFailure {
                                        Toast.makeText(context, "Nie udało się odrzucić: ${it.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }) { Text("❌ Odrzuć") }
                        }
                    }
                }
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
private fun CloudAccountCard(viewModel: AuthViewModel, onClearLocalData: () -> Unit) {
    val authState by viewModel.state.collectAsState()
    val busy by viewModel.busy.collectAsState()
    val error by viewModel.error.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    // FR-79: two SEPARATE questions -- "log out?" then (only if confirmed)
    // "also clear local data?" -- not one combined dialog, per the FR's
    // own description of the flow.
    var showSignOutConfirm by rememberSaveable { mutableStateOf(false) }
    var showClearDataChoice by rememberSaveable { mutableStateOf(false) }

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
                    TextButton(onClick = { showSignOutConfirm = true }) { Text("🚪 Wyloguj się z tego urządzenia") }
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

    if (showSignOutConfirm) {
        AlertDialog(
            onDismissRequest = { showSignOutConfirm = false },
            title = { Text("Wylogować się z tego urządzenia?") },
            text = { Text("Synchronizacja z chmurą zostanie zatrzymana, dopóki nie zalogujesz się ponownie.") },
            confirmButton = {
                TextButton(onClick = {
                    showSignOutConfirm = false
                    showClearDataChoice = true
                }) { Text("Wyloguj") }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutConfirm = false }) { Text("Anuluj") }
            },
        )
    }
    if (showClearDataChoice) {
        // FR-79: "nie czyść" is the documented default -- both the dedicated
        // button AND dismissing the dialog any other way (back/outside tap)
        // take that path, always still signing out either way.
        val proceedKeepingData = {
            showClearDataChoice = false
            viewModel.signOut()
        }
        AlertDialog(
            onDismissRequest = proceedKeepingData,
            title = { Text("Wyczyścić też dane lokalne?") },
            text = {
                Text(
                    "Oprócz wylogowania możesz też wyczyścić dane zapisane na tym urządzeniu " +
                        "(spiżarnię, listę zakupów, planer, ulubione itd.) — przydatne na wspólnym " +
                        "urządzeniu, żeby kolejna osoba nie widziała Twoich danych.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showClearDataChoice = false
                    viewModel.signOut()
                    onClearLocalData()
                }) { Text("Wyczyść") }
            },
            dismissButton = {
                TextButton(onClick = proceedKeepingData) { Text("Nie czyść") }
            },
        )
    }
}

/**
 * Wraps `RequestPermission()` so both [WaterNotificationCard] and
 * [WaterReminderCard] can share one launcher instead of each needing their
 * own -- returns a function that checks the current grant first (skips the
 * system dialog entirely if already granted, including on API <33 where the
 * runtime permission doesn't exist at all) and only then launches the
 * request, invoking [onResult] either way.
 */
@Composable
private fun rememberNotificationPermissionRequester(): ((onResult: (Boolean) -> Unit) -> Unit) {
    val context = LocalContext.current
    var pendingResult by remember { mutableStateOf<((Boolean) -> Unit)?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        pendingResult?.invoke(granted)
        pendingResult = null
    }
    return { onResult ->
        val alreadyGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS,
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (alreadyGranted) {
            onResult(true)
        } else {
            pendingResult = onResult
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

/** FR-38: persistent "+1/-1" water tracker notification toggle -- port of index.html's `enableWaterNotifBtn`/`enableWaterNotification`. */
@Composable
private fun WaterNotificationCard(viewModel: WaterNotificationViewModel, currentWaterCount: Int) {
    val enabled by viewModel.trackerEnabled.collectAsState()
    val requestPermission = rememberNotificationPermissionRequester()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("💧 Powiadomienie z licznikiem wody", style = MaterialTheme.typography.titleMedium)
            Text(
                "Stałe powiadomienie z przyciskami „+1 💧” / „-1 ↩️” pozwala liczyć wypite szklanki " +
                    "bez otwierania aplikacji. Nie znika samo — zostaw je widoczne, dopóki go nie wyłączysz.",
                style = MaterialTheme.typography.bodySmall,
            )
            Button(onClick = {
                if (enabled) {
                    viewModel.setTrackerEnabled(false, currentWaterCount)
                } else {
                    requestPermission { granted ->
                        if (granted) viewModel.setTrackerEnabled(true, currentWaterCount)
                    }
                }
            }) {
                Text(if (enabled) "🔕 Wyłącz powiadomienie" else "🔔 Włącz powiadomienie")
            }
        }
    }
}

/** FR-39: recurring "drink water" reminder -- port of index.html's reminder card (`enableReminderBtn`/`setReminderInterval`/`setReminderFrom`/`setReminderTo`). */
@Composable
private fun WaterReminderCard(viewModel: WaterNotificationViewModel) {
    val reminder by viewModel.reminder.collectAsState()
    val requestPermission = rememberNotificationPermissionRequester()

    var intervalText by rememberSaveable(reminder.intervalMinutes) { mutableStateOf(reminder.intervalMinutes.toString()) }
    var fromText by rememberSaveable(reminder.activeFrom) { mutableStateOf(reminder.activeFrom) }
    var toText by rememberSaveable(reminder.activeTo) { mutableStateOf(reminder.activeTo) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("⏰ Cykliczne przypomnienie o piciu wody", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = intervalText,
                    onValueChange = { intervalText = it.filter(Char::isDigit) },
                    label = { Text("Co ile minut") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = fromText,
                    onValueChange = { fromText = it },
                    label = { Text("Od (GG:MM)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = toText,
                    onValueChange = { toText = it },
                    label = { Text("Do (GG:MM)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (reminder.enabled) {
                        viewModel.setReminderEnabled(false)
                    } else {
                        requestPermission { granted ->
                            if (granted) {
                                viewModel.updateReminderConfig(
                                    intervalText.toIntOrNull() ?: reminder.intervalMinutes,
                                    fromText,
                                    toText,
                                )
                                viewModel.setReminderEnabled(true)
                            }
                        }
                    }
                }) {
                    Text(if (reminder.enabled) "🔕 Wyłącz przypomnienia o piciu wody" else "🔔 Włącz przypomnienia o piciu wody")
                }
                if (reminder.enabled) {
                    TextButton(onClick = {
                        viewModel.updateReminderConfig(intervalText.toIntOrNull() ?: reminder.intervalMinutes, fromText, toText)
                    }) {
                        Text("Zapisz zmiany")
                    }
                }
            }
            if (reminder.enabled && reminder.nextAt != null) {
                val d = remember(reminder.nextAt) { java.util.Date(reminder.nextAt!!) }
                val timeFmt = remember { java.text.SimpleDateFormat("HH:mm", java.util.Locale("pl", "PL")) }
                val today = remember { java.util.Calendar.getInstance() }
                val target = remember(reminder.nextAt) { java.util.Calendar.getInstance().apply { timeInMillis = reminder.nextAt!! } }
                val dayLabel = if (today.get(java.util.Calendar.DAY_OF_YEAR) == target.get(java.util.Calendar.DAY_OF_YEAR)) "dziś" else "jutro"
                Text("Następne przypomnienie: $dayLabel, ${timeFmt.format(d)}.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/** FR-38 diagnostics: last 20 notification-button dispatches this device actually received -- port of index.html's `renderWaterActionLog`. */
@Composable
private fun WaterNotificationLogCard(viewModel: WaterNotificationViewModel) {
    val log by viewModel.actionLog.collectAsState()
    val timeFmt = remember { java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale("pl", "PL")) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("🩺 Diagnostyka powiadomień", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { viewModel.refreshActionLog() }) { Text("Odśwież") }
            }
            if (log.isEmpty()) {
                Text(
                    "Brak zapisanych zdarzeń jeszcze — stuknij +1/-1 w powiadomieniu, potem wróć tu i odśwież.",
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                log.asReversed().forEach { entry ->
                    val time = timeFmt.format(java.util.Date(entry.timestamp))
                    val line = if (entry.result == "swallowed-duplicate") {
                        "$time  action=${entry.action}  [odrzucono jako duplikat]"
                    } else {
                        "$time  action=${entry.action}  ${entry.countBefore} → ${entry.countAfter}"
                    }
                    Text(line, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

/**
 * FR-32 (Ulubione settings tab): a searchable, alphabetized grid of every
 * ingredient name that appears in any recipe (`PantryTiles.buildTileNames`,
 * the same source as the Spiżarnia tile grid), each toggleable ☆/★ -- port
 * of index.html's `renderFavIngChips`/`favIngSearch`. Deliberately a
 * different surface from the inline ☆ on a recipe card's ingredient list
 * (RecipeListScreen.kt) -- same underlying FavoriteIngredientsViewModel, but
 * this one lets you manage favorites for ingredients you haven't opened a
 * recipe card for yet, matching the web version's own separate tab.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FavoriteIngredientsCard(
    viewModel: FavoriteIngredientsViewModel,
    allRecipes: List<com.przemas230.dietaapp.data.Recipe>,
) {
    val favorites by viewModel.favorites.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    val collator = remember { java.text.Collator.getInstance(java.util.Locale("pl", "PL")) }
    val allNames = remember(allRecipes) {
        PantryTiles.buildTileNames(allRecipes).sortedWith(collator)
    }
    val visibleNames = remember(allNames, query) {
        val q = query.trim().lowercase(java.util.Locale("pl", "PL"))
        if (q.isEmpty()) allNames else allNames.filter { it.lowercase(java.util.Locale("pl", "PL")).contains(q) }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("⭐ Ulubione składniki", style = MaterialTheme.typography.titleMedium)
            Text(
                "Wybrane tutaj składniki będą pogrubione na liście przepisów (tak samo jak gwiazdka przy " +
                    "składniku w przepisie — to ten sam wybór).",
                style = MaterialTheme.typography.bodySmall,
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Szukaj składnika…") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                visibleNames.forEach { name ->
                    val active = name in favorites
                    FilterChip(
                        selected = active,
                        onClick = { viewModel.toggle(name) },
                        label = { Text((if (active) "★ " else "☆ ") + name) },
                    )
                }
            }
        }
    }
}
