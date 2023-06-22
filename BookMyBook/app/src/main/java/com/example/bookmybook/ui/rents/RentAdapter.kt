package com.example.bookmybook.ui.library

import Rent
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.bookmybook.R
import com.example.bookmybook.models.Book
import java.util.*
import kotlin.collections.ArrayList

class RentAdapter(
    context: Context,
    private var rentList: MutableList<Rent>
) : ArrayAdapter<Rent>(context, R.layout.rent_item, rentList), Filterable {

    override fun getCount(): Int {
        return rentList.size
    }

    override fun getItem(position: Int): Rent {
        return rentList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView = convertView ?: inflater.inflate(R.layout.rent_item, parent, false)

        val rent = rentList[position]

        val rentText = rowView.findViewById<TextView>(R.id.text_list_rent)
        rentText.text = "From " + rent.getFormattedDate(rent.startDate)

        val returnDateText = rowView.findViewById<TextView>(R.id.text_list_return_date)
        returnDateText.text = " due on the " + rent.getFormattedDate(rent.returnDate)

        val contactText = rowView.findViewById<TextView>(R.id.text_list_contact)
        contactText.text = "Rented by Anonymous"

        return rowView
    }

    fun updateRentList(newRentList: MutableList<Rent>) {
        rentList = newRentList
        notifyDataSetChanged()
    }
}