package com.przemas230.dietaapp.logic

enum class DayCardState { EMPTY, TODO, STARTED, DONE }

/**
 * One card of the FR-58/FR-62 shopping-day-strip -- `dayName` is the full
 * Polish day name (for the long-press detail), `label` is what's actually
 * shown on the card ("Dziś"/"Jutro"/"Pojutrze" for the next two days, a
 * 3-letter abbreviation otherwise).
 */
data class ShoppingDayCard(
    val day: Int,
    val dayName: String,
    val label: String,
    val state: DayCardState,
    val planned: Int,
    val onList: Int,
    val progressPct: Int,
)

/**
 * FR-58/FR-62: pure port of index.html's renderShopDayStrip -- replaces two
 * previously separate widgets (a row of per-day add buttons, and a
 * read-only "mini calendar" of fill rings) with one strip where each card
 * both states its own status in plain text and is itself the add button.
 */
object ShoppingDayStrip {
    /** jsDayOfWeek: 0=Sunday..6=Saturday (JS's Date#getDay()) -> WeekPlan's own 0=Poniedziałek..6=Niedziela index for "today". */
    fun todayIndex(jsDayOfWeek: Int): Int = if (jsDayOfWeek == 0) 6 else jsDayOfWeek - 1

    fun buildCards(weekPlan: WeekPlan, isRecipeAdded: (String) -> Boolean, todayIdx: Int): List<ShoppingDayCard> =
        (0..6).map { day ->
            val dayMeals = weekPlan[day].orEmpty()
            val planned = dayMeals.size
            val onList = dayMeals.values.count { isRecipeAdded(it.recipeId) }
            val dayName = PlannerOperations.DAYS_PL[day]
            val label = when (day) {
                todayIdx -> "Dziś"
                (todayIdx + 1) % 7 -> "Jutro"
                (todayIdx + 2) % 7 -> "Pojutrze"
                else -> dayName.take(3)
            }
            val state = when {
                planned == 0 -> DayCardState.EMPTY
                onList == planned -> DayCardState.DONE
                onList > 0 -> DayCardState.STARTED
                else -> DayCardState.TODO
            }
            val pct = when (state) {
                DayCardState.DONE -> 100
                DayCardState.STARTED -> Math.round(onList.toDouble() / planned * 100).toInt()
                else -> 0
            }
            ShoppingDayCard(day, dayName, label, state, planned, onList, pct)
        }

    /** "Dziś"/"Jutro"/"Pojutrze" pass through as-is for the toast/click label; any other day uses its full Polish name -- matches index.html's addDayToShoppingList(di, ...) call. */
    fun clickDayLabel(card: ShoppingDayCard): String =
        if (card.label == "Dziś" || card.label == "Jutro" || card.label == "Pojutrze") card.label else card.dayName

    /** index.html's addDayToShoppingList toast text, built from the same add/already counts. */
    fun addResultMessage(dayLabel: String, added: Int, already: Int): String {
        val lower = dayLabel.lowercase()
        return if (added == 0) {
            if (already > 0) "Dania na $lower są już na liście" else "Brak zaplanowanych dań na $lower w Planerze"
        } else {
            "Dodano składniki z $added dań na $lower" + if (already > 0) " ($already już było na liście)" else ""
        }
    }
}
