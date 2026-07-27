package com.astro.expensetracker.utils

import android.content.Context
import com.astro.expensetracker.data.Transaction
import com.google.gson.Gson
import java.io.File

object BackupManager {

    fun backupTransactions(
        context: Context,
        transactions: List<Transaction>
    ): File {

        val gson = Gson()

        val json = gson.toJson(transactions)

        val file = File(
            context.getExternalFilesDir(null),
            "ASTRO_Backup.json"
        )

        file.writeText(json)

        return file
    }
}