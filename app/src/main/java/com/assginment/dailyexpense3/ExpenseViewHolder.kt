package com.assginment.dailyexpense3


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assginment.dailyexpense3.db.Expense
import java.text.SimpleDateFormat
import java.util.Locale


class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val expenseItemView: TextView = itemView.findViewById(R.id.expense_title)
    private val expenseAmountView: TextView = itemView.findViewById(R.id.expense_amount)
    private val expenseDateView: TextView = itemView.findViewById(R.id.expense_datetime)
    private val expenseCategoryView: TextView = itemView.findViewById(R.id.expense_category)


    fun bind(expense: Expense) {
        // Format the Date to "yyyy-MM-dd"
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = formatter.format(expense.date)
        expenseItemView.text = expense.title
        expenseAmountView.text = "${expense.currency} ${expense.amount}"
        expenseDateView.text = formattedDate
        expenseCategoryView.text = expense.category

    }
    companion object {
        fun create(parent: ViewGroup): ExpenseViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
            return ExpenseViewHolder(view)
        }
    }
}
