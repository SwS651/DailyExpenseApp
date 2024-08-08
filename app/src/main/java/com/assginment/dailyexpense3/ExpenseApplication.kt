package com.assginment.dailyexpense3

import android.app.Application
import com.assginment.dailyexpense3.db.ExpenseRepository
import com.assginment.dailyexpense3.db.ExpenseRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ExpenseApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ExpenseRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { ExpenseRepository(database.expenseDao()) }
}