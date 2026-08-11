package com.przemas230.dietaapp.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Faza 1 planu wizualnego dopracowania (2026-08-11, C:\Users\Robert\.claude\plans\curried-tickling-fairy.md):
 * skala odstępów zamiast ręcznie wpisywanych `.dp` rozjeżdżających się
 * między ekranami (np. wnętrze karty 14.dp w PlannerScreen vs 16.dp
 * niemal wszędzie indziej, `Arrangement.spacedBy` wahające się między
 * 8/10/12.dp bez wyraźnego powodu -- ustalone badaniem przed napisaniem
 * tego pliku). Skala 4dp, dobrana pod istniejące, najczęściej powtarzające
 * się wartości w kodzie, żeby wdrożenie nie wymagało wizualnego przeskoku.
 *
 * Świadomie NIE stosowane wstecznie do każdego istniejącego wywołania w
 * jednej turze (zbyt duży, trudny do zweryfikowania na raz zakres) --
 * używane w NOWYCH i przy okazji dotykanych miejscach, żeby stopniowo
 * wypierać rozrzucone literały bez jednorazowego, ryzykownego przepisania
 * sześciu ekranów naraz.
 */
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
}
