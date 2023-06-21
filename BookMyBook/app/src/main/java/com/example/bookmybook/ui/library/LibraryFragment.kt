package com.example.bookmybook.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookmybook.R
import com.example.bookmybook.databinding.FragmentLibraryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val list: MutableList<String> = mutableListOf(
        "A", "AB", "B", "BC", "C", "CD", "D", "DE", "E", "EF", "F", "FG", "G", "GH", "H", "HI"
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val searchView = binding.myBooksSearch
        val listView = binding.myBooksLv

        val listAdapter = BookAdapter(requireActivity(), list)
        listView.adapter = listAdapter

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

        // Find the EditText and Button inside the bottom sheet layout
        val editTextBookTitle = view.findViewById<EditText>(R.id.editTextBookTitle)
        val buttonAddBook = view.findViewById<Button>(R.id.buttonAddBook)

        buttonAddBook.setOnClickListener {
            val bookTitle = editTextBookTitle.text.toString()

            if (bookTitle.isNotBlank()) {
                // Add the book to the existing list
                listAdapter.add(bookTitle)

                // Dismiss the bottom sheet dialog
                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter a book title", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.setOnDismissListener {
            // Notify the adapter that the list has changed
            listAdapter.notifyDataSetChanged()
        }

        bottomSheetDialog.show()
    }


}
