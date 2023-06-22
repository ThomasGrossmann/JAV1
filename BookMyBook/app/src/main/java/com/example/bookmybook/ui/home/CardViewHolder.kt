package com.example.bookmybook.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.example.bookmybook.databinding.CardCellBinding
import com.example.bookmybook.models.Book

class CardViewHolder (private val cardCellBinding: CardCellBinding) : RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindBook(book: Book) {
        cardCellBinding.textTitlePo.text = book.title
        cardCellBinding.textAuthorPo.text = "by : " + book.author
        cardCellBinding.textIsbnPo.text = book.isbn
    }
}