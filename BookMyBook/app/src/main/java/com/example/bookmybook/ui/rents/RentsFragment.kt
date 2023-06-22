package com.example.bookmybook.ui.rents

import Rent
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookmybook.databinding.FragmentRentsBinding
import com.example.bookmybook.DBHelper
import com.example.bookmybook.ui.library.RentAdapter
import rentList
import java.util.Calendar

class RentsFragment : Fragment() {

    private var _binding: FragmentRentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: RentAdapter
    private lateinit var listView: ListView
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listView = binding.myRentsLv
        dbHelper = DBHelper(requireContext(), null)

        listAdapter = RentAdapter(requireContext(), rentList)
        listView.adapter = listAdapter

        binding.fab.setOnClickListener {
            showCalendarPicker()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        fetchRentsFromDatabase()
    }

    private fun fetchRentsFromDatabase() {
        val rentsCursor = dbHelper.getRents()
        val newRentList: MutableList<Rent> = mutableListOf()

        rentsCursor?.use {
            while (it.moveToNext()) {
                val bookId = it.getLong(it.getColumnIndexOrThrow(DBHelper.RENT_COLUMN_BOOK_ID))
                val contactId = it.getLong(it.getColumnIndexOrThrow(DBHelper.RENT_COLUMN_CONTACT_ID))
                val startDateMillis = it.getLong(it.getColumnIndexOrThrow(DBHelper.RENT_COLUMN_START_DATE))
                val returnDateMillis = it.getLong(it.getColumnIndexOrThrow(DBHelper.RENT_COLUMN_RETURN_DATE))
                val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.RENT_COLUMN_ID))

                val startDate = Calendar.getInstance()
                startDate.timeInMillis = startDateMillis

                val returnDate = Calendar.getInstance()
                returnDate.timeInMillis = returnDateMillis

                val rent = Rent(bookId, contactId, startDate, returnDate, id)
                newRentList.add(rent)
            }
        }

        listAdapter.updateRentList(newRentList)
    }

    private fun addRent(bookId: Long, startDate: Calendar, returnDate: Calendar, contactId: Long) {
        val startDateMillis = startDate.timeInMillis
        val returnDateMillis = returnDate.timeInMillis

        dbHelper.addRent(bookId, returnDateMillis.toString(), startDateMillis.toString(), contactId)
        Toast.makeText(requireContext(), "Rent added successfully", Toast.LENGTH_SHORT).show()
        fetchRentsFromDatabase()
    }

    private fun showCalendarPicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)

            val bookId = 1L // Set the desired book ID
            val contactId = 1L // Set the desired contact ID

            addRent(bookId, Calendar.getInstance(), selectedDate, contactId)
        }, year, month, day)

        datePicker.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
