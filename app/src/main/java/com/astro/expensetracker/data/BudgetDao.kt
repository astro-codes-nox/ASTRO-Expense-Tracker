package com.astro.expensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBudget(
        budget: Budget
    )

    @Query("SELECT * FROM budget WHERE id = 1")
    suspend fun getBudget(): Budget?
}