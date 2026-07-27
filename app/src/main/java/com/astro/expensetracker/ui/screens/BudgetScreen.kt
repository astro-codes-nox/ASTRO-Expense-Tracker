package com.astro.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.astro.expensetracker.data.Budget
import com.astro.expensetracker.data.DatabaseProvider
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults

@Composable
fun BudgetScreen() {

    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val dao = db.budgetDao()

    var budget by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        val savedBudget = dao.getBudget()

        if (savedBudget != null) {
            budget = savedBudget.amount.toString()
        }

    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {

        Text(
            text = "💰 Monthly Budget",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Current Budget",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "৳ ${
                        if (budget.isEmpty())
                            "0"
                        else
                            "%,.0f".format(budget.toDoubleOrNull() ?: 0.0)
                    }",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = budget,
            onValueChange = {
                budget = it
            },
            label = {
                Text("Enter Monthly Budget")
            },
            leadingIcon = {
                Text("💰")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                scope.launch {

                    dao.saveBudget(
                        Budget(
                            amount = budget.toDoubleOrNull() ?: 0.0
                        )
                    )

                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Text(
                text = "💾 Save Budget",
                style = MaterialTheme.typography.titleMedium
            )

        }

    }
}