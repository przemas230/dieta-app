package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeComment

/**
 * FR-76/FR-77: pure port of index.html's `sanitizeCommunityRecipeDoc()`/
 * `sanitizeRatingDoc()` — validates/coerces a raw Firestore document (another
 * user's device, so never trusted) into a safe local value instead of
 * crashing or rendering `NaN`/an unknown category. Deliberately has NO
 * `escapeHtml()` equivalent: Compose's `Text()` never interprets its input
 * as markup the way `innerHTML` does in the web app, so there is no XSS
 * vector to close here — see PARITY.md's FR-76 note.
 */
object CommunityRecipeOperations {
    private val VALID_CATEGORIES = setOf("sniadania", "drugie", "obiady", "kolacje", "deser")

    private fun numOrNull(v: Any?): Double? = when (v) {
        is Number -> v.toDouble().takeIf { it.isFinite() }
        is String -> v.toDoubleOrNull()?.takeIf { it.isFinite() }
        else -> null
    }

    fun sanitizeCommunityRecipeDoc(data: Map<String, Any?>, id: String): Recipe {
        val cat = (data["cat"] as? String)?.takeIf { it in VALID_CATEGORIES } ?: "obiady"
        val name = ((data["name"] as? String)?.takeIf { it.isNotBlank() } ?: "Przepis bez nazwy").take(200)
        val time = ((data["time"] as? String)?.takeIf { it.isNotBlank() } ?: "—").take(40)
        val kcal = Math.round(numOrNull(data["kcal"]) ?: 0.0).toInt()
        val ingredients = (data["ingredients"] as? List<*>)
            ?.filterIsInstance<String>()
            ?.take(60)
            ?.map { it.take(200) }
            ?: emptyList()
        val method = ((data["method"] as? String)?.takeIf { it.isNotBlank() } ?: "—").take(4000)
        val authorUid = data["authorUid"] as? String
        val authorDisplayName = ((data["authorDisplayName"] as? String)?.takeIf { it.isNotBlank() } ?: "Anonimowy użytkownik").take(60)

        // Same "all three or none" rule as web: a doc with only a partial
        // macro breakdown reports no macros at all rather than silently
        // treating missing fields as zero.
        val protein = numOrNull(data["protein"])
        val carbs = numOrNull(data["carbs"])
        val fat = numOrNull(data["fat"])
        val hasFullMacros = protein != null && carbs != null && fat != null

        return Recipe(
            id = id,
            cat = cat,
            name = name,
            time = time,
            kcal = kcal,
            ingredients = ingredients,
            method = method,
            protein = if (hasFullMacros) protein else null,
            carbs = if (hasFullMacros) carbs else null,
            fat = if (hasFullMacros) fat else null,
            fiber = if (hasFullMacros) (numOrNull(data["fiber"]) ?: 0.0) else null,
            gi = if (hasFullMacros) (numOrNull(data["gi"]) ?: 50.0) else null,
            gl = if (hasFullMacros) (numOrNull(data["gl"]) ?: 0.0) else null,
            source = "community",
            authorUid = authorUid,
            authorDisplayName = authorDisplayName,
        )
    }

    /** [createdAtMillis] is extracted from the Firestore Timestamp by the caller so this stays a pure, Firestore-free function. */
    fun sanitizeRatingDoc(data: Map<String, Any?>, uid: String, createdAtMillis: Long?): RecipeComment {
        val stars = (Math.round(numOrNull(data["stars"]) ?: 0.0).toInt()).coerceIn(1, 5)
        val comment = (data["comment"] as? String)?.take(600)?.takeIf { it.isNotBlank() }
        val displayName = ((data["displayName"] as? String)?.takeIf { it.isNotBlank() } ?: "Anonimowy użytkownik").take(60)
        return RecipeComment(uid, displayName, stars, comment, createdAtMillis)
    }

    /** Port of the `allRecipes()` dedupe (index.html) — an author's own copy always wins over the community mirror of the same id. */
    fun dedupeCommunityRecipes(myRecipes: List<Recipe>, community: List<Recipe>): List<Recipe> {
        val myIds = myRecipes.map { it.id }.toSet()
        return community.filterNot { it.id in myIds }
    }
}
