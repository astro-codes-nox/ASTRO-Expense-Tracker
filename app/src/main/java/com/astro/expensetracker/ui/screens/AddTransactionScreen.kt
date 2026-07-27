package com.astro.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.astro.expensetracker.data.DatabaseProvider
import com.astro.expensetracker.data.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import android.app.DatePickerDialog
import java.util.Calendar
@Composable
fun AddTransactionScreen(
    id: Int,
    navController: androidx.navigation.NavHostController
) {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf("Expense") }

    var selectedDate by remember {
        mutableStateOf("Select Date")
    }

    var saved by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val dao = db.transactionDao()

    LaunchedEffect(id) {

        if (id != -1) {

            val transaction = dao.getTransactionById(id)

            transaction?.let {
                title = it.title
                amount = it.amount.toString()
                category = it.category
                transactionType = it.type
                selectedDate = it.date
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = if (id == -1) "➕ Add Transaction" else "✏️ Edit Transaction",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Transaction Type",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = {
                    transactionType = "Income"
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (transactionType == "Income")
                            Color(0xFF4CAF50)
                        else
                            Color.LightGray
                )
            ) {
                Text("Income")
            }

            Button(
                onClick = {
                    transactionType = "Expense"
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (transactionType == "Expense")
                            Color(0xFFF44336)
                        else
                            Color.LightGray
                )
            ) {
                Text("Expense")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Selected: $transactionType",
            style = MaterialTheme.typography.bodyLarge
        )

        val calendar = Calendar.getInstance()

        Button(
            onClick = {

                DatePickerDialog(
                    context,
                    { _, year, month, day ->

                        selectedDate = "$day/${month + 1}/$year"

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

                ).show()

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📅 $selectedDate")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                if (
                    title.isBlank() ||
                    amount.isBlank() ||
                    category.isBlank() ||
                    selectedDate == "Select Date"
                ) {
                    errorMessage = "Please fill all fields and select a date"
                    return@Button
                } else {
                    errorMessage = ""
                }
                val transaction = Transaction(
                        id = id,
                title = title,
                amount = amount.toDoubleOrNull() ?: 0.0,
                category = category,
                type = transactionType,
                date = selectedDate
                )

                CoroutineScope(Dispatchers.IO).launch {

                    if (id == -1) {
                        dao.insert(transaction)
                    } else {
                        dao.update(transaction)
                    }
                }

                saved = true
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (id == -1)
                    "Save Transaction"
                else
                    "Update Transaction"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {

            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (saved) {
            Text(
                text = "✅ Transaction Saved Successfully",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2E7D32)
            )
        }

    }
}