package com.assginment.dailyexpense3.expense_list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assginment.dailyexpense3.ExpenseApplication
import com.assginment.dailyexpense3.ExpenseListAdapter
import com.assginment.dailyexpense3.MainActivity
import com.assginment.dailyexpense3.R
import com.assginment.dailyexpense3.db.Expense
import com.assginment.dailyexpense3.db.ExpenseViewModel
import com.assginment.dailyexpense3.db.ExpenseViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ExpenseListActivity : AppCompatActivity() {




    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as ExpenseApplication).repository)
    }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        var recyclerView = findViewById<RecyclerView>(R.id.expense_view)
        var adapter = ExpenseListAdapter(expenseViewModel){
                expense ->
            // Navigate to the detail page when an item is clicked
            val intent = Intent(this, ExpenseDetailActivity::class.java)
            intent.putExtra("expense", expense) // Pass the expense object to the detail activity
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        expenseViewModel.getExpensesForLast7Days().observe(this , Observer { expenses ->
            adapter.submitList(expenses)
        })

        var bottomNavView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavView.setOnItemSelectedListener   { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_sevendays -> {
                    expenseViewModel.getExpensesForLast7Days().observe(this , Observer { expenses ->
                        adapter.submitList(expenses)
                    })}
                R.id.nav_fifteendays -> {expenseViewModel.getExpensesForLast15Days().observe(this , Observer { expenses ->
                    adapter.submitList(expenses)
                })}
                R.id.nav_all -> getAllExpenses(adapter)
            }
            true
        }

        var buttonBack = findViewById<ImageButton>(R.id.button_back)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val searchBar = findViewById<EditText>(R.id.search_bar)
        searchBar.addTextChangedListener { text ->
            expenseViewModel.searchExpenses(text.toString()).observe(this, Observer { expenses ->
                adapter.submitList(expenses)
            })
        }
    }



    private fun getAllExpenses(adapter: ExpenseListAdapter) {
        expenseViewModel.allExpenses.observe(this, Observer { expenses ->
            adapter.submitList(expenses)
        })
    }
}