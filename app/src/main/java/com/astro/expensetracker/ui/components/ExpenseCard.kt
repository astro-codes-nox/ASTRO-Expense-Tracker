package com.astro.expensetracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExpenseCard(
    expense: Double
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "💸 Expense",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFC62828)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "৳ ${"%,.0f".format(expense)}",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFB71C1C)
            )

        }

    }

}