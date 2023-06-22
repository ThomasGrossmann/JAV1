package com.example.bookmybook.models

import java.util.Date

var rentList = mutableListOf<Rent>()

data class Rent(
    val bookId: Long,
    val returnDate: Date,
    val contactId: Long,
    val id: Int = rentList.size
)