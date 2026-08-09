package com.przemas230.dietaapp.logic

import kotlin.math.abs

data class PluralForms(val one: String, val few: String, val many: String)

/**
 * FR-29: Polish grammatical inflection for count-based pantry product names
 * ("jajko"/"jajka"/"jajek"), port of index.html's PANTRY_PLURAL_FORMS /
 * polishPluralCategory / pantryDisplayName. Data copied verbatim from the
 * same table in index.html, not retyped by hand, to avoid transcription
 * drift between the two versions.
 */
object PantryDisplay {
    private val PANTRY_PLURAL_FORMS: Map<String, PluralForms> = mapOf(
        "ananas" to PluralForms("ananas", "ananasy", "ananasów"),
        "awokado" to PluralForms("awokado", "awokado", "awokado"),
        "banan" to PluralForms("banan", "banany", "bananów"),
        "brokuł" to PluralForms("brokuł", "brokuły", "brokułów"),
        "burak" to PluralForms("burak", "buraki", "buraków"),
        "bułka pszenna" to PluralForms("bułka pszenna", "bułki pszenne", "bułek pszennych"),
        "cebula" to PluralForms("cebula", "cebule", "cebul"),
        "cukinia" to PluralForms("cukinia", "cukinie", "cukinii"),
        "cytryna" to PluralForms("cytryna", "cytryny", "cytryn"),
        "czerwona cebula" to PluralForms("czerwona cebula", "czerwone cebule", "czerwonych cebul"),
        "gruszka" to PluralForms("gruszka", "gruszki", "gruszek"),
        "imbir" to PluralForms("imbir", "imbiry", "imbirów"),
        "jabłko" to PluralForms("jabłko", "jabłka", "jabłek"),
        "jajka" to PluralForms("jajko", "jajka", "jajek"),
        "jarmuż" to PluralForms("jarmuż", "jarmuże", "jarmuży"),
        "kalafior" to PluralForms("kalafior", "kalafiory", "kalafiorów"),
        "kiełki" to PluralForms("kiełek", "kiełki", "kiełków"),
        "kiwi" to PluralForms("kiwi", "kiwi", "kiwi"),
        "koperek" to PluralForms("koperek", "koperki", "koperków"),
        "marchewka" to PluralForms("marchewka", "marchewki", "marchewek"),
        "morele suszone" to PluralForms("morela suszona", "morele suszone", "moreli suszonych"),
        "ogórek" to PluralForms("ogórek", "ogórki", "ogórków"),
        "ogórek kiszony" to PluralForms("ogórek kiszony", "ogórki kiszone", "ogórków kiszonych"),
        "owsiano-bananowa muffinka" to PluralForms(
            "owsiano-bananowa muffinka",
            "owsiano-bananowe muffinki",
            "owsiano-bananowych muffinek",
        ),
        "papryka" to PluralForms("papryka", "papryki", "papryk"),
        "pietruszka (korzeń)" to PluralForms("pietruszka (korzeń)", "pietruszki (korzeń)", "pietruszek (korzeń)"),
        "pomarańcza" to PluralForms("pomarańcza", "pomarańcze", "pomarańczy"),
        "pomidor" to PluralForms("pomidor", "pomidory", "pomidorów"),
        "pomidorki koktajlowe" to PluralForms("pomidorek koktajlowy", "pomidorki koktajlowe", "pomidorków koktajlowych"),
        "pstrąg" to PluralForms("pstrąg", "pstrągi", "pstrągów"),
        "rzodkiewka" to PluralForms("rzodkiewka", "rzodkiewki", "rzodkiewek"),
        "seler naciowy" to PluralForms("seler naciowy", "selery naciowe", "selerów naciowych"),
        "szczypiorek" to PluralForms("szczypiorek", "szczypiorki", "szczypiorków"),
        "tortilla" to PluralForms("tortilla", "tortille", "tortilli"),
        "wafle chrupkie" to PluralForms("wafel chrupki", "wafle chrupkie", "wafli chrupkich"),
    )

    private val COUNT_UNITS = setOf("szt", "szt.")

    /** Same weight/volume/count-vs-everything-else split the rest of the pantry logic uses for step defaults. */
    fun isCountUnit(unit: String): Boolean = unit.trim().lowercase() in COUNT_UNITS

    /** one/few/many per the standard Polish plural-agreement rule (2-4 except 12-14 -> few, else many). */
    fun polishPluralCategory(n: Int): String {
        val magnitude = abs(n)
        if (magnitude == 1) return "one"
        val lastDigit = magnitude % 10
        val lastTwo = magnitude % 100
        return if (lastDigit in 2..4 && lastTwo !in 12..14) "few" else "many"
    }

    /**
     * Nominative display name for a count-based pantry product, agreeing
     * with `qty` -- port of index.html's pantryDisplayName(). Falls back to
     * `canonName` unchanged for anything not in the table above (e.g. a
     * freely-typed pantry entry that doesn't match a canonical ingredient
     * name, or `qty == null` meaning "no specific count").
     */
    fun displayName(canonName: String, qty: Int?): String {
        val forms = PANTRY_PLURAL_FORMS[canonName] ?: return canonName
        if (qty == null) return forms.one
        return when (polishPluralCategory(qty)) {
            "one" -> forms.one
            "few" -> forms.few
            else -> forms.many
        }
    }
}
