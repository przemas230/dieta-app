package com.przemas230.dietaapp.logic

/**
 * FR-2/v6 (ported to Android 2026-08-29): search keys that ignore Polish
 * diacritics, so "zolty" finds "żółty ser" and "brokul" finds "brokuł".
 *
 * The web app fixed this on 2026-08-28 across its three search boxes; an
 * audit of the Kotlin side the same night found the same bug in FOUR places
 * here (recipe search in [RecipeBrowsing], the snack-name suggestions in
 * MainActivity, the ingredient filter in RecipeListScreen and the favourite
 * -ingredient filter in SettingsScreen), all of which had faithfully copied
 * the web behaviour — bug included. This is the shared fix so the fifth
 * search box someone adds gets it for free.
 *
 * Deliberately NOT `java.text.Normalizer` with NFD + mark stripping: "ł"
 * has no decomposed form, so that approach silently leaves the single most
 * common Polish diacritic unfolded — which is exactly the letter in
 * "brokuł", "żółty" and "masło".
 */
object PolishText {
    private val FOLD = mapOf(
        'ą' to 'a', 'ć' to 'c', 'ę' to 'e', 'ł' to 'l', 'ń' to 'n',
        'ó' to 'o', 'ś' to 's', 'ź' to 'z', 'ż' to 'z',
    )

    /** Lowercased and stripped of Polish diacritics — the form both sides of a search comparison should be in. */
    fun searchKey(text: String): String {
        val lowered = text.lowercase(java.util.Locale("pl", "PL"))
        if (lowered.none { it in FOLD }) return lowered
        return buildString(lowered.length) {
            lowered.forEach { append(FOLD[it] ?: it) }
        }
    }

    /** True when [haystack] contains [needle], with diacritics ignored on both sides. */
    fun contains(haystack: String, needle: String): Boolean =
        searchKey(haystack).contains(searchKey(needle))

    /** True when [haystack] starts with [needle], with diacritics ignored on both sides. */
    fun startsWith(haystack: String, needle: String): Boolean =
        searchKey(haystack).startsWith(searchKey(needle))
}
