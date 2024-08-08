package com.assginment.dailyexpense3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assginment.dailyexpense3.db.Expense
import com.assginment.dailyexpense3.db.ExpenseViewModel


class ExpenseListAdapter(private val viewModel: ExpenseViewModel,private val onItemClicked: (Expense) -> Unit ) : ListAdapter<Expense, ExpenseViewHolder>(ExpensesComparator()) {

    private var expenseList: List<Expense> = emptyList()
//    private val expenseItemLayout =  R.layout.recyclerview_item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {

        val current = getItem(position)
        holder.bind(current)

        holder.itemView.setOnClickListener {
            onItemClicked(current) // Handle item click
        }
        // Handle delete button click or swipe
        holder.itemView.findViewById<Button>(R.id.delete_button)?.setOnClickListener {
            viewModel.delete(current)
        }
    }



    class ExpensesComparator : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.title == newItem.title
        }
    }
}