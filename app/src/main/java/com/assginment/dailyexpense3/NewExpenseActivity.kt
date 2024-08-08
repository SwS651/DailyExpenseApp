package com.assginment.dailyexpense3

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assginment.dailyexpense3.datetime.DatePickerFragment
import com.assginment.dailyexpense3.db.Expense
import com.assginment.dailyexpense3.db.ExpenseViewModel
import com.assginment.dailyexpense3.db.ExpenseViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewExpenseActivity : AppCompatActivity() ,DatePickerFragment.OnDateSetListener, AdapterView.OnItemSelectedListener{
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_CODE_STORAGE_PERMISSION = 1001

    private val catNames = arrayOf("Dinning","Entertainment","Bill")
    private lateinit var tvCategory: TextView

    private var etNotes: EditText? = null
    private var selectCategory:Button? = null


    private var currencyType = "MYR"
    private var selectedCatIndex = 0
    var isAllFieldsChecked = false
    var imgpatht:String?=null

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as ExpenseApplication).repository)
    }

    private lateinit var mEditTitleView: EditText
    private lateinit var mEditAmountView: EditText
    private lateinit var mEditDateView: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expense)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.form_expense_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mEditTitleView = findViewById(R.id.edit_title)
        mEditAmountView = findViewById(R.id.edit_amount)
        mEditDateView = findViewById(R.id.edit_date)
        tvCategory = findViewById<TextView>(R.id.text_category)
        etNotes = findViewById(R.id.edit_text_notes)
        selectCategory = findViewById(R.id.button_select_category)



        //pick date
        findViewById<Button>(R.id.button_pickDate).setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
        }

        //Spinner Currency Setup
        val spinner: Spinner = findViewById(R.id.spinner_currency)
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.currency_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        //Implement select image button
        findViewById<Button>(R.id.select_image_button).setOnClickListener {
            requestStoragePermission()
        }

        //Button Save
        val buttonSave = findViewById<Button>(R.id.button_save)
        buttonSave.setOnClickListener {

            isAllFieldsChecked = CheckAllFields()
            if (isAllFieldsChecked){

                val title: String = mEditTitleView.getText().toString()
                val amount:Double = mEditAmountView.getText().toString().toDouble()
                val datet:String = mEditDateView.getText().toString()
                val notet:String = etNotes?.getText().toString()
                var location_text = findViewById<EditText>(R.id.et_location).text.toString()


                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsedDate: Date = formatter.parse(datet)

                val expense = Expense(
                    title = title,
                    amount = amount,
                    date = parsedDate,
                    category = tvCategory.getText().toString(),
                    currency = currencyType,
                    notes = notet,
                    location = location_text,
                    imageUri = imgpatht.toString())
                expenseViewModel.insert(expense)


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(
                    applicationContext,
                    "Text Fields required to fill in",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //Go back Button Listener
        findViewById<ImageButton>(R.id.button_cancel).setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            }else{
                openGallery()
            }
        } else {
            // For Android 12 and below
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            }else{
                openGallery()
            }
        }
    }

    // Handle the permission request response
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with reading external storage
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageView:ImageView = findViewById(R.id.expense_image)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                imageView.setImageURI(imageUri) // Safely use imageView
                imgpatht = imageUri.toString()
            }
        }
    }

    //Category
    fun categorySelect(view:View){

        val catDialog = MaterialAlertDialogBuilder(this)
        catDialog.setTitle("Select category")
        catDialog.setSingleChoiceItems(catNames,selectedCatIndex){ dialog, which->
            selectedCatIndex = which
            tvCategory.text= catNames[which]
        }
        catDialog.setPositiveButton("Ok"){
                dialog,which->
            if (which >= 0 && which < catNames.size) {
                // Make sure 'which' is a valid index
                tvCategory.text = catNames[which] // Update the TextView
            }
        }

        catDialog.setNegativeButton("Cancel"){
                dialog,which ->
            dialog.dismiss()
        }
        catDialog.show()
    }



    // Handle the selected date
    override fun onDateSet(year: Int, month: Int, day: Int) {
        // Do something with the selected date, e.g., display it in a TextView
        findViewById<TextView>(R.id.edit_date).text = "$year-${month + 1}-$day"
    }
    //The methods below used for spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        currencyType= parent?.getItemAtPosition(position).toString()
        Toast.makeText(this, currencyType, Toast.LENGTH_SHORT).show()
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        currencyType= parent?.getItemAtPosition(0).toString()
    }

    //Form Validation
    @SuppressLint("SetTextI18n")
    private fun CheckAllFields(): Boolean {
        if (mEditTitleView!!.length() == 0) {
            mEditTitleView!!.error = "Expense Name field is required"
            return false
        }
        if (mEditAmountView!!.length() == 0) {
            mEditAmountView!!.error = "Amount field is required"
            return false
        }
        if (tvCategory!!.length() == 0) {
            findViewById<TextView>(R.id.error_text).setText("Category is required")
            return false
        }
        if (currencyType.isEmpty()) {
            findViewById<TextView>(R.id.error_text).setText("Currency is required")
            return false
        }
        else if (mEditDateView!!.length() == 0) {
            findViewById<TextView>(R.id.error_text).setText("Date is required")
            return false
        }

        // after all validation return true.
        return true
    }

}