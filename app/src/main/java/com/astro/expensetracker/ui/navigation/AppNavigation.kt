package com.astro.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.astro.expensetracker.ui.screens.AddTransactionScreen
import com.astro.expensetracker.ui.screens.DashboardScreen
import com.astro.expensetracker.ui.screens.StatisticsScreen
import com.astro.expensetracker.ui.screens.BudgetScreen
import com.astro.expensetracker.ui.screens.SettingsScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        composable("dashboard") {
            DashboardScreen(navController)
        }

        composable("add_transaction") {
            AddTransactionScreen(
                id = -1,
                navController = navController
            )
        }

        composable("add_transaction/{id}") {

                backStackEntry ->

            val id = backStackEntry.arguments
                ?.getString("id")
                ?.toInt() ?: -1

            AddTransactionScreen(
                id = id,
                navController = navController
            )
        }

        composable("statistics") {
            StatisticsScreen()
        }

        composable("budget") {
            BudgetScreen()
        }

        composable("settings") {
            SettingsScreen()
        }
    }
}