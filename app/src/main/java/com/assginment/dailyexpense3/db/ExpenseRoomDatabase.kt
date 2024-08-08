package com.assginment.dailyexpense3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.assginment.dailyexpense3.datetime.Converters
import kotlinx.coroutines.CoroutineScope
import kotlin.concurrent.Volatile

@Database(entities = arrayOf(Expense::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ExpenseRoomDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao



    companion object {
        @Volatile
        private var INSTANCE: ExpenseRoomDatabase? = null


        fun getDatabase(context: Context,scope: CoroutineScope): ExpenseRoomDatabase {

//            context.deleteDatabase("expense_database");
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseRoomDatabase::class.java,
                    "expense_database"
//                ).fallbackToDestructiveMigration().build()
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}


