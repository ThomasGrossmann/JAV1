package com.example.bookmybook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookmybook.DBHelper
import com.example.bookmybook.databinding.FragmentHomeBinding
import com.example.bookmybook.models.Book

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bookCountText = binding.textNbbooks
        val rentCountText = binding.textNbrents
        val db = DBHelper(requireContext(), null)

        val bookCount = db.getBooks()
        val rentCount = db.getRents()
        val lastAddedBooks = db.getLastThreeBooks()
        val lastThreeBooks = ArrayList<Book>()

        lastAddedBooks?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.BOOK_COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndex(DBHelper.BOOK_COLUMN_TITLE))
                val isbn = cursor.getString(cursor.getColumnIndex(DBHelper.BOOK_COLUMN_ISBN))
                val author = cursor.getString(cursor.getColumnIndex(DBHelper.BOOK_COLUMN_AUTHOR))

                val book = Book(title, isbn, author, id.toInt())
                lastThreeBooks.add(book)
            }
        }

        binding.recyclerLastBooks.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = CardAdapter(lastThreeBooks)
        }

        if (bookCount != null) {
            bookCountText.text = bookCount.count.toString()
        }

        if (rentCount != null) {
            rentCountText.text = rentCount.count.toString()
        }

        db.close()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
