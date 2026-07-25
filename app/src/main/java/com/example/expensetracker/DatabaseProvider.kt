package com.example.expensetracker

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    val db by lazy {

        Room.databaseBuilder(
            App.context,
            ExpenseDatabase::class.java,
            "expense_database"
        ).build()
    }
}