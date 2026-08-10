package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe

/** Result of [Micronutrients.estimate] -- ca in mg, vitD/b12 in µg, all per portion. */
data class MicronutrientEstimate(val ca: Int, val vitD: Double, val b12: Double)

private data class MicroPer100(val ca: Double, val vitD: Double, val b12: Double)

/**
 * FR-64: calcium/vitamin D/B12 estimate for the FR-12 "Jak policzono" dialog
 * -- pure port of index.html's MICRO_TABLE/computeMicronutrients. Calcium,
 * vitamin D and B12 specifically because a lactose-free diet is the one
 * thing every profile in this app has in common, and those three are
 * easiest to fall short on when dairy is off the table. NOT exhaustive --
 * only ingredients on this recognized list count, same tradeoff as the web
 * version, flagged via [APPROX_NOTE] rather than repeated per recipe.
 */
object Micronutrients {
    private val MICRO_TABLE: Map<String, MicroPer100> = mapOf(
        "jajka" to MicroPer100(25.0, 1.1, 0.45),
        "kurczak (pierś)" to MicroPer100(6.0, 0.1, 0.3),
        "indyk" to MicroPer100(15.0, 0.1, 0.4),
        "wołowina" to MicroPer100(10.0, 0.05, 2.0),
        "wieprzowina" to MicroPer100(8.0, 0.5, 0.6),
        "łosoś" to MicroPer100(12.0, 12.0, 3.5),
        "dorsz" to MicroPer100(12.0, 1.5, 1.1),
        "makrela" to MicroPer100(15.0, 8.0, 8.0),
        "pstrąg" to MicroPer100(60.0, 8.0, 5.0),
        "tuńczyk" to MicroPer100(10.0, 2.0, 2.5),
        "krewetki" to MicroPer100(70.0, 0.0, 1.1),
        "serek bez laktozy" to MicroPer100(110.0, 0.0, 0.5),
        "twaróg bez laktozy" to MicroPer100(130.0, 0.0, 0.6),
        "jogurt bez laktozy" to MicroPer100(150.0, 0.0, 0.4),
        "skyr bez laktozy" to MicroPer100(180.0, 0.0, 0.5),
        "feta / ser bez laktozy" to MicroPer100(100.0, 0.0, 0.3),
        "napój roślinny (migdałowy/owsiany/kokosowy)" to MicroPer100(180.0, 0.75, 0.0),
        "mleko kokosowe" to MicroPer100(15.0, 0.0, 0.0),
        "tofu" to MicroPer100(200.0, 0.0, 0.0),
        "ciecierzyca" to MicroPer100(45.0, 0.0, 0.0),
        "fasola" to MicroPer100(35.0, 0.0, 0.0),
        "soczewica" to MicroPer100(20.0, 0.0, 0.0),
        "hummus" to MicroPer100(20.0, 0.0, 0.0),
        "szpinak" to MicroPer100(27.0, 0.0, 0.0),
        "jarmuż" to MicroPer100(40.0, 0.0, 0.0),
        "brokuł" to MicroPer100(47.0, 0.0, 0.0),
        "migdały" to MicroPer100(38.0, 0.0, 0.0),
        "orzechy" to MicroPer100(15.0, 0.0, 0.0),
        "sezam" to MicroPer100(90.0, 0.0, 0.0),
        "siemię lniane / chia" to MicroPer100(18.0, 0.0, 0.0),
        "kasza gryczana" to MicroPer100(9.0, 0.0, 0.0),
        "chleb żytni" to MicroPer100(15.0, 0.0, 0.0),
    )

    // Fortification varies by brand in Poland -- plant milks are commonly
    // (not universally) enriched with calcium/vitamin D, almost never B12.
    const val APPROX_NOTE = "Wartości orientacyjne, dla części typowych składników (nabiał bez laktozy, ryby, jajka, rośliny strączkowe, zielone warzywa, nasiona) — nie wszystkie składniki przepisu są uwzględnione. Fortyfikacja napojów roślinnych w wapń/wit. D zależy od marki."

    /** Null when the recipe has no calc[] data, or none of its ingredients are on the recognized list -- never shows a row of zeros. */
    fun estimate(recipe: Recipe): MicronutrientEstimate? {
        if (recipe.calc.isEmpty()) return null
        var ca = 0.0
        var vitD = 0.0
        var b12 = 0.0
        var matched = 0
        recipe.calc.forEach { item ->
            val per100 = MICRO_TABLE[IngredientCanon.thumbCanon(item.label)] ?: return@forEach
            matched++
            ca += per100.ca * item.qty
            vitD += per100.vitD * item.qty
            b12 += per100.b12 * item.qty
        }
        if (matched == 0) return null
        return MicronutrientEstimate(Math.round(ca).toInt(), round1(vitD), round1(b12))
    }

    private fun round1(value: Double): Double = Math.round(value * 10) / 10.0
}
