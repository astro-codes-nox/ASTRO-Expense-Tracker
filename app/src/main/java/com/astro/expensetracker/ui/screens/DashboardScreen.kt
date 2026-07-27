package com.astro.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.astro.expensetracker.data.DatabaseProvider
import com.astro.expensetracker.data.Transaction
import com.astro.expensetracker.ui.components.ExpenseCard
import com.astro.expensetracker.ui.components.IncomeCard
import com.astro.expensetracker.ui.components.TransactionItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.astro.expensetracker.utils.PdfGenerator
import android.widget.Toast
import androidx.compose.foundation.background
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.draw.clip
import java.text.DecimalFormat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment

@Composable
fun DashboardScreen(
    navController: NavHostController
) {

    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val dao = db.transactionDao()
    val budgetDao = db.budgetDao()

    var transactions by remember {
        mutableStateOf<List<Transaction>>(emptyList())
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var transactionToDelete by remember {
        mutableStateOf<Transaction?>(null)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var budgetAmount by remember {
        mutableStateOf(0.0)
    }

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    LaunchedEffect(Unit) {

        transactions = dao.getAllTransactions()

        budgetAmount =
            budgetDao.getBudget()?.amount ?: 0.0
    }

    val totalIncome = transactions
        .filter { it.type == "Income" }
        .sumOf { it.amount }

    val totalExpense = transactions
        .filter { it.type == "Expense" }
        .sumOf { it.amount }

    val totalBalance = totalIncome - totalExpense

    val moneyFormat = DecimalFormat("#,###")

    val remainingBudget = budgetAmount - totalExpense

    val budgetProgress =
        if (budgetAmount > 0)
            (totalExpense / budgetAmount).coerceAtMost(1.0)
        else
            0.0

    val budgetLeftPercent =
        ((1 - budgetProgress) * 100)
            .toInt()
            .coerceAtLeast(0)

    val budgetWarning = if (
        budgetAmount > 0 &&
        remainingBudget < 0
    ) {
        "🔴 Budget Exceeded!"
    } else if (budgetAmount > 0) {
        "🟢 Remaining Budget: ৳$remainingBudget"
    } else {
        ""
    }

    val filteredTransactions = transactions.filter { transaction ->

        val matchesSearch =
            transaction.title.contains(searchText, ignoreCase = true)

        val matchesFilter =
            when (selectedFilter) {
                "Income" -> transaction.type == "Income"
                "Expense" -> transaction.type == "Expense"
                else -> true
            }

        matchesSearch && matchesFilter
    }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_transaction")
                }
            ) {
                Text("+")
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(20.dp)
        ) {

            Column {

                Text(
                    text = "👋 Welcome Back",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "ASTRO Expense Tracker",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Manage your money smarter.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF4F46E5),
                                    Color(0xFF7C3AED)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {

                    val healthText =
                        when {
                            budgetProgress < 0.6 -> "🟢 Excellent"
                            budgetProgress < 0.9 -> "🟡 Warning"
                            else -> "🔴 Critical"
                        }

                    Text(
                        text = healthText,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "💰 Total Balance",
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "৳ ${"%,.0f".format(totalBalance)}",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "🎯 Monthly Budget : ৳$budgetAmount",
                        color = Color(0xFFE0E7FF)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "💵 Remaining Budget : ৳$remainingBudget",
                        color =
                            if (remainingBudget >= 0)
                                Color(0xFF86EFAC)
                            else
                                Color(0xFFFCA5A5)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { budgetProgress.toFloat() },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(50)),

                        trackColor = Color.White.copy(alpha = 0.25f),

                        color =
                            when {
                                budgetProgress < 0.6 -> Color(0xFF4ADE80)   // Green
                                budgetProgress < 0.9 -> Color(0xFFFACC15)   // Yellow
                                else -> Color(0xFFF87171)                   // Red
                            }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "📈 Used ${(budgetProgress * 100).toInt()}%",
                            color = Color(0xFF86EFAC),   // Soft Green
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "💼 Left $budgetLeftPercent%",
                            color = Color(0xFFE0E7FF),   // Soft White/Blue
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }

                    if (budgetWarning.isNotEmpty()) {
                        Text(
                            text =
                                if (remainingBudget < 0)
                                    "⚠️ Budget Exceeded!"
                                else
                                    "✅ Budget On Track",
                            color =
                                if (remainingBudget < 0)
                                    Color(0xFFFCA5A5)
                                else
                                    Color(0xFF86EFAC),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(modifier = Modifier.weight(1f)) {
                    IncomeCard(
                        income = totalIncome
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    ExpenseCard(
                        expense = totalExpense
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Card(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        navController.navigate("statistics")
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text("📊", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Statistics")
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        navController.navigate("budget")
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text("💰", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Budget")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Card(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        navController.navigate("settings")
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text("⚙️", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Settings")
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {

                        try {

                            val file = PdfGenerator.createPdf(
                                context = context,
                                income = totalIncome,
                                expense = totalExpense,
                                balance = totalBalance,
                                transactions = transactions
                            )

                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }

                            context.startActivity(
                                Intent.createChooser(intent, "Share PDF")
                            )

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text("📄", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Export")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                label = {
                    Text("🔍 Search Transaction")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        selectedFilter = "All"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("All")
                }

                Button(
                    onClick = {
                        selectedFilter = "Income"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Income")
                }

                Button(
                    onClick = {
                        selectedFilter = "Expense"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Expense")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredTransactions.isEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "📭",
                            style = MaterialTheme.typography.displayMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "No Transactions Yet",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Start tracking your income and expenses.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            } else {

            filteredTransactions.forEach { transaction ->

                TransactionItem(
                    title = transaction.title,

                    amount =
                        if (transaction.type == "Income")
                            "+৳${transaction.amount}"
                        else
                            "-৳${transaction.amount}",

                    date = transaction.date,

                    onDelete = {

                        transactionToDelete = transaction
                        showDeleteDialog = true

                    },

                    onClick = {
                        navController.navigate("add_transaction/${transaction.id}")
                    }
                )
            }
        }

            Spacer(modifier = Modifier.height(100.dp))

        }
    }

    if (showDeleteDialog) {

        AlertDialog(

            onDismissRequest = {
                showDeleteDialog = false
            },

            title = {
                Text("Delete Transaction")
            },

            text = {
                Text("Are you sure you want to delete this transaction?")
            },

            confirmButton = {

                Button(
                    onClick = {

                        showDeleteDialog = false

                        transactionToDelete?.let { transaction ->

                            CoroutineScope(Dispatchers.IO).launch {

                                dao.delete(transaction)

                                val updatedList = dao.getAllTransactions()

                                withContext(Dispatchers.Main) {
                                    transactions = updatedList
                                }
                            }
                        }
                    }
                ) {
                    Text("Delete")
                }

            },

            dismissButton = {

                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }

            }

        )

    }

}

