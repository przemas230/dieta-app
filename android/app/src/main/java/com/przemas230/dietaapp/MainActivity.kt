package com.przemas230.dietaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.przemas230.dietaapp.ui.RecipeListScreen
import com.przemas230.dietaapp.ui.theme.DietaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DietaAppTheme {
                RecipeListScreen()
            }
        }
    }
}
