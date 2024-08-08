package com.assginment.dailyexpense3.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ExpenseViewModel(private val repository: ExpenseRepository): ViewModel(){

     val allExpenses: LiveData<List<Expense>> = repository.mAllExpenses.asLiveData()


    fun insert(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(expense)
    }

    fun delete(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {  // Add this method
        repository.delete(expense)
    }

    fun getExpensesFromDate(startDate: Date): LiveData<List<Expense>> {
        return repository.getExpensesFromDate(startDate).asLiveData()
    }

    fun getExpensesForLast7Days(): LiveData<List<Expense>> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
//        val startDate = sdf.format(calendar.time)
        val startDate =calendar.time
        return getExpensesFromDate(startDate)
    }

    fun getExpensesForLast15Days(): LiveData<List<Expense>> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -15)
        val startDate =calendar.time
        return getExpensesFromDate(startDate)
    }

    fun searchExpenses(query: String): LiveData<List<Expense>> {
        return repository.searchExpenses(query).asLiveData()
    }

    fun filterExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return repository.filterExpensesByDateRange(startDate, endDate).asLiveData()
    }


}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
