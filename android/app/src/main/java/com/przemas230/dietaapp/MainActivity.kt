package com.przemas230.dietaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.przemas230.dietaapp.data.CloudSyncBaselineStore
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.AppThemes
import com.przemas230.dietaapp.logic.DailyCalorieTargets
import com.przemas230.dietaapp.logic.EatenOperations
import com.przemas230.dietaapp.logic.HeaderScrollBehavior
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.PlannerCategory
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.ShoppingDayStrip
import com.przemas230.dietaapp.logic.SnackNutritionDb
import com.przemas230.dietaapp.logic.UiScale
import com.przemas230.dietaapp.logic.WaterOperations
import com.przemas230.dietaapp.logic.forCategory
import com.przemas230.dietaapp.ui.ActivityLogViewModel
import com.przemas230.dietaapp.ui.AuthState
import com.przemas230.dietaapp.ui.AuthViewModel
import com.przemas230.dietaapp.ui.CloudSyncCoordinator
import com.przemas230.dietaapp.ui.CommunityCoordinator
import com.przemas230.dietaapp.ui.EatenViewModel
import com.przemas230.dietaapp.ui.FavoriteDishIdeaDialog
import com.przemas230.dietaapp.ui.FavoriteIngredientsViewModel
import com.przemas230.dietaapp.ui.PantryScreen
import com.przemas230.dietaapp.ui.PantryViewModel
import com.przemas230.dietaapp.ui.PlannerScreen
import com.przemas230.dietaapp.ui.LocalPersistenceCoordinator
import com.przemas230.dietaapp.ui.PlannerViewModel
import com.przemas230.dietaapp.ui.PostepScreen
import com.przemas230.dietaapp.ui.ProfileViewModel
import com.przemas230.dietaapp.ui.RecipeCommentsViewModel
import com.przemas230.dietaapp.ui.RecipeListScreen
import com.przemas230.dietaapp.ui.RecipeModerationCoordinator
import com.przemas230.dietaapp.ui.RecipeModerationViewModel
import com.przemas230.dietaapp.ui.RecipeViewModel
import com.przemas230.dietaapp.ui.SettingsScreen
import com.przemas230.dietaapp.ui.ShoppingScreen
import com.przemas230.dietaapp.ui.ShoppingViewModel
import com.przemas230.dietaapp.ui.SwipeRatingStyle
import com.przemas230.dietaapp.ui.UserListScreen
import com.przemas230.dietaapp.ui.UserProfileScreen
import com.przemas230.dietaapp.ui.SwipeRatingStyleViewModel
import com.przemas230.dietaapp.ui.ThemeViewModel
import com.przemas230.dietaapp.ui.UiScaleViewModel
import com.przemas230.dietaapp.ui.WaterNotificationCoordinator
import com.przemas230.dietaapp.ui.WaterNotificationViewModel
import com.przemas230.dietaapp.ui.WaterViewModel
import com.przemas230.dietaapp.ui.WeightViewModel
import com.przemas230.dietaapp.ui.navigation.BOTTOM_NAV_SCREENS
import com.przemas230.dietaapp.ui.navigation.Screen
import com.przemas230.dietaapp.ui.theme.DietaAppTheme
import com.przemas230.dietaapp.ui.theme.LocalDietaThemeId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import kotlin.math.max
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // FR-14: Compose has no CSS-zoom equivalent, so the whole app's
            // "zoom" is done by scaling LocalDensity around everything below
            // this point — including the top bar/bottom nav (this wraps them
            // too, matching zoom's effect on position:fixed elements in the
            // web version) — rather than scaling individual screens.
            val uiScaleViewModel: UiScaleViewModel = viewModel()
            val customScale by uiScaleViewModel.uiScale.collectAsState()
            val screenWidthDp = LocalConfiguration.current.screenWidthDp
            val effectiveScale = customScale ?: UiScale.detectDefault(screenWidthDp)
            val baseDensity = LocalDensity.current
            // FR-48: created at the Activity root (not SettingsScreen's default
            // viewModel() param) so the whole app -- not just the Ustawienia
            // screen -- repaints under the chosen palette, same reasoning as
            // profileViewModel/pantryViewModel etc. in DietaAppRoot below.
            val themeViewModel: ThemeViewModel = viewModel()
            val themeId by themeViewModel.themeId.collectAsState()
            // FR-48 (second of its two original blockers): index.html sets
            // <meta name="theme-color"> per theme (AppThemeDef.metaColor is
            // this app's port of that same value, computed since the initial
            // FR-48 port but never wired up). enableEdgeToEdge() already
            // leaves the status bar transparent, so the header's own
            // background paints through it for free -- the one thing that
            // still needed doing was the status bar ICONS' light/dark
            // contrast, which by default follows the system's day/night
            // setting rather than the app's own chosen theme (a mismatch
            // whenever those two disagree, e.g. "Ciemny" theme picked while
            // the phone itself is in light mode). Deliberately keyed off
            // metaColor's own brightness, NOT AppThemeDef.isDark -- isDark
            // describes the theme's overall background/text mode, but every
            // theme's metaColor (what's actually behind the status bar) is a
            // saturated header color, e.g. "Jasny"/isDark=false still has a
            // solid blue metaColor that needs light icons, not dark ones.
            SideEffect {
                val meta = AppThemes.byId(themeId).metaColor
                val r = (meta shr 16) and 0xFF
                val g = (meta shr 8) and 0xFF
                val b = meta and 0xFF
                val luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255.0
                WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = luminance > 0.6
            }
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = baseDensity.density * effectiveScale.toFloat(),
                    fontScale = baseDensity.fontScale,
                ),
            ) {
                DietaAppTheme(themeId = themeId) {
                    DietaAppRoot(
                        uiScaleViewModel = uiScaleViewModel,
                        effectiveScale = effectiveScale,
                        themeViewModel = themeViewModel,
                    )
                }
            }
        }
    }
}

/**
 * App-level shell: one top bar (title + Ustawienia action, matching the web
 * app's header gear icon — see FR list in android/PARITY.md), one bottom
 * nav row mirroring index.html's `nav.bottom`, and a NavHost swapping the
 * screen content underneath both. See android/README.md "Co dalej" for the
 * build order and android/PARITY.md for what's still ⏳/⬜ per tab.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DietaAppRoot(uiScaleViewModel: UiScaleViewModel, effectiveScale: Double, themeViewModel: ThemeViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    // FR-71 bugfix (2026-08-11): tapping a bottom-nav tab while a text field
    // on Ustawienia still has focus/keyboard open silently ate that tap --
    // Android's default behavior lets the first outside-tap merely dismiss
    // the keyboard/clear focus rather than ALSO performing the click
    // underneath it, so nothing visibly happened until a second tap (or,
    // confusingly, the system Back button, which clears focus as part of
    // its own handling). Clearing focus explicitly in the same click that
    // triggers navigation (below) makes both happen together, like Back
    // already did implicitly.
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    // Created here (not inside SettingsScreen's default viewModel() param) so
    // it's scoped to the whole NavHost/Activity — the header subtitle below
    // and the profile form on the Ustawienia screen must share one instance,
    // otherwise Compose Navigation would hand each destination its own.
    val profileViewModel: ProfileViewModel = viewModel()
    val profile by profileViewModel.profile.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    // FR-69/FR-73: shared so the header/Ustawienia (sign-in state) and
    // CloudSyncCoordinator below (which only pushes/pulls once signed in)
    // always agree on the same Firebase user.
    val authViewModel: AuthViewModel = viewModel()
    // FR-15: shared here (not PantryScreen's default viewModel() param) so
    // marking a recipe "✅ Zrobione" on the Przepisy tab and viewing the
    // resulting stock change on the Spiżarnia tab see the same instance —
    // same reasoning as profileViewModel above.
    val pantryViewModel: PantryViewModel = viewModel()
    // FR-16: shared for the same reason -- the "🛒" per-ingredient add button
    // in the recipe pantry-check window must show up on the Zakupy tab too.
    val shoppingViewModel: ShoppingViewModel = viewModel()
    // FR-19: shared so "📅 Zaplanuj" on a recipe card (Przepisy tab) and the
    // Planer tab itself see the same week plan.
    val plannerViewModel: PlannerViewModel = viewModel()
    // FR-61: shared so a change on the Ustawienia screen is reflected
    // immediately by the swipe-drag card on the Przepisy tab.
    val swipeRatingStyleViewModel: SwipeRatingStyleViewModel = viewModel()
    // FR-32: shared so an ingredient starred from a recipe card's ingredient
    // list is remembered app-wide (drives "💡 Pomysł na danie" too).
    val favoriteIngredientsViewModel: FavoriteIngredientsViewModel = viewModel()
    // FR-73: hoisted here (not RecipeListScreen's default viewModel() param,
    // which is scoped to the NavBackStackEntry) so cook history and swipe
    // like/dislike ratings survive navigating away from Przepisy and back,
    // and so CloudSyncCoordinator below can read/write them.
    val recipeViewModel: RecipeViewModel = viewModel()
    // FR-77: shared here (not RecipeListScreen's default viewModel() param)
    // so CommunityCoordinator below can invalidate an expanded comment
    // thread the instant this device's own review is saved/deleted.
    val recipeCommentsViewModel: RecipeCommentsViewModel = viewModel()
    // FR-76/v2: "Moje przepisy" status + moderator-only approval in Ustawienia.
    val recipeModerationViewModel: RecipeModerationViewModel = viewModel()
    // FR-70: shared at the Scaffold level (not per-screen) so the header
    // droplet strip below is the single source of truth, visible on every tab.
    val waterViewModel: WaterViewModel = viewModel()
    // FR-38/39: hoisted here (not SettingsScreen's default viewModel() param)
    // so WaterNotificationCoordinator below can wire it to waterViewModel.
    val waterNotificationViewModel: WaterNotificationViewModel = viewModel()
    // FR-36: shared for the same reason as waterViewModel above.
    val eatenViewModel: EatenViewModel = viewModel()
    // FR-40: hoisted here (not PostepScreen's default viewModel() param) so
    // LocalPersistenceCoordinator below can read/restore it.
    val weightViewModel: WeightViewModel = viewModel()
    // FR-42: shared for the same reason as weightViewModel above -- both
    // RecipeListScreen and PantryScreen log to it, PostepScreen displays it.
    val activityLogViewModel: ActivityLogViewModel = viewModel()
    // No UI of its own -- restores every local ViewModel's state on launch
    // and debounce-saves it to a local file on every change, regardless of
    // sign-in state (unlike CloudSyncCoordinator below, this always runs --
    // before this, ALL local state was lost on every app restart).
    LocalPersistenceCoordinator(
        profileViewModel = profileViewModel,
        pantryViewModel = pantryViewModel,
        themeViewModel = themeViewModel,
        uiScaleViewModel = uiScaleViewModel,
        swipeRatingStyleViewModel = swipeRatingStyleViewModel,
        favoriteIngredientsViewModel = favoriteIngredientsViewModel,
        recipeViewModel = recipeViewModel,
        shoppingViewModel = shoppingViewModel,
        plannerViewModel = plannerViewModel,
        eatenViewModel = eatenViewModel,
        waterViewModel = waterViewModel,
        weightViewModel = weightViewModel,
        activityLogViewModel = activityLogViewModel,
    )
    // FR-73: no UI of its own -- pushes/pulls the syncable subset of state
    // above to/from Firestore while authViewModel reports a real (non-
    // anonymous) signed-in user, no-ops otherwise. Declared after every
    // ViewModel it reads so they all already exist.
    CloudSyncCoordinator(
        authViewModel = authViewModel,
        profileViewModel = profileViewModel,
        pantryViewModel = pantryViewModel,
        themeViewModel = themeViewModel,
        uiScaleViewModel = uiScaleViewModel,
        swipeRatingStyleViewModel = swipeRatingStyleViewModel,
        favoriteIngredientsViewModel = favoriteIngredientsViewModel,
        recipeViewModel = recipeViewModel,
        shoppingViewModel = shoppingViewModel,
        plannerViewModel = plannerViewModel,
        eatenViewModel = eatenViewModel,
        waterViewModel = waterViewModel,
        weightViewModel = weightViewModel,
        activityLogViewModel = activityLogViewModel,
    )
    // FR-68/76/77: no UI of its own -- syncs the PUBLIC recipes/publicProfiles
    // Firestore collections (own recipe publishing, community subscription,
    // rating publishing), independent of CloudSyncCoordinator's private
    // users/{uid} doc above.
    CommunityCoordinator(
        authViewModel = authViewModel,
        profileViewModel = profileViewModel,
        recipeViewModel = recipeViewModel,
        commentsViewModel = recipeCommentsViewModel,
    )
    // FR-76/v2: no UI of its own -- feeds "Moje przepisy"'s status badges +
    // the moderator-only approval card in Ustawienia, see its own doc comment.
    RecipeModerationCoordinator(authViewModel = authViewModel, viewModel = recipeModerationViewModel)
    // FR-38/39: no UI of its own -- keeps the persistent tracker notification
    // in sync with waterViewModel in both directions. See its own doc comment.
    WaterNotificationCoordinator(waterViewModel = waterViewModel, notificationViewModel = waterNotificationViewModel)

    // Bug fixed 2026-08-11 ("mimo wyczyszczenia danych dalej wróciły
    // poprzednie ustawienia, czyli kobieta mimo że był wybrany mężczyzna"):
    // the FR-79 "Wyczyść dane lokalne" flow used to reset every ViewModel to
    // defaults SYNCHRONOUSLY, in the very same click handler as
    // authViewModel.signOut() -- but FirebaseAuth's AuthStateListener (the
    // only thing that actually flips CloudSyncCoordinator's `uid` away from
    // the real account) fires asynchronously, not within that same call.
    // If it hadn't fired yet by the time the ViewModels flipped to defaults,
    // CloudSyncCoordinator was still composed with the REAL signed-in uid,
    // so its 1.5s-debounced push effect could -- depending on exactly how
    // fast the listener happened to fire relative to that window -- push the
    // freshly-reset default profile (Profile()'s hardcoded KOBIETA) up to
    // the real account's Firestore document, genuinely OVERWRITING the
    // user's real data with defaults, not just failing to redisplay it.
    // Signing back in then correctly pulled back this now-corrupted cloud
    // copy, which is indistinguishable from "the old defaults never left"
    // from the user's side. Fixed by no longer resetting the ViewModels
    // directly from the click handler at all -- `onClearLocalData` below
    // only clears the on-disk baseline (harmless regardless of timing) and
    // sets `pendingLocalDataClear`; the actual reset is deferred to this
    // LaunchedEffect, which only runs once `authState` has ACTUALLY stopped
    // reporting the real account (Loading/Anonymous/Unavailable), so
    // CloudSyncCoordinator is guaranteed to already be composed with a
    // non-real `uid` (and thus unable to push) by the time any ViewModel
    // changes at all.
    val authState by authViewModel.state.collectAsState()
    var pendingLocalDataClear by remember { mutableStateOf(false) }
    LaunchedEffect(authState, pendingLocalDataClear) {
        if (!pendingLocalDataClear || authState is AuthState.SignedIn) return@LaunchedEffect
        pendingLocalDataClear = false
        profileViewModel.resetToDefault()
        profileViewModel.setDisplayName("")
        pantryViewModel.replaceAll(emptyMap())
        shoppingViewModel.replaceAll(emptyMap())
        plannerViewModel.replaceAll(emptyMap())
        recipeViewModel.replaceCooked(emptyMap())
        recipeViewModel.replaceRatings(emptyMap())
        recipeViewModel.replaceReviews(emptyMap())
        recipeViewModel.replaceMyRecipes(emptyList())
        favoriteIngredientsViewModel.replaceAll(emptySet())
        eatenViewModel.replaceAll(emptyMap())
        waterViewModel.setCount(0)
        waterViewModel.replaceHistory(emptyMap())
        weightViewModel.replaceAll(emptyList())
        activityLogViewModel.clear()
        themeViewModel.setTheme(com.przemas230.dietaapp.logic.AppThemes.DEFAULT_ID)
        uiScaleViewModel.resetToAuto()
        swipeRatingStyleViewModel.setStyle(SwipeRatingStyle.BALLOON)
        recipeViewModel.setCommunityRecipesEnabled(false)
    }

    // FR-89 (2026-08-24, added after a real data-loss incident on this
    // account: user asked for a way to fully reset a signed-in account's
    // data, in chmurze AND locally, from either platform). Unlike
    // pendingLocalDataClear above, this does NOT sign out first -- resetting
    // every ViewModel here is safe to do IMMEDIATELY (no deferral needed)
    // because authState stays SignedIn with the SAME uid throughout, so
    // CloudSyncCoordinator keeps pushing to the SAME account the whole time
    // and naturally propagates every one of these resets to Firestore
    // through its own normal debounced dirty-field mechanism -- exactly like
    // any regular local edit, not the FR-79 race this function avoids.
    //
    // CloudSyncCoordinator only tracks the ~18 fields Android has ViewModels
    // for, though -- myRecipes/customTiles/recipeReviews/pantry*Override/
    // recipeAdded/waterNotifEnabled/waterReminder/household are web-only
    // features Android has no local domain model for at all, so they'd
    // silently survive a reset triggered from THIS platform, leaving the
    // next web session still showing old data. Wiped here directly with a
    // targeted Firestore `update()` (touches only these listed field paths,
    // doesn't disturb whatever CloudSyncCoordinator's own push is doing to
    // the other fields at the same time) using the exact same default
    // shapes as index.html's loadState() fallback object, so a reset means
    // the same thing regardless of which platform triggered it.
    val scope = rememberCoroutineScope()
    val resetAccountData: () -> Unit = {
        profileViewModel.resetToUnconfigured()
        profileViewModel.setDisplayName("")
        pantryViewModel.replaceAll(emptyMap())
        shoppingViewModel.replaceAll(emptyMap())
        plannerViewModel.replaceAll(emptyMap())
        recipeViewModel.replaceCooked(emptyMap())
        recipeViewModel.replaceRatings(emptyMap())
        recipeViewModel.replaceReviews(emptyMap())
        recipeViewModel.replaceMyRecipes(emptyList())
        recipeViewModel.replaceFavoriteRecipes(emptySet())
        favoriteIngredientsViewModel.replaceAll(emptySet())
        eatenViewModel.replaceAll(emptyMap())
        waterViewModel.setCount(0)
        waterViewModel.replaceHistory(emptyMap())
        weightViewModel.replaceAll(emptyList())
        activityLogViewModel.clear()
        themeViewModel.setTheme(com.przemas230.dietaapp.logic.AppThemes.DEFAULT_ID)
        uiScaleViewModel.resetToAuto()
        swipeRatingStyleViewModel.setStyle(SwipeRatingStyle.BALLOON)
        recipeViewModel.setCommunityRecipesEnabled(false)
        val uid = (authState as? AuthState.SignedIn)?.uid
        if (uid != null) {
            scope.launch {
                try {
                    FirebaseFirestore.getInstance().collection("users").document(uid)
                        .update(
                            mapOf(
                                "myRecipes" to emptyList<Any>(),
                                "customTiles" to emptyMap<String, Any>(),
                                "recipeReviews" to emptyMap<String, Any>(),
                                "pantryUnitOverride" to emptyMap<String, Any>(),
                                "pantryCategoryOverride" to emptyMap<String, Any>(),
                                "pantryStepOverride" to emptyMap<String, Any>(),
                                "recipeAdded" to emptyMap<String, Any>(),
                                "waterNotifEnabled" to false,
                                "waterReminder" to mapOf(
                                    "enabled" to false,
                                    "intervalMinutes" to 90,
                                    "activeFrom" to "08:00",
                                    "activeTo" to "22:00",
                                    "nextAt" to null,
                                ),
                                "household" to mapOf(
                                    "id" to null,
                                    "name" to "",
                                    "inviteCode" to null,
                                    "members" to emptyList<Any>(),
                                ),
                            ),
                        ).await()
                } catch (e: Exception) {
                    android.util.Log.w("MainActivity", "Reset danych na koncie (pola web-only) nie powiódł się", e)
                }
            }
        }
    }

    val eatenEntries by eatenViewModel.entries.collectAsState()
    val snacks by eatenViewModel.snacks.collectAsState()
    // FR-87/v7: fed straight into PlannerScreen's Klinika-only dashboard
    // below (WODA card) -- HeaderKcalPanel already collects its own copy
    // for the other 11 themes, this is the same StateFlow, just also
    // needed one level up now that Klinika reads it outside that panel.
    val plannerDashboardWaterCount by waterViewModel.count.collectAsState()
    var showQuickAddDialog by remember { mutableStateOf(false) }
    // FR-87/v7: the Klinika Planer dashboard's icon-only sign-out button
    // gets its own lightweight confirm (not the full Ustawienia flow with
    // the second "clear local data too?" step -- that stays reachable via
    // Ustawienia for anyone who needs it, this is just the quick-tap guard).
    var showDashboardSignOutConfirm by remember { mutableStateOf(false) }
    // FR-32/v2: floating "💡" on Przepisy -- see FavoriteDishIdeaDialog.
    var showFavoriteDishIdeaDialog by remember { mutableStateOf(false) }
    val favIngredientsForIdea by favoriteIngredientsViewModel.favorites.collectAsState()
    // FR-66/v2: floating "📖" on Przepisy -- see RecipeListScreen's
    // showAddRecipeDialog/onAddRecipeDialogDismiss doc comment.
    var showAddRecipeDialog by remember { mutableStateOf(false) }
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val allRecipes by plannerViewModel.allRecipes.collectAsState()
    val recipesById = remember(allRecipes) { allRecipes.associateBy { it.id } }
    val todayIdx = remember { ShoppingDayStrip.todayIndex(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) }
    val todayMeals = weekPlan[todayIdx].orEmpty()
    val kcalTargets = remember(profile) { ProfileCalculations.calcTargets(profile) }
    // FR-44: starts expanded on Przepisy, collapsed elsewhere -- keyed on
    // currentRoute so switching tabs always resets to that tab's default,
    // same as index.html's `if(name!=="recipes") headerEl.classList.add("collapsed")`.
    var headerExpanded by remember(currentRoute) { mutableStateOf(currentRoute == Screen.Recipes.route) }
    // FR-45: a manual toggle (either direction) freezes the FR-44 auto
    // show/hide below until the user manually toggles it again (or leaves
    // and re-enters Przepisy, which resets both via the `remember(currentRoute)`
    // above).
    var headerAutoFrozen by remember(currentRoute) { mutableStateOf(false) }
    // FR-44: hoisted so this same LazyListState can be observed here for
    // scroll position -- see the LaunchedEffect below.
    val recipeListState = rememberLazyListState()
    val density = LocalDensity.current
    LaunchedEffect(recipeListState, currentRoute) {
        if (currentRoute != Screen.Recipes.route) return@LaunchedEffect
        val nearTopPx = with(density) { 60.dp.toPx() }.toInt()
        snapshotFlow { recipeListState.firstVisibleItemIndex to recipeListState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                // FR-44/v2 (2026-08-11): near-top-only, no longer direction-based
                // -- anywhere below the top of the list the header stays
                // collapsed regardless of scroll direction, matching
                // index.html's `if(y<NEAR_TOP){show}else{hide}`.
                if (!headerAutoFrozen) {
                    headerExpanded = HeaderScrollBehavior.isNearTop(index, offset, nearTopPx)
                }
            }
    }

    Scaffold(
        topBar = {
            // FR-36's ring/meal-list/summary panel is tall enough that it
            // does NOT fit inside TopAppBar's own `title` slot -- that slot
            // silently clips content past a fixed max height (confirmed by
            // testing: even a single always-composed debug Text placed after
            // HeaderWaterRow there never appeared). Scaffold's topBar slot
            // itself has no such cap, so the water row and kcal panel live
            // here instead, as siblings after TopAppBar inside one
            // teal-background Column, not nested in its title.
            // FR-87/v2: Klinika's header trades the solid teal/sage-fill
            // block every other theme uses for a light page background with
            // the kcal/water panel as its own elevated card below -- port of
            // diet-chef-pro-75's (Lovable) light header, on explicit
            // feedback that recoloring the EXISTING solid header wasn't
            // enough ("nie widzę zmian w Nagłówek... kolor motywu dalej
            // jest jasny" -- their own "jasny" here means the OLD design
            // still showed through under the new color). See HeaderKcalPanel
            // below for the card itself.
            val isClinicHeader = AppThemes.isClinicFamily(LocalDietaThemeId.current)
            // Requested 2026-08-25 (screenshot follow-up): for Klinika, the
            // action icons sat inside a full-size TopAppBar whose title slot
            // is always empty (`return@TopAppBar` below) -- TopAppBar's own
            // fixed minimum height still reserved its usual space regardless,
            // leaving a visible empty band above the icons that plain padding
            // tweaks on the title can't remove ("puste miejsce... zacznij od
            // samej góry strony"). A lightweight Row with just
            // WindowInsets.statusBars padding replaces TopAppBar entirely for
            // Klinika, landing the icons right below the status bar; the
            // other 11 themes keep the unchanged TopAppBar below.
            val headerActions: @Composable RowScope.() -> Unit = {
                // FR-33: global quick-add, visible on every tab (matches
                // index.html's header "➕" button) -- the Postęp tab's OWN
                // floating-button entry point to this same dialog isn't
                // ported, since that tab is still a placeholder.
                IconButton(onClick = { showQuickAddDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Dodaj przekąskę lub dodatkowe danie")
                }
                IconButton(onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(Screen.Settings.icon, contentDescription = Screen.Settings.label)
                }
            }
            Column(
                modifier = Modifier.background(
                    if (isClinicHeader) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                ),
            ) {
                if (isClinicHeader) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        headerActions()
                    }
                    }
                } else {
                TopAppBar(
                    title = {
                        // Requested 2026-08-25 (Web FR-87/v9, ported here): the
                        // title/subtitle earn no space once nothing above them
                        // needs collapsing/expanding for Klinika (see the chevron
                        // gate below) -- an earlier web attempt at replacing this
                        // with a date+greeting line was itself reverted the same
                        // day for duplicating the Planer dashboard's own greeting
                        // one scroll down, so this ports straight to the FINAL
                        // state: hidden entirely, leaving just the action icons
                        // (settings gear + quick-add) at the end of the bar.
                        if (isClinicHeader) return@TopAppBar
                        Column(
                            modifier = Modifier.clickable {
                                // FR-45/v4 (2026-08-11): EITHER manual toggle now freezes
                                // FR-44's near-top-only auto behavior -- with auto-show
                                // scoped to "only near the very top", an unfrozen manual
                                // expand while scrolled down would just get silently
                                // undone by the next scroll event, making "force it open"
                                // pointless. Auto resumes fresh next time Przepisy is
                                // (re)entered (the `remember(currentRoute)` above).
                                headerExpanded = !headerExpanded
                                headerAutoFrozen = true
                            },
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Dieta App")
                                // FR-87/v7: nothing left to collapse/expand for Klinika --
                                // the ring/meal-list this chevron used to toggle moved to
                                // the Planer dashboard (see the gated HeaderKcalPanel call
                                // below), so the chevron itself is dropped too, matching
                                // web's .header-chevron{display:none} for this theme family.
                                if (!isClinicHeader) {
                                    Text(
                                        if (headerExpanded) "⌃" else "⌄",
                                        modifier = Modifier.padding(start = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            }
                            val namePrefix = if (displayName.isNotBlank()) "$displayName · " else ""
                            val subtitle = if (!profile.configured) {
                                "${namePrefix}👋 Ustaw swój profil w Ustawieniach, aby dopasować dietę do siebie"
                            } else {
                                "$namePrefix${profile.sex.label}, ${profile.age} lat · ${profile.heightCm} cm · " +
                                    "${formatWeight(profile.weightKg)} kg → cel ${formatWeight(profile.targetWeightKg)} kg · " +
                                    "${profile.goal.headerLabel} · ~${kcalTargets.daily} kcal/dzień"
                            }
                            Text(
                                subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (isClinicHeader) Color.Transparent else MaterialTheme.colorScheme.primary,
                        titleContentColor = if (isClinicHeader) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = if (isClinicHeader) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary,
                    ),
                    actions = {
                        // FR-33: global quick-add, visible on every tab (matches
                        // index.html's header "➕" button) -- the Postęp tab's
                        // OWN floating-button entry point to this same dialog
                        // isn't ported, since that tab is still a placeholder.
                        IconButton(onClick = { showQuickAddDialog = true }) {
                            Icon(Icons.Filled.Add, contentDescription = "Dodaj przekąskę lub dodatkowe danie")
                        }
                        IconButton(onClick = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            // Bug fixed 2026-08-11 ("z Ustawień kliknięcie karty
                            // Przepisy nie przełącza, trzeba wstecz"): this used
                            // to navigate here with no popUpTo at all, so Ustawienia
                            // got pushed on top of whatever bottom tab was current
                            // instead of sitting at the same back-stack level as the
                            // tabs themselves. From there, tapping a bottom-nav tab
                            // whose target happened to be the start destination
                            // (Przepisy) -- reached via that same popUpTo(start){
                            // saveState=true}+launchSingleTop+restoreState combo --
                            // could land back on an entry that was never actually
                            // popped off underneath Ustawienia, leaving it visibly
                            // stuck until a manual Back press did the pop instead.
                            // Using the identical popUpTo/singleTop/restoreState
                            // pattern as BOTTOM_NAV_SCREENS below puts Ustawienia at
                            // the same stack depth as every tab, so switching away
                            // from it behaves exactly like switching between any two
                            // tabs (already correct) instead of a special case.
                            navController.navigate(Screen.Settings.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }) {
                            Icon(Screen.Settings.icon, contentDescription = Screen.Settings.label)
                        }
                    },
                )
                }
                // FR-87/v7: this whole panel (ring + today's meal list + snacks)
                // moved into a new dashboard at the top of the Planer tab for
                // Klinika/Klinika (noc) -- see PlannerScreen's clinicDashboard
                // param below, fed the exact same todayMeals/eatenEntries/snacks/
                // waterViewModel/callbacks this used to render directly. The
                // other 11 themes keep this global header panel unchanged.
                if (!isClinicHeader) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        HeaderWaterRow(waterViewModel)
                        if (headerExpanded) {
                            HeaderKcalPanel(
                                targetKcal = kcalTargets.daily,
                                kcalTargets = kcalTargets,
                                todayMeals = todayMeals,
                                recipesById = recipesById,
                                eatenEntries = eatenEntries,
                                snacks = snacks,
                                waterViewModel = waterViewModel,
                                onToggleEaten = { cat, kcal, name -> eatenViewModel.toggle(cat, kcal, name) },
                                onRemoveSnack = { id -> eatenViewModel.removeSnack(id) },
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            val onNavigate: (Screen) -> Unit = { screen ->
                focusManager.clearFocus()
                keyboardController?.hide()
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            // FR-87/v2: Klinika gets the "floating pill" nav bar from
            // diet-chef-pro-75 (Lovable) instead of a docked Material3 bar --
            // on explicit request ("podobały mi się karty na dole... że nie
            // były osadzone na dole tylko jakby nad ekranem"). Every other
            // theme keeps the standard docked NavigationBar unchanged.
            if (AppThemes.isClinicFamily(LocalDietaThemeId.current)) {
                FloatingBottomNav(currentRoute = currentRoute, onNavigate = onNavigate)
            } else {
                NavigationBar {
                    BOTTOM_NAV_SCREENS.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = { onNavigate(screen) },
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            // FR-33/v2: same dialog/state as the header "➕" -- visible only
            // on Postęp (the app's "main" tab per the user's own framing)
            // and gone the instant another tab is selected, since it's keyed
            // straight off currentRoute rather than its own remembered flag.
            if (currentRoute == Screen.Progress.route) {
                FloatingActionButton(onClick = { showQuickAddDialog = true }) {
                    Text("➕")
                }
            }
            // FR-32/v2 + FR-66/v2 (2026-08-11): two floating buttons, Przepisy
            // only -- "💡" (dish-idea search, replaces the old inline "💡
            // Pomysł na danie" button, see FavoriteDishIdeaDialog's doc
            // comment) and "📖" (add your own recipe -- "książka kucharska"
            // cookbook look per the user's request, replaces the old inline
            // "➕ Dodaj swój przepis" button). Stacked in an explicit Column
            // (Scaffold's floatingActionButton slot does NOT auto-arrange
            // multiple direct children -- without this they'd overlap at the
            // same anchor), bottom one first so "💡" ends up above "📖".
            if (currentRoute == Screen.Recipes.route) {
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FloatingActionButton(onClick = { showFavoriteDishIdeaDialog = true }) {
                        Text("💡")
                    }
                    FloatingActionButton(onClick = { showAddRecipeDialog = true }) {
                        Text("📖")
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Planner.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(Screen.Recipes.route) {
                RecipeListScreen(
                    profileViewModel = profileViewModel,
                    pantryViewModel = pantryViewModel,
                    shoppingViewModel = shoppingViewModel,
                    plannerViewModel = plannerViewModel,
                    swipeRatingStyleViewModel = swipeRatingStyleViewModel,
                    favoriteIngredientsViewModel = favoriteIngredientsViewModel,
                    viewModel = recipeViewModel,
                    activityLogViewModel = activityLogViewModel,
                    listState = recipeListState,
                    commentsViewModel = recipeCommentsViewModel,
                    headerExpanded = headerExpanded,
                    showAddRecipeDialog = showAddRecipeDialog,
                    onAddRecipeDialogDismiss = { showAddRecipeDialog = false },
                )
            }
            composable(Screen.Shopping.route) {
                ShoppingScreen(viewModel = shoppingViewModel, plannerViewModel = plannerViewModel, pantryViewModel = pantryViewModel)
            }
            composable(Screen.Planner.route) {
                PlannerScreen(
                    plannerViewModel = plannerViewModel,
                    profileViewModel = profileViewModel,
                    shoppingViewModel = shoppingViewModel,
                    eatenEntries = eatenEntries,
                    snacks = snacks,
                    displayName = displayName,
                    onToggleEaten = { cat, kcal, name -> eatenViewModel.toggle(cat, kcal, name) },
                    waterCount = plannerDashboardWaterCount,
                    onSignOut = { showDashboardSignOutConfirm = true },
                    onWaterTap = { i -> waterViewModel.tapDroplet(i) },
                    onWaterSetCount = { n -> waterViewModel.setCount(n) },
                    onSetEaten = { cat, eaten, kcal, name -> eatenViewModel.setEaten(cat, eaten, kcal, name) },
                )
            }
            composable(Screen.Progress.route) {
                PostepScreen(
                    profileViewModel = profileViewModel,
                    waterViewModel = waterViewModel,
                    weightViewModel = weightViewModel,
                    eatenViewModel = eatenViewModel,
                    activityLogViewModel = activityLogViewModel,
                    plannerViewModel = plannerViewModel,
                )
            }
            composable(Screen.Pantry.route) {
                PantryScreen(viewModel = pantryViewModel, allRecipes = allRecipes, activityLogViewModel = activityLogViewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    profileViewModel = profileViewModel,
                    authViewModel = authViewModel,
                    uiScaleViewModel = uiScaleViewModel,
                    effectiveUiScale = effectiveScale,
                    swipeRatingStyleViewModel = swipeRatingStyleViewModel,
                    themeViewModel = themeViewModel,
                    waterNotificationViewModel = waterNotificationViewModel,
                    currentWaterCount = waterViewModel.count.collectAsState().value,
                    favoriteIngredientsViewModel = favoriteIngredientsViewModel,
                    allRecipes = allRecipes,
                    plannerViewModel = plannerViewModel,
                    shoppingViewModel = shoppingViewModel,
                    // Bug fixed 2026-08-11: was never explicitly passed, so
                    // SettingsScreen's `recipeViewModel: RecipeViewModel =
                    // viewModel()` default silently resolved to an instance
                    // scoped to the Ustawienia NavBackStackEntry -- DIFFERENT
                    // from the shared instance every other screen uses
                    // (Navigation-Compose scopes unpassed `viewModel()`
                    // defaults per-destination, same reason every other
                    // ViewModel in this file is explicitly threaded through).
                    // CommunityRecipesCard's toggle happened to still look
                    // reactive via the cloud/local-disk round-trip, but the
                    // new "Moje przepisy" card needs the REAL shared
                    // `myRecipes` list, which surfaced this.
                    recipeViewModel = recipeViewModel,
                    recipeModerationViewModel = recipeModerationViewModel,
                    onClearLocalData = {
                        // FR-79: "wyczyść dane lokalne". The on-disk cloud-sync
                        // baseline is cleared right away (harmless regardless of
                        // timing, see CloudSyncBaselineStore's doc comment); the
                        // actual ViewModel reset is deferred to the
                        // LaunchedEffect(authState, pendingLocalDataClear) above,
                        // which waits until we're confirmed to no longer be
                        // signed in as the real account before touching any
                        // ViewModel -- see that LaunchedEffect's doc comment for
                        // the data-loss bug this avoids.
                        CloudSyncBaselineStore.clear(context)
                        pendingLocalDataClear = true
                    },
                    onResetAccountData = resetAccountData,
                    onBrowseUsers = { navController.navigate(Screen.UserList.route) },
                )
            }
            // FR-76: reached from Ustawienia's "🌍 Przepisy społeczności" card,
            // not the bottom nav.
            composable(Screen.UserList.route) {
                UserListScreen(
                    onBack = { navController.popBackStack() },
                    onOpenProfile = { uid, _ -> navController.navigate(Screen.UserProfile.routeFor(uid)) },
                )
            }
            composable(
                route = Screen.UserProfile.route,
                arguments = listOf(navArgument("uid") { type = NavType.StringType }),
            ) { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: return@composable
                UserProfileScreen(uid = uid, fallbackDisplayName = null, onBack = { navController.popBackStack() })
            }
        }
    }

    if (showQuickAddDialog) {
        QuickAddSnackDialog(
            onDismiss = { showQuickAddDialog = false },
            onAdd = { name, kcal ->
                eatenViewModel.addSnack(name, kcal)
                showQuickAddDialog = false
            },
        )
    }
    if (showFavoriteDishIdeaDialog) {
        FavoriteDishIdeaDialog(
            favIngredients = favIngredientsForIdea,
            onDismiss = { showFavoriteDishIdeaDialog = false },
        )
    }
    if (showDashboardSignOutConfirm) {
        AlertDialog(
            onDismissRequest = { showDashboardSignOutConfirm = false },
            title = { Text("Wylogować się z tego urządzenia?") },
            text = { Text("Synchronizacja z chmurą zostanie zatrzymana, dopóki nie zalogujesz się ponownie.") },
            confirmButton = {
                TextButton(onClick = {
                    showDashboardSignOutConfirm = false
                    authViewModel.signOut()
                }) { Text("Wyloguj") }
            },
            dismissButton = {
                TextButton(onClick = { showDashboardSignOutConfirm = false }) { Text("Anuluj") }
            },
        )
    }
}

/** "67.0" -> "67", "67.5" -> "67.5" — matches how JS template literals print numbers. */
private fun formatWeight(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

/**
 * FR-70: compact cup-icon strip, always visible in the header on every tab
 * (not gated by a collapse state -- FR-44/45's header auto-hide isn't
 * ported yet). Each cup is its own tap target -- port of index.html's
 * renderHeaderWater, minus the Postępy tab's full water view to mirror it
 * with (that tab is still a placeholder, see android/PARITY.md).
 */
@Composable
private fun HeaderWaterRow(viewModel: WaterViewModel) {
    val count by viewModel.count.collectAsState()
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
    Row(
        modifier = Modifier.padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        for (i in 0 until WaterOperations.MAX_LEVEL) {
            WaterCupIcon(
                filled = i < count,
                size = 14.dp,
                modifier = Modifier.padding(3.dp).clickable { viewModel.tapDroplet(i) },
            )
        }
        Text(
            "$count/${WaterOperations.MAX_LEVEL}",
            style = MaterialTheme.typography.labelSmall,
            color = if (isClinic) MaterialTheme.colorScheme.onBackground else Color.White,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

/**
 * Small hand-drawn mug icon (Canvas, no vector-asset dependency -- same "no
 * extra Gradle deps" spirit as WeightChart/KcalHistoryChart) replacing the
 * old 💧/⚪ emoji, matching index.html's new cupIconSvg (FR-36/v2 redesign):
 * a rounded-bottom mug body with a small handle loop, solid ring-water blue
 * when filled, thin outline in a neutral color when empty.
 */
@Composable
private fun WaterCupIcon(filled: Boolean, size: Dp, modifier: Modifier = Modifier) {
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
    val filledColor = if (isClinic) MaterialTheme.colorScheme.tertiary else Color(0xFF3E8EF5)
    val emptyColor = if (isClinic) {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f)
    } else {
        Color.White.copy(alpha = 0.45f)
    }
    val color = if (filled) filledColor else emptyColor
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = w * 0.12f
        val bodyWidth = w * 0.62f
        val bodyHeight = h * 0.7f
        val bodyLeft = w * 0.06f
        val bodyTop = h * 0.12f
        val corner = CornerRadius(bodyWidth * 0.22f, bodyWidth * 0.22f)
        val bodyPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(Offset(bodyLeft, bodyTop), Size(bodyWidth, bodyHeight)),
                    bottomLeft = corner,
                    bottomRight = corner,
                ),
            )
        }
        if (filled) {
            drawPath(bodyPath, color)
        } else {
            drawPath(bodyPath, color, style = Stroke(width = strokeWidth))
        }
        drawArc(
            color = color,
            startAngle = -80f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(bodyLeft + bodyWidth - w * 0.05f, h * 0.26f),
            size = Size(w * 0.34f, h * 0.4f),
            style = Stroke(width = strokeWidth),
        )
    }
}

/**
 * FR-36/v2: today's dual kcal/water ring (outer orange arc = eaten kcal vs
 * daily target, inner blue arc = today's hydration count/8, in one ring --
 * port of index.html's redesigned renderHeader). Previously (before
 * 2026-08-10) the ring showed PLANNED kcal from the Planer, independent of
 * the eaten toggle -- a faithfully-ported quirk from the source that never
 * actually matched FR-36.md's own description. Fixed together with the
 * wider design-system restyle, on the user's explicit choice to match the
 * reference screenshots (see android/PARITY.md). Also renders the 5
 * Planer-slot rows (swipe right to toggle eaten) and the eaten-kcal summary
 * line. Only shown while headerExpanded, in DietaAppRoot.
 */
/**
 * FR-87/v2: "floating pill" bottom nav for the Klinika theme -- port of
 * diet-chef-pro-75's (Lovable) nav bar, which sits with visible margin above
 * the screen edge instead of docking flush like Material3's NavigationBar,
 * with the active tab's icon in a filled sage circle instead of a text-label
 * color change alone. Lives in Scaffold's own `bottomBar` slot (same as the
 * docked NavigationBar it replaces for this theme) so content padding still
 * accounts for its height automatically -- the "floating" look comes purely
 * from the surrounding Box painting the screen's own background color behind
 * the pill (so the margin reads as page, not as a separate opaque bar) plus
 * the pill's own shadow/rounded shape, not from any special Scaffold slot.
 */
@Composable
private fun FloatingBottomNav(currentRoute: String?, onNavigate: (Screen) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BOTTOM_NAV_SCREENS.forEach { screen ->
                    val selected = currentRoute == screen.route
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onNavigate(screen) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                screen.icon,
                                contentDescription = screen.label,
                                tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            screen.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderKcalPanel(
    targetKcal: Int,
    kcalTargets: DailyCalorieTargets,
    todayMeals: Map<String, PlannedMeal>,
    recipesById: Map<String, Recipe>,
    eatenEntries: Map<String, EatenEntry>,
    snacks: List<Snack>,
    waterViewModel: WaterViewModel,
    onToggleEaten: (cat: String, plannedKcal: Int?, plannedName: String?) -> Unit,
    onRemoveSnack: (id: String) -> Unit,
) {
    val eatenKcal = EatenOperations.dailyEatenKcal(eatenEntries) + EatenOperations.snacksKcal(snacks)
    val remaining = max(0, targetKcal - eatenKcal)
    val kcalPct = if (targetKcal > 0) (eatenKcal.toFloat() / targetKcal).coerceIn(0f, 1f) else 0f
    val waterCount by waterViewModel.count.collectAsState()
    val waterPct = (waterCount.toFloat() / WaterOperations.MAX_LEVEL).coerceIn(0f, 1f)
    // FR-87/v2: Klinika renders this whole panel as an elevated card on its
    // now-light header background (see the topBar Column above) instead of
    // white text over a solid color fill -- textColor/mutedTextColor below
    // are the single switch every Text() in this function reads, so the
    // other 11 themes' white-on-fill rendering stays byte-for-byte the same.
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
    val textColor = if (isClinic) MaterialTheme.colorScheme.onSurface else Color.White
    val mutedTextColor = if (isClinic) MaterialTheme.colorScheme.onSurfaceVariant else Color.White.copy(alpha = 0.75f)
    val panelContent = @Composable {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
                Text("POZOSTAŁO", fontSize = 8.sp, color = mutedTextColor, maxLines = 1)
                Text(remaining.toString(), fontWeight = FontWeight.Bold, fontSize = 17.sp, color = textColor)
                Text("kcal", fontSize = 8.sp, color = mutedTextColor)
            }
            // Bug found 2026-08-23 ("motyw jasny klinika jest w niektórych
            // momentach aż za jasny"): the ring track used to be
            // colorScheme.surfaceVariant, which for Klinika IS the same
            // `line` token the elevated panel's own border/divider color
            // comes from -- on a white card, an unfilled (0%) ring rendered
            // almost perfectly invisible (surfaceVariant and the card's own
            // surface differ by only a few RGB values). onSurfaceVariant
            // (the muted-text token) is already tuned for real contrast
            // against surface/background by design, so a low-alpha version
            // of THAT reads as a proper pale track instead of vanishing.
            val clinicRingTrack = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(78.dp)) {
                CircularProgressIndicator(
                    progress = { kcalPct },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 8.dp,
                    color = if (isClinic) MaterialTheme.colorScheme.primary else Color(0xFFF5822B),
                    trackColor = if (isClinic) clinicRingTrack else Color.White.copy(alpha = 0.20f),
                )
                CircularProgressIndicator(
                    progress = { waterPct },
                    modifier = Modifier.fillMaxSize().padding(11.dp),
                    strokeWidth = 6.dp,
                    color = if (isClinic) MaterialTheme.colorScheme.tertiary else Color(0xFF3E8EF5),
                    trackColor = if (isClinic) clinicRingTrack else Color.White.copy(alpha = 0.20f),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(eatenKcal.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                    Text("z $targetKcal kcal", fontSize = 8.sp, color = mutedTextColor)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                    val meal = todayMeals[category.id]
                    val recipe = meal?.let { recipesById[it.recipeId] }
                    // Real bug fixed 2026-08-11 ("Domowy batonik... pokazuje
                    // inna kalorycznosc w przepisach a inna... na kolku ktore
                    // liczy dzienne spozycie"): this used to pass `recipe?.kcal`
                    // -- the recipe's BASE, unscaled kcal -- straight into the
                    // eaten-toggle, ignoring `meal.scale` (FR-20's portion
                    // scaling) entirely. PlannerScreen's own "Razem" day total
                    // already correctly uses PlannerOperations.scaledKcal(recipe,
                    // meal.scale) for the exact same data, and index.html's
                    // equivalent (plannedRecipeFor -> scaleRecipe(base,
                    // getPlannerScale(...)).kcal) has always captured the SCALED
                    // value -- so a portion scaled to, say, 1.3x correctly logged
                    // 1.3x the kcal as eaten on web, but only the un-scaled 1x
                    // amount on Android, silently undercounting (or overcounting)
                    // the daily ring/"Zjedzone" total for every scaled meal.
                    val eatenKcal = if (recipe != null && meal != null) {
                        PlannerOperations.scaledKcal(recipe, meal.scale)
                    } else {
                        null
                    }
                    KcalMealRow(
                        category = category,
                        targetKcal = kcalTargets.forCategory(category.id) ?: 0,
                        recipe = recipe,
                        recipeKcal = eatenKcal,
                        eaten = EatenOperations.isEaten(eatenEntries, category.id),
                        onToggle = { onToggleEaten(category.id, eatenKcal, recipe?.name) },
                    )
                }
            }
        }
        // FR-33/34: ad-hoc snacks, each removable -- port of index.html's
        // per-snack "×" delete in the "co zjadłaś/eś" list.
        snacks.forEach { snack ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "🍿 ${snack.name} · ${snack.kcal} kcal",
                    color = textColor,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "✕",
                    color = mutedTextColor,
                    fontSize = 10.sp,
                    modifier = Modifier.clickable { onRemoveSnack(snack.id) }.padding(start = 4.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "🍽️ Zjedzone: $eatenKcal kcal · Zostało: $remaining kcal",
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
        )
    }
    if (isClinic) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
            shape = MaterialTheme.shapes.extraLarge,
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        ) {
            Column(modifier = Modifier.padding(14.dp)) { panelContent() }
        }
    } else {
        Column(modifier = Modifier.padding(top = 8.dp)) { panelContent() }
    }
}

/**
 * One Planer-slot row -- swipe right past the commit threshold to toggle
 * eaten (only when a recipe is actually planned there, matching
 * index.html's `.eatable` class gate on attachSwipeToEat). Right-only drag,
 * clamped to [0, maxPx], same shape as index.html's SWIPE_MAX/SWIPE_COMMIT
 * but in dp instead of raw CSS px.
 */
@Composable
private fun KcalMealRow(
    category: PlannerCategory,
    targetKcal: Int,
    recipe: Recipe?,
    // The recipe's OWN kcal for this specific planned portion (already
    // scaled by meal.scale -- see the bugfix note at the call site). Shown
    // next to the dish name so it's visible when it differs from the
    // recipe's base kcal shown on its card in Przepisy -- and from
    // `targetKcal` above, which is this meal SLOT's calorie budget, a
    // different number entirely, not this specific recipe's calorie count.
    recipeKcal: Int?,
    eaten: Boolean,
    onToggle: () -> Unit,
) {
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
    val textColor = if (isClinic) MaterialTheme.colorScheme.onSurface else Color.White
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val maxPx = with(density) { 40.dp.toPx() }
    val commitPx = with(density) { 22.dp.toPx() }
    val dragModifier = if (recipe != null) {
        Modifier.pointerInput(category.id) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    val committed = offsetX.value > commitPx
                    scope.launch {
                        if (committed) onToggle()
                        offsetX.animateTo(0f)
                    }
                },
                onDragCancel = { scope.launch { offsetX.animateTo(0f) } },
            ) { change, dragAmount ->
                change.consume()
                scope.launch { offsetX.snapTo((offsetX.value + dragAmount).coerceIn(0f, maxPx)) }
            }
        }
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .then(dragModifier)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (eaten) {
            Text("✓", color = Color(0xFF5AD28C), fontSize = 10.sp, modifier = Modifier.padding(end = 2.dp))
        }
        Column {
            Text(
                "${category.emoji} ${category.label} · $targetKcal kcal",
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
            )
            if (recipe != null) {
                Text(
                    if (recipeKcal != null) "${recipe.name} ($recipeKcal kcal)" else recipe.name,
                    color = textColor.copy(alpha = if (eaten) 0.55f else 0.72f),
                    fontSize = 9.sp,
                    textDecoration = if (eaten) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * FR-33/34: the "➕" quick-add dialog -- free-text name (auto-filled kcal via
 * SnackNutritionDb.estimate, editable, with a startsWith-then-contains
 * suggestion list from 2 typed characters, matching index.html's
 * buildSnackAddForm/renderSuggestions) plus a manual kcal field.
 */
@Composable
private fun QuickAddSnackDialog(onDismiss: () -> Unit, onAdd: (name: String, kcal: Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var kcalText by remember { mutableStateOf("") }
    var kcalWasAutoFilled by remember { mutableStateOf(false) }

    val estimate = remember(name) { SnackNutritionDb.estimate(name) }
    LaunchedEffect(estimate) {
        if (estimate != null) {
            kcalText = estimate.kcal.toString()
            kcalWasAutoFilled = true
        } else if (kcalWasAutoFilled) {
            kcalText = ""
            kcalWasAutoFilled = false
        }
    }
    val suggestions = remember(name) {
        val q = name.trim().lowercase()
        if (q.length < 2) {
            emptyList()
        } else {
            val names = SnackNutritionDb.TABLE.keys
            val starts = names.filter { it.startsWith(q) }
            val includes = names.filter { !it.startsWith(q) && it.contains(q) }
            (starts + includes).take(8)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "➕ Dodaj przekąskę lub dodatkowe danie",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Co zjadłaś/eś?") },
                    placeholder = { Text("np. 1 banan, 150g ryżu, jogurt 200g") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (suggestions.isNotEmpty()) {
                    Column {
                        suggestions.forEach { suggestion ->
                            // FR-35: emoji suffix in the suggestion, matching index.html's renderSuggestions.
                            Text(
                                IngredientCanon.withEmoji(suggestion, suggestion),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { name = suggestion }
                                    .padding(vertical = 6.dp),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                } else if (name.trim().length >= 2) {
                    Text(
                        if (estimate != null) {
                            "≈ ${estimate.kcal} kcal (${estimate.basis}) — możesz poprawić ręcznie"
                        } else {
                            "Nie rozpoznano produktu — podaj kcal ręcznie"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = kcalText,
                        onValueChange = { kcalText = it; kcalWasAutoFilled = false },
                        label = { Text("kcal") },
                        singleLine = true,
                        modifier = Modifier.width(110.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val kcal = kcalText.toIntOrNull()
                            if (name.isNotBlank() && kcal != null && kcal > 0) {
                                // FR-35: emoji suffix baked into the saved snack name, matching index.html's withEmoji(rawName, est.canon).
                                val savedName = estimate?.let { IngredientCanon.withEmoji(name.trim(), it.canonName) } ?: name.trim()
                                onAdd(savedName, kcal)
                            }
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("+ Dodaj")
                    }
                }
            }
        }
    }
}
