package com.example.bookmybook.ui.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookmybook.DBHelper
import com.example.bookmybook.R
import com.example.bookmybook.databinding.FragmentLibraryBinding
import com.example.bookmybook.models.Book
import com.google.android.material.bottomsheet.BottomSheetDialog

class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val bookList: MutableList<Book> = mutableListOf()

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val searchView = binding.myBooksSearch
        val listView = binding.myBooksLv

        val listAdapter = BookAdapter(requireActivity(), bookList)
        listView.adapter = listAdapter

        // Fetch books from the database and populate the bookList
        val db = DBHelper(requireContext(), null)
        val cursor = db.getBooks()
        cursor?.let {
            while (it.moveToNext()) {
                val title = it.getString(it.getColumnIndex(DBHelper.BOOK_COLUMN_TITLE))
                val isbn = it.getString(it.getColumnIndex(DBHelper.BOOK_COLUMN_ISBN))
                val author = it.getString(it.getColumnIndex(DBHelper.BOOK_COLUMN_AUTHOR))
                val id = it.getInt(it.getColumnIndex(DBHelper.BOOK_COLUMN_ID))
                val book = Book(title, isbn, author, id)
                bookList.add(book)
            }
            it.close()
        }
        db.close()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })

        val addButton = binding.fab
        addButton.setOnClickListener {
            showAddBookBottomSheet(listAdapter)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddBookBottomSheet(listAdapter: BookAdapter) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(view)

        val editTextBookTitle = view.findViewById<EditText>(R.id.editTextBookTitle)
        val editTextBookAuthor = view.findViewById<EditText>(R.id.editTextBookAuthor)
        val editTextBookIsbn = view.findViewById<EditText>(R.id.editTextBookIsbn)
        val buttonAddBook = view.findViewById<Button>(R.id.buttonAddBook)

        buttonAddBook.setOnClickListener {
            val db = DBHelper(requireContext(), null)
            val bookTitle = editTextBookTitle.text.toString()
            val bookAuthor = editTextBookAuthor.text.toString()
            val bookIsbn = editTextBookIsbn.text.toString()

            val book = Book(bookTitle, bookIsbn, bookAuthor)
            db.addBook(bookTitle, bookAuthor, bookIsbn)

            Toast.makeText(requireContext(), "Book successfully added.", Toast.LENGTH_SHORT).show()

            // Add the new book to the adapter instead of clearing it
            listAdapter.add(book)

            bottomSheetDialog.dismiss() // Close the bottom sheet
        }

        bottomSheetDialog.setOnDismissListener {
            // Clear the input fields when the bottom sheet is dismissed
            editTextBookTitle.text.clear()
            editTextBookAuthor.text.clear()
            editTextBookIsbn.text.clear()
        }

        bottomSheetDialog.show()
    }


}
