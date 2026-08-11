package com.przemas230.dietaapp.logic

import kotlin.random.Random

/**
 * FR-32/v2 ("💡 pomysł na danie z ulubionych składników", floating-button
 * variant on Android): picks up to 5 favorite ingredients to seed a Google
 * search for a real dish idea, instead of the old inline button's templated
 * 2-ingredient text suggestion (still on web, unchanged -- see PARITY.md for
 * the deliberate gap).
 *
 * Ingredient categories come from [CanonInfo.cat] (`CANON_INFO`'s 8-way
 * split: Nabiał/Warzywa/Owoce/"Mięso, ryby, jajka"/"Strączki i
 * orzechy"/"Pieczywo i zboża"/Przyprawy/Inne). [FLEXIBLE_CATEGORIES]
 * (Warzywa, Owoce) may contribute more than one ingredient each -- a
 * vegetable salad or a fruit dessert genuinely wants several -- every other
 * category is capped at one, so a batch of 5 favorites all being different
 * kasze/mąki ("Pieczywo i zboża") can't dominate the whole picks the way it
 * would under plain random sampling.
 */
object MealTimeChoice {
    data class Option(val id: String, val label: String, val accusative: String)

    val SNIADANIE = Option("sniadanie", "Śniadanie", "śniadanie")
    val OBIAD = Option("obiad", "Obiad", "obiad")
    val KOLACJA = Option("kolacja", "Kolacja", "kolację")
    val DESER = Option("deser", "Deser", "deser")

    val ALL: List<Option> = listOf(SNIADANIE, OBIAD, KOLACJA, DESER)
}

object FavoriteDishSearch {
    private val FLEXIBLE_CATEGORIES = setOf("Warzywa", "Owoce")

    /**
     * Picks up to [count] favorites, capping every category outside
     * [FLEXIBLE_CATEGORIES] to ONE pick each -- an inviolable cap, not a
     * soft preference, so the result can never be dominated by one
     * repetitive food group (e.g. several different kasze/mąki). [count] is
     * therefore a ceiling, not a target: if the user's favorites don't span
     * enough distinct categories (plus [FLEXIBLE_CATEGORIES], which may
     * each contribute more than once), fewer than [count] ingredients come
     * back rather than breaking the cap to pad the result out -- a weaker
     * but genuinely varied search beats a strong-looking one that's
     * secretly all flour.
     */
    fun pickDiverseIngredients(
        favorites: Set<String>,
        categoryOf: (String) -> String,
        count: Int = 5,
        random: Random = Random.Default,
    ): List<String> {
        if (favorites.isEmpty()) return emptyList()
        val byCategory = favorites.groupBy(categoryOf)
        val picked = LinkedHashSet<String>()

        val strictCategories = byCategory.keys.filter { it !in FLEXIBLE_CATEGORIES }.shuffled(random)
        for (cat in strictCategories) {
            if (picked.size >= count) break
            picked += byCategory.getValue(cat).random(random)
        }

        val flexiblePool = byCategory.filterKeys { it in FLEXIBLE_CATEGORIES }.values.flatten().shuffled(random)
        for (item in flexiblePool) {
            if (picked.size >= count) break
            picked += item
        }

        return picked.toList()
    }

    /** "przepis na {śniadanie/obiad/kolację/deser} z {ingredient, ingredient, ...}" -- fed straight into a Google search query. */
    fun buildSearchQuery(meal: MealTimeChoice.Option, ingredients: List<String>): String =
        "przepis na ${meal.accusative} z ${ingredients.joinToString(", ")}"
}
