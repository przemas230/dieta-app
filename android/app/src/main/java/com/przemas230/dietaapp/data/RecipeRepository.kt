package com.przemas230.dietaapp.data

import android.content.Context
import org.json.JSONArray

/**
 * Loads app/src/main/assets/recipes.json — a straight export of the web
 * app's 229 built-in recipes (see index.html's RECIPES array). Uses
 * org.json (built into Android) rather than a serialization library, to
 * keep this first milestone free of extra Gradle dependencies to get wrong
 * without a way to compile-check them here.
 */
object RecipeRepository {
    fun loadRecipes(context: Context): List<Recipe> {
        val json = context.assets.open("recipes.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
        val array = JSONArray(json)
        val result = ArrayList<Recipe>(array.length())
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val ingredientsArray = obj.getJSONArray("ingredients")
            val ingredients = ArrayList<String>(ingredientsArray.length())
            for (j in 0 until ingredientsArray.length()) {
                ingredients.add(ingredientsArray.getString(j))
            }
            result.add(
                Recipe(
                    id = obj.getString("id"),
                    cat = obj.getString("cat"),
                    name = obj.getString("name"),
                    time = obj.optString("time", "—"),
                    kcal = obj.optInt("kcal", 0),
                    ingredients = ingredients,
                    method = obj.optString("method", ""),
                    protein = if (obj.has("protein")) obj.getDouble("protein") else null,
                    carbs = if (obj.has("carbs")) obj.getDouble("carbs") else null,
                    fat = if (obj.has("fat")) obj.getDouble("fat") else null,
                    fiber = if (obj.has("fiber")) obj.getDouble("fiber") else null,
                    gi = if (obj.has("gi")) obj.getDouble("gi") else null,
                    gl = if (obj.has("gl")) obj.getDouble("gl") else null,
                )
            )
        }
        return result
    }
}
