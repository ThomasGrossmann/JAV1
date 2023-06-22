package com.example.bookmybook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val bookQuery = ("CREATE TABLE " + BOOKS_TABLE_NAME + "("
                + BOOK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_COLUMN_TITLE + " TEXT, "
                + BOOK_COLUMN_ISBN + " TEXT, "
                + BOOK_COLUMN_AUTHOR + " TEXT" + ")")
        db?.execSQL(bookQuery)

        val rentQuery = ("CREATE TABLE " + RENTS_TABLE_NAME + "("
                + RENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RENT_COLUMN_BOOK_ID + " INTEGER, "
                + RENT_COLUMN_RETURN_DATE + " TEXT, "
                + RENT_COLUMN_CONTACT_ID + " INTEGER" + ")")
        db?.execSQL(rentQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $BOOKS_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $RENTS_TABLE_NAME")

        onCreate(db)
    }

    fun addBook(title: String, isbn: String, author: String) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(BOOK_COLUMN_TITLE, title)
        values.put(BOOK_COLUMN_ISBN, isbn)
        values.put(BOOK_COLUMN_AUTHOR, author)

        db.insert(BOOKS_TABLE_NAME, null, values)
        db.close()
    }

    fun getBooks(): Cursor? {
        val db = this.readableDatabase
        return db.query(BOOKS_TABLE_NAME, null, null, null, null, null, null)
    }

    fun getLastThreeBooks(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $BOOKS_TABLE_NAME ORDER BY $BOOK_COLUMN_ID DESC LIMIT 3", null)
    }


    fun addRent(bookId: Long, returnDate: String, contactId: Long) {
        val values = ContentValues()
        values.put(RENT_COLUMN_BOOK_ID, bookId)
        values.put(RENT_COLUMN_RETURN_DATE, returnDate)
        values.put(RENT_COLUMN_CONTACT_ID, contactId)

        val db = this.writableDatabase

        db.insert(RENTS_TABLE_NAME, null, values)
        db.close()
    }

    fun getRents() : Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $RENTS_TABLE_NAME", null)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BookMyBook"

        const val BOOKS_TABLE_NAME = "books"
        const val BOOK_COLUMN_ID = "id"
        const val BOOK_COLUMN_TITLE = "title"
        const val BOOK_COLUMN_ISBN = "isbn"
        const val BOOK_COLUMN_AUTHOR = "author"

        const val RENTS_TABLE_NAME = "rents"
        const val RENT_COLUMN_ID = "id"
        const val RENT_COLUMN_BOOK_ID = "book_id"
        const val RENT_COLUMN_RETURN_DATE = "return_date"
        const val RENT_COLUMN_CONTACT_ID = "contact_id"
    }

}