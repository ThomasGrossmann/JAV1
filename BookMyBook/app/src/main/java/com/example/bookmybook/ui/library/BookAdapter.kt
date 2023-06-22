package com.example.bookmybook.ui.library

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

class BookAdapter(
    context: Context,
    private val bookList: MutableList<Book>
) : ArrayAdapter<Book>(context, R.layout.book_item, bookList), Filterable {

    private var filteredBookList: MutableList<Book> = ArrayList()

    override fun getCount(): Int {
        return filteredBookList.size
    }

    override fun getItem(position: Int): Book {
        return filteredBookList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView = convertView ?: inflater.inflate(R.layout.book_item, parent, false)

        val titleText = rowView.findViewById<TextView>(R.id.text_list_book_title)
        titleText.text = filteredBookList[position].title

        return rowView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val query = constraint?.toString()?.toLowerCase(Locale.getDefault())

                if (query.isNullOrBlank()) {
                    filteredBookList = ArrayList(bookList)
                } else {
                    filteredBookList = bookList.filter {
                        it.title.toLowerCase(Locale.getDefault()).contains(query)
                    }.toMutableList()
                }

                results.values = filteredBookList
                results.count = filteredBookList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBookList = results?.values as? MutableList<Book> ?: ArrayList()
                notifyDataSetChanged()
            }
        }
    }

    fun addBook(book: Book) {
        bookList.add(book)
        bookList.sortBy { it.title } // Sort the book list by title
        notifyDataSetChanged()
    }
}