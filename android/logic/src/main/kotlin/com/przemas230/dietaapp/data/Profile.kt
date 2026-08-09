package com.przemas230.dietaapp.data

enum class Sex(val label: String) {
    KOBIETA("Kobieta"),
    MEZCZYZNA("Mężczyzna"),
}

enum class ActivityLevel(val factor: Double, val label: String) {
    SIEDZACY(1.2, "Siedzący tryb życia"),
    LEKKO_AKTYWNY(1.375, "Lekko aktywny (spacery, 1-2x trening/tydz.)"),
    UMIARKOWANIE_AKTYWNY(1.55, "Umiarkowanie aktywny (3-5x trening/tydz.)"),
    BARDZO_AKTYWNY(1.725, "Bardzo aktywny"),
}

enum class Goal(val label: String, val headerLabel: String) {
    REDUKCJA("Redukcja masy ciała", "redukcja"),
    UTRZYMANIE("Utrzymanie wagi", "utrzymanie"),
    BUDOWANIE("Budowanie masy", "budowanie masy"),
}

/**
 * Mirrors the web app's DEFAULT_PROFILE (index.html) — same field set and
 * same placeholder numbers, kept here so :logic's ProfileCalculations can
 * be tested without any Android dependency.
 */
data class Profile(
    val sex: Sex = Sex.KOBIETA,
    val age: Int = 37,
    val heightCm: Int = 164,
    val weightKg: Double = 67.0,
    val targetWeightKg: Double = 62.0,
    val activity: ActivityLevel = ActivityLevel.LEKKO_AKTYWNY,
    val goal: Goal = Goal.REDUKCJA,
    val glutenFree: Boolean = false,
    val lactoseFree: Boolean = false,
    // Not yet consumed anywhere — only matters once recipeMatchScore (FR-11)
    // exists to apply its glycemic-load penalty. Stored now so the profile
    // shape matches DEFAULT_PROFILE in index.html and the toggle can be
    // added to the UI without another Profile-shape change later.
    val strictLowGI: Boolean = true,
    val configured: Boolean = false,
)
