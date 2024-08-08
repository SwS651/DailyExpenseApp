package com.assginment.dailyexpense3.datetime

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment

import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    // Define an interface to communicate the date back to the host activity or fragment
    interface OnDateSetListener {
        fun onDateSet(year: Int, month: Int, day: Int)
    }

    private var listener: OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it.
        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Send the selected date back to the host activity or fragment
        listener?.onDateSet(year, month, day)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure the host activity or fragment implements the callback interface
        listener = try {
            context as OnDateSetListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDateSetListener")
        }
    }




}