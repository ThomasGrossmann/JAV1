package com.example.bookmybook.models

var bookList = mutableListOf<Book>()

data class Book (
    val title: String = "",
    val isbn: String = "",
    val author: String = "",
    val id: Int = bookList.size
)