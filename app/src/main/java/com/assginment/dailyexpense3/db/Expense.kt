package com.assginment.dailyexpense3.db


import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@Parcelize
@Entity(tableName = "expense_table")
class Expense(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "date") val date: Date,

    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "notes") val notes: String? = "",
    @ColumnInfo(name = "imageUri") val imageUri: String? = "",
    @ColumnInfo(name = "location") val location: String? =""

):Parcelable


//val expense = Expense(
//    title = "Grocery Shopping",
//    amount = 45.75,
//    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
//    category = "Shopping",
//    currency = "USD",
//    notes = "Bought some fruits and veggies",
//    imageUri = null,
//    location = null
//)
//expenseViewModel.insert(expense)

