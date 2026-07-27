package com.astro.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.astro.expensetracker.ui.theme.ThemeManager
import androidx.compose.foundation.background
import android.widget.Toast
import com.astro.expensetracker.utils.BackupManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
@Composable
fun SettingsScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {

        Text(
            text = "⚙️ Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "🌙 Dark Mode",
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Switch(
                        checked = ThemeManager.isDarkTheme.value,
                        onCheckedChange = {
                            ThemeManager.isDarkTheme.value = it
                        }
                    )

                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "📱 App Version",
                    style = MaterialTheme.typography.titleMedium
                )

                Text("Version 1.1")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "👨‍💻 Developer",
                    style = MaterialTheme.typography.titleMedium
                )

                Text("Astro")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "❤️ About",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "📧 Contact",
                    style = MaterialTheme.typography.titleMedium
                )

                Text("astro.dev.team@gmail.com")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "⭐ Rate App",
                    style = MaterialTheme.typography.titleMedium
                )

                Text("Coming Soon")

                Text("ASTRO Expense Tracker")
            }
        }
        }
    }