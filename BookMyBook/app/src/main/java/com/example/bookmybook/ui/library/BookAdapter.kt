package com.example.bookmybook.ui.library

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.bookmybook.R
import com.example.bookmybook.models.Book
import java.util.Collections
import java.util.Locale

class BookAdapter(
    private val context: Activity,
    private val bookList: MutableList<Book>
) : ArrayAdapter<Book>(context, R.layout.book_item, bookList), Filterable {

    private var filteredData: MutableList<Book> = bookList.toMutableList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.book_item, null, true)

        val titleText = rowView.findViewById(R.id.text_list_book_title) as TextView
        titleText.text = filteredData[position].title

        return rowView
    }

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): Book {
        return filteredData[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val query = constraint?.toString()?.toLowerCase(Locale.getDefault())

                val filteredTitles = if (query.isNullOrBlank()) {
                    bookList.toMutableList()
                } else {
                    bookList.filter { it.title.toLowerCase(Locale.getDefault()).contains(query) }
                        .toMutableList()
                }

                results.values = filteredTitles
                results.count = filteredTitles.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as? MutableList<Book> ?: bookList.toMutableList()
                notifyDataSetChanged()
            }
        }
    }

    override fun add(item: Book?) {
        item?.let {
            if (bookList.isEmpty()) {
                bookList.add(it)
                filteredData.add(it)
            } else {
                val insertionIndex = Collections.binarySearch(bookList, it) { book1, book2 ->
                    book1.title.compareTo(book2.title)
                }
                val index = if (insertionIndex >= 0) insertionIndex else -insertionIndex - 1
                bookList.add(index, it)
                filteredData.add(index, it)
            }
            notifyDataSetChanged()
        }
    }


}

