package com.assginment.dailyexpense3



import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assginment.dailyexpense3.db.ExpenseViewModel
import com.assginment.dailyexpense3.db.ExpenseViewModelFactory
import com.assginment.dailyexpense3.expense_list.ExpenseDetailActivity
import com.assginment.dailyexpense3.expense_list.ExpenseListActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as ExpenseApplication).repository)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Recycleview
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ExpenseListAdapter(expenseViewModel)  {     expense ->
        // Navigate to the detail page when an item is clicked
            val intent = Intent(this, ExpenseDetailActivity::class.java)
            intent.putExtra("expense", expense) // Pass the expense object to the detail activity
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        expenseViewModel.allExpenses.observe(this) { expenses ->
            // Update the cached copy of the words in the adapter.
            expenses.let { adapter.submitList(it) }
        }

        //Implement floating button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NewExpenseActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button_expenseRecord).setOnClickListener{
            val intent = Intent(this, ExpenseListActivity::class.java)
            startActivity(intent)
            finish()
        }
        findViewById<ImageView>(R.id.buttonSplashSetting).setOnClickListener {
            showSplashScreenDialog()
        }

    }

    //Splash Screen Dialog Setting
    private fun showSplashScreenDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        var sharedPreferences: SharedPreferences =  getSharedPreferences("appPreferences", MODE_PRIVATE)
        // Create a LinearLayout to hold the CheckBox
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(15)
        // Create the CheckBox
        val splashScreenCheckBox = CheckBox(this).apply {
            text = "Show Splash Screen"
            // Optionally set the initial state of the CheckBox if needed
            isChecked = sharedPreferences.getBoolean("skipWelcomeScreen", false)
        }
        // Add the CheckBox to the layout
        layout.addView(splashScreenCheckBox)

        dialogBuilder.setTitle("Settings")
            .setView(layout)
            .setPositiveButton("OK") { _, _ ->
                // Check if the CheckBox is checked
                val isChecked = splashScreenCheckBox.isChecked
                if (isChecked) {
                    // Handle the case when the CheckBox is checked
                    sharedPreferences.edit().putBoolean("skipWelcomeScreen", true).apply()
                } else {
                    // Handle the case when the CheckBox is not checked
                    sharedPreferences.edit().putBoolean("skipWelcomeScreen", false).apply()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = dialogBuilder.create()
        dialog.show()
    }


}