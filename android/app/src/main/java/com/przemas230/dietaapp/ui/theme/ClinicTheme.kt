package com.przemas230.dietaapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.przemas230.dietaapp.R

/**
 * FR-87: font/kszalt/uklad zestaw dla motywu "Klinika" (id "clinic"), oddzielny
 * od reszty 11 motywow AppThemes -- ten sam wzorzec co juz istniejace
 * strukturalnie odmienne motywy Polaroid/Kafelki (patrz FR-49/FR-63 w
 * RecipeListScreen.kt), tylko rozszerzony o fonty i typografie, nie tylko
 * ksztalt karty. Oba pliki .ttf w res/font/ sa fontami zmiennymi (variable
 * fonts, jeden plik = caly zakres wag) -- kazdy FontWeight ponizej to ten sam
 * plik z innym FontVariation.Settings, zgodnie z oficjalnym wzorcem Compose
 * dla fontow zmiennych. Wymaga minSdk 26 (platformowe wsparcie
 * Typeface.Builder.setFontVariationSettings) -- projekt ma minSdk 26.
 */
@OptIn(ExperimentalTextApi::class)
private val SpaceGrotesk = FontFamily(
    Font(R.font.space_grotesk_variable, FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.space_grotesk_variable, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.space_grotesk_variable, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
)

@OptIn(ExperimentalTextApi::class)
private val DMSans = FontFamily(
    Font(R.font.dm_sans_variable, FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.dm_sans_variable, FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.dm_sans_variable, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.dm_sans_variable, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
)

val ClinicTypography = Typography(
    displayLarge = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 40.sp),
    displayMedium = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp),
    displaySmall = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 30.sp),
    headlineLarge = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 32.sp),
    headlineMedium = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    headlineSmall = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp),
    titleLarge = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = DMSans, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = DMSans, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = DMSans, fontWeight = FontWeight.Normal, fontSize = 12.5.sp, lineHeight = 17.sp),
    labelLarge = TextStyle(fontFamily = DMSans, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontFamily = DMSans, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontFamily = DMSans, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 15.sp),
)

/**
 * Duze, jednolite zaokraglenia -- wyraznie wieksze/bardziej "kliniczne" niz
 * AppShapes. Skala v2 (2026-08-23) dopasowana do dokladnej skali promieni
 * diet-chef-pro-75 (`--radius: 1.25rem` = 20px, z calc() offsetami -4/+4/+8):
 * sm=16dp, md=18dp, lg=20dp, xl=24dp, 2xl=28dp -- ta ostatnia uzyta dla kart
 * przepisow, ktore w Lovable byly najbardziej zaokraglonym elementem na ekranie.
 */
val ClinicShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
