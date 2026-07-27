package com.astro.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class Budget(

    @PrimaryKey
    val id: Int = 1,

    val amount: Double
)