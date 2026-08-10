package com.przemas230.dietaapp.logic

import kotlin.random.Random

/**
 * FR-32 ("💡 Pomysł na danie z ulubionych składników"): port of index.html's
 * IDEA_TEMPLATES/generateIdea — combines two random favorite ingredients into
 * a templated dish-name suggestion, scoped to the currently browsed category.
 * Purely a naming/inspiration generator, not a real recipe (same disclaimer
 * text as the web version).
 */
object DishIdeaGenerator {
    private val TEMPLATES: Map<String, List<String>> = mapOf(
        "sniadania" to listOf(
            "Szybka jajecznica z {a} i {b}",
            "Owsianka z {a} i {b}",
            "Kanapki z {a} i {b} na chlebie żytnim",
            "Omlet z {a} i {b}",
        ),
        "drugie" to listOf(
            "Sałatka z {a} i {b}",
            "Szybka przekąska z {a} i {b}",
            "Pasta z {a} na chrupkim pieczywie, z dodatkiem {b}",
        ),
        "obiady" to listOf(
            "Duszone {a} z {b} i kaszą",
            "Zapiekane {a} z {b}",
            "Sałatka obiadowa z {a}, {b} i oliwą",
            "Jednopatelniowe danie z {a} i {b}",
        ),
        "kolacje" to listOf(
            "Lekka sałatka z {a} i {b}",
            "Krem z {a} z dodatkiem {b}",
            "Roladki z {a} i {b}",
        ),
    )

    data class Idea(val name: String, val method: String, val aClean: String, val bClean: String)

    /** Returns null if fewer than 2 favorite ingredients are tracked (needs at least a+b). */
    fun generate(cat: String, favorites: Set<String>, random: Random = Random.Default): Idea? {
        if (favorites.size < 2) return null
        val shuffled = favorites.shuffled(random)
        val aClean = shuffled[0]
        val bClean = shuffled[1]
        val templates = TEMPLATES[cat] ?: TEMPLATES.getValue("obiady")
        val template = templates[random.nextInt(templates.size)]
        val name = template.replace("{a}", aClean).replace("{b}", bClean)
        val method = "Połącz $aClean z $bClean w duchu niskiego IG — duś, piecz lub podsmaż na oliwie, dopraw " +
            "ziołami i solą. To inspiracja, nie testowany przepis — dopasuj proporcje i czas gotowania samodzielnie."
        return Idea(name, method, aClean, bClean)
    }
}
