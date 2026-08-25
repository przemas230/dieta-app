package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe

/**
 * FR-66: pure port of index.html's "➕ Dodaj swój przepis" form validation
 * and save handler (`state.myRecipes.push(recipe)`). The automatic
 * kcal/macro estimation from ingredient text lives separately in
 * `IngredientMacroEstimation`/`IngredientMacroDb` (port of
 * `estimateRecipeMacrosFromText`/`INGREDIENT_MACRO_DB`) and is wired up in
 * `AddCustomRecipeDialog` (app module), not here -- this object only ever
 * validates/builds the final `Recipe` from whatever kcal/protein/carbs/fat
 * values the form settles on, same as index.html's own submit handler,
 * which doesn't care whether those numbers came from auto-calc or manual entry.
 */
object CustomRecipeOperations {
    data class Input(
        val name: String,
        val cat: String,
        val time: String,
        val ingredientsText: String,
        val method: String,
        val kcalText: String,
        val proteinText: String,
        val carbsText: String,
        val fatText: String,
        // FR-66/v5: optional -- ported from index.html's "Źródło
        // inspiracji" field, blank means "not provided" like the other
        // optional text fields here.
        val inspirationSourceText: String = "",
    )

    sealed class ValidationError {
        data object MissingName : ValidationError()
        data object MissingIngredients : ValidationError()
        data object InvalidKcal : ValidationError()
    }

    /** Parses a comma/dot-decimal, possibly-blank numeric field -- blank means "not provided" (null), not zero. */
    private fun parseOptionalDouble(text: String): Double? =
        text.trim().replace(',', '.').takeIf { it.isNotEmpty() }?.toDoubleOrNull()

    /**
     * Same required/defaulted fields as index.html: name and >=1 ingredient
     * are mandatory, category/time/method fall back to sensible defaults
     * when left blank, kcal must be a positive number.
     */
    fun validate(input: Input): ValidationError? {
        if (input.name.trim().isEmpty()) return ValidationError.MissingName
        val ingredients = input.ingredientsText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        if (ingredients.isEmpty()) return ValidationError.MissingIngredients
        val kcal = parseOptionalDouble(input.kcalText)
        if (kcal == null || kcal <= 0) return ValidationError.InvalidKcal
        return null
    }

    /** Returns null if [validate] would report an error -- call validate() first to know which one, for the UI's error message. */
    fun build(input: Input, id: String): Recipe? {
        if (validate(input) != null) return null
        val ingredients = input.ingredientsText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        val kcal = parseOptionalDouble(input.kcalText)!!.let { Math.round(it).toInt() }
        return Recipe(
            id = id,
            cat = input.cat,
            name = input.name.trim(),
            time = input.time.trim().ifEmpty { "15 min" },
            kcal = kcal,
            ingredients = ingredients,
            method = input.method.trim().ifEmpty { "Przygotuj składniki i połącz zgodnie z własnym przepisem." },
            protein = parseOptionalDouble(input.proteinText),
            carbs = parseOptionalDouble(input.carbsText),
            fat = parseOptionalDouble(input.fatText),
            fiber = null,
            gi = null,
            gl = null,
            source = "custom",
            inspirationSource = input.inspirationSourceText.trim().ifEmpty { null },
        )
    }
}
