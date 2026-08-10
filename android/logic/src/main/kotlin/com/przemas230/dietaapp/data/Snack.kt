package com.przemas230.dietaapp.data

/** FR-33/34: one ad-hoc snack/extra-dish log entry for today -- mirrors index.html's state.eaten[date].snacks[] shape. */
data class Snack(val id: String, val name: String, val kcal: Int)
