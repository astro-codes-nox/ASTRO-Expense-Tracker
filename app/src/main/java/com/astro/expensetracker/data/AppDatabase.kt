package com.astro.expensetracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Transaction::class,
        Budget::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun budgetDao(): BudgetDao

}