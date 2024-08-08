package com.assginment.dailyexpense3.db

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.concurrent.Executors

class ExpenseRepository(private val expenseDao: ExpenseDao) {
//    private var mExpenseDao: ExpenseDao
    var mAllExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }



    fun getExpensesFromDate(startDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesFromDate(startDate)
    }

    @WorkerThread
    fun delete(expense: Expense) {  // Add this method
        expenseDao.delete(expense)
    }

    fun searchExpenses(searchQuery: String): Flow<List<Expense>> {
        return expenseDao.searchExpenses("%$searchQuery%")
    }

    fun filterExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.filterExpensesByDateRange(startDate, endDate)
    }


}