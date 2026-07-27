package com.astro.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.astro.expensetracker.data.DatabaseProvider
import com.astro.expensetracker.data.Transaction
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.core.entry.entryModelOf
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults

@Composable
fun StatisticsScreen() {

    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val dao = db.transactionDao()

    var transactions by remember {
        mutableStateOf<List<Transaction>>(emptyList())
    }

    LaunchedEffect(Unit) {
        transactions = dao.getAllTransactions()
    }

    val currentMonth = remember {
        SimpleDateFormat(
            "MM/yyyy",
            Locale.getDefault()
        ).format(Date())
    }

    val totalIncome = transactions
        .filter { it.type == "Income" }
        .sumOf { it.amount }

    val totalExpense = transactions
        .filter { it.type == "Expense" }
        .sumOf { it.amount }

    val balance = totalIncome - totalExpense

    val thisMonthTransactions = transactions.filter {
        it.date.endsWith(currentMonth)
    }

    val thisMonthIncome = thisMonthTransactions
        .filter { it.type == "Income" }
        .sumOf { it.amount }

    val thisMonthExpense = thisMonthTransactions
        .filter { it.type == "Expense" }
        .sumOf { it.amount }

    val thisMonthBalance = thisMonthIncome - thisMonthExpense

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 36.dp,
                bottom = 20.dp
            )
    ) {

        Text(
            text = "📊  Statistics",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "📈  Overall Summary",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "💰  Income : ৳${"%,.0f".format(totalIncome)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "💸  Expense : ৳${"%,.0f".format(totalExpense)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "💵  Balance : ৳${"%,.0f".format(balance)}",
                    style = MaterialTheme.typography.titleMedium
                )

            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Monthly Summary",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "📅  This Month",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "💰  Income : ৳${"%,.0f".format(thisMonthIncome)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "💸  Expense : ৳${"%,.0f".format(thisMonthExpense)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "💵  Balance : ৳${"%,.0f".format(thisMonthBalance)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val foodExpense = transactions
            .filter {
                it.type == "Expense" &&
                        it.category.equals("Food", ignoreCase = true)
            }
            .sumOf { it.amount }

        val travelExpense = transactions
            .filter {
                it.type == "Expense" &&
                        it.category.equals("Travel", ignoreCase = true)
            }
            .sumOf { it.amount }

        val shoppingExpense = transactions
            .filter {
                it.type == "Expense" &&
                        it.category.equals("Shopping", ignoreCase = true)
            }
            .sumOf { it.amount }

        val othersExpense = transactions
            .filter {
                it.type == "Expense" &&
                        it.category.lowercase() !in listOf(
                    "food",
                    "travel",
                    "shopping"
                )
            }
            .sumOf { it.amount }

        val chartModel = entryModelOf(
            foodExpense,
            travelExpense,
            shoppingExpense,
            othersExpense
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "📊  Expense Chart",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Chart(
                    chart = columnChart(),
                    model = chartModel,
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "📂  Category Summary",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "🍔  Food : ৳${"%,.0f".format(foodExpense)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🚗  Travel : ৳${"%,.0f".format(travelExpense)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🛍  Shopping : ৳${"%,.0f".format(shoppingExpense)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "📱  Others : ৳${"%,.0f".format(othersExpense)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }


    }
}