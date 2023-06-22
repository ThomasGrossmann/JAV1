package com.example.bookmybook.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
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
    private lateinit var listAdapter: BookAdapter
    private val bookList: MutableList<Book> = mutableListOf()
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchView = binding.myBooksSearch
        listView = binding.myBooksLv

        listAdapter = BookAdapter(requireActivity(), bookList)
        listView.adapter = listAdapter

        val db = DBHelper(requireContext(), null)
        val cursor = db.getBooks()
        cursor?.let {
            while (it.moveToNext()) {
                val title = it.getString(it.getColumnIndexOrThrow(DBHelper.BOOK_COLUMN_TITLE))
                val isbn = it.getString(it.getColumnIndexOrThrow(DBHelper.BOOK_COLUMN_ISBN))
                val author = it.getString(it.getColumnIndexOrThrow(DBHelper.BOOK_COLUMN_AUTHOR))
                val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.BOOK_COLUMN_ID))
                val book = Book(title, isbn, author, id)
                bookList.add(book)
            }
            it.close()

            listAdapter.notifyDataSetChanged()
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
            showAddBookBottomSheet()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddBookBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(view)

        val editTextBookTitle = view.findViewById<EditText>(R.id.editTextBookTitle)
        editTextBookTitle.isSingleLine = true
        val editTextBookAuthor = view.findViewById<EditText>(R.id.editTextBookAuthor)
        editTextBookAuthor.isSingleLine = true
        val editTextBookIsbn = view.findViewById<EditText>(R.id.editTextBookIsbn)
        editTextBookIsbn.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        editTextBookIsbn.isSingleLine = true
        val buttonAddBook = view.findViewById<Button>(R.id.buttonAddBook)

        buttonAddBook.setOnClickListener {
            if (inputCheck(
                    editTextBookTitle.text.toString(),
                    editTextBookAuthor.text.toString(),
                    editTextBookIsbn.text.toString()
                )
            ) {
                val db = DBHelper(requireContext(), null)
                val bookTitle = editTextBookTitle.text.toString()
                val bookAuthor = editTextBookAuthor.text.toString()
                val bookIsbn = editTextBookIsbn.text.toString()

                val book = Book(bookTitle, bookIsbn, bookAuthor)
                db.addBook(bookTitle, bookIsbn, bookAuthor)

                listAdapter.addBook(book)

                listAdapter.notifyDataSetChanged()

                Toast.makeText(requireContext(), "Book successfully added.", Toast.LENGTH_SHORT).show()

                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill all the fields.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        bottomSheetDialog.setOnDismissListener {
            editTextBookTitle.text.clear()
            editTextBookAuthor.text.clear()
            editTextBookIsbn.text.clear()
        }

        bottomSheetDialog.show()
    }

    private fun inputCheck(title: String, author: String, isbn: String): Boolean {
        return !(title.isEmpty() || author.isEmpty() || isbn.isEmpty())
    }
}
