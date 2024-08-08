package com.assginment.dailyexpense3.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assginment.dailyexpense3.db.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(expense: Expense)

    @Query("DELETE FROM expense_table")
    fun deleteAll()

    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>


    @Query("SELECT * FROM expense_table WHERE date >= :startDate ORDER BY date DESC")
    fun getExpensesFromDate(startDate: Date): Flow<List<Expense>>

    @Delete
    fun delete(expense: Expense)

    @Query("SELECT * FROM expense_table WHERE title LIKE :searchQuery OR category LIKE :searchQuery ORDER BY date DESC")
    fun searchExpenses(searchQuery: String): Flow<List<Expense>>

    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun filterExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>
}