package com.astro.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.astro.expensetracker.ui.screens.DashboardScreen
import com.astro.expensetracker.ui.theme.ASTROTheme
import com.astro.expensetracker.ui.navigation.AppNavigation
import com.astro.expensetracker.ui.theme.ThemeManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ASTROTheme(
                darkTheme = ThemeManager.isDarkTheme.value
            ) {
                AppNavigation()
            }
        }
    }
}