package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.Sex

/**
 * FR-95/v3 (2026-08-30, requested: "prompt bardziej rozbudowany, dokładna
 * gramatura, dopasowanie do diety/wagi/płci, informacja o inspiracji"):
 * the text sent to Google AI Mode for the "✨ Gemini" button -- personalized
 * with the user's full nutritional profile and this specific meal-slot's
 * kcal/macro target, and explicitly asking for exact gram amounts and the
 * recipe's culinary inspiration/source. The source info is deliberately
 * asked for IN the prompt rather than parsed back into the app: the AI Mode
 * answer page is a plain external search result (no API response to parse),
 * so "the user also gets that info" means it has to already be part of what
 * the page answers, not something rendered separately in-app. Mirrors
 * index.html's buildGeminiPrompt() byte-for-byte in structure.
 */
object GeminiPrompt {
    private fun activityLabel(activity: ActivityLevel): String = when (activity) {
        ActivityLevel.SIEDZACY -> "siedzący tryb życia"
        ActivityLevel.LEKKO_AKTYWNY -> "lekko aktywny (spacery, 1-2x trening/tydz.)"
        ActivityLevel.UMIARKOWANIE_AKTYWNY -> "umiarkowanie aktywny (3-5x trening/tydz.)"
        ActivityLevel.BARDZO_AKTYWNY -> "bardzo aktywny"
    }

    private fun goalLabel(goal: Goal): String = when (goal) {
        Goal.REDUKCJA -> "redukcja masy ciała"
        Goal.UTRZYMANIE -> "utrzymanie wagi"
        Goal.BUDOWANIE -> "budowanie masy"
    }

    fun build(recipe: Recipe, profile: Profile, kcalTarget: Int?, macroTarget: MacroGrams?): String {
        val sexLabel = if (profile.sex == Sex.MEZCZYZNA) "mężczyzna" else "kobieta"
        val restrictions = buildList {
            if (profile.glutenFree) add("bez glutenu")
            if (profile.lactoseFree) add("bez laktozy")
            if (profile.strictLowGI) add("niski indeks glikemiczny")
        }
        val restrictionsText = if (restrictions.isEmpty()) "brak specjalnych ograniczeń" else restrictions.joinToString(", ")
        val macroText = if (macroTarget != null && kcalTarget != null) {
            "ok. $kcalTarget kcal (białko ${macroTarget.protein} g / tłuszcz ${macroTarget.fat} g / węglowodany ${macroTarget.carbs} g)"
        } else {
            "brak wyliczonego celu (uzupełnij profil w Ustawieniach)"
        }
        return """
            |Jesteś doświadczonym dietetykiem klinicznym. Przygotuj szczegółowy, profesjonalny przepis krok po kroku na danie: „${recipe.name}", dopasowany do poniższego profilu żywieniowego.
            |
            |PROFIL OSOBY:
            |- płeć: $sexLabel, wiek: ${profile.age} lat, wzrost: ${profile.heightCm} cm
            |- waga obecna: ${profile.weightKg} kg, waga docelowa: ${profile.targetWeightKg} kg
            |- poziom aktywności: ${activityLabel(profile.activity)}
            |- cel: ${goalLabel(profile.goal)}
            |- ograniczenia dietetyczne: $restrictionsText
            |- orientacyjne zapotrzebowanie na TEN posiłek: $macroText
            |
            |WYMAGANIA:
            |1. Podaj DOKŁADNĄ listę składników z gramaturą w gramach/mililitrach (nie w "szklankach" ani "garściach"), tak dobraną, żeby całe danie mieściło się w podanym wyżej zapotrzebowaniu kalorycznym i makroskładnikowym.
            |2. Rozpisz przygotowanie krok po kroku: dokładne czasy, temperatury i kolejność czynności, ze wskazówkami przydatnymi dla początkujących.
            |3. Jeśli standardowy składnik koliduje z ograniczeniami dietetycznymi powyżej, od razu zaproponuj konkretny zamiennik.
            |4. Na końcu odpowiedzi napisz, z jakiej kuchni, tradycji kulinarnej lub typu źródeł pochodzi ten przepis (np. "klasyczna kuchnia śródziemnomorska", "polska kuchnia domowa"), żebym mógł poszukać więcej podobnych inspiracji.
        """.trimMargin()
    }
}
