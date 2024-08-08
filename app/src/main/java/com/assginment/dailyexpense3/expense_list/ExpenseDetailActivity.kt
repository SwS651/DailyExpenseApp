package com.assginment.dailyexpense3.expense_list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.assginment.dailyexpense3.R
import com.assginment.dailyexpense3.db.Expense
import com.bumptech.glide.Glide
import java.io.FileNotFoundException
import java.io.InputStream
private val REQUEST_CODE_STORAGE_PERMISSION = 1001



class ExpenseDetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expense_detail)


        // Get the expense object passed from the previous activity
        val expense = intent.getParcelableExtra<Expense>("expense")

        // Find the views
        val titleView = findViewById<TextView>(R.id.expense_detail_title)
        val amountView = findViewById<TextView>(R.id.expense_detail_amount)
        val dateView = findViewById<TextView>(R.id.expense_detail_date)
        val categoryView = findViewById<TextView>(R.id.expense_detail_category)
        val notesView = findViewById<TextView>(R.id.expense_detail_notes)
        val locationView = findViewById<TextView>(R.id.expense_detail_location)
        val imageView = findViewById<ImageView>(R.id.expense_detail_image)

        // Set the values
        expense?.let { it ->
            titleView.text = it.title
            amountView.text = "${it.currency} ${it.amount}"
            dateView.text = it.date.toString()  // Format this if needed
            categoryView.text = it.category
            notesView.text = it.notes
            locationView.text = it.location

            // If the image exists, check if URI is accessible
            if (it.imageUri!= null) {
                val uri:Uri = Uri.parse(it.imageUri.toString())
                // Load the image using Glide
                Glide.with(this)
                    .load(uri) // `it.imageUri` should be a String URI or Uri object
                    .into(imageView)
            } else {
                Toast.makeText(this, "Image not accessible", Toast.LENGTH_SHORT).show()
                imageView.setImageResource(R.drawable.baseline_image_24) // Set a placeholder image if no image
            }
        }

        findViewById<ImageButton>(R.id.button_back).setOnClickListener{
            val intent = Intent(this, ExpenseListActivity::class.java)
            startActivity(intent)
            finish()
        }

    }




}