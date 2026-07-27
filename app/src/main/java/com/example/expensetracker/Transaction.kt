package com.example.expensetracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val id: Long,

    val amount: Int,
    val type: String,
    val date: String,
    val timestamp: Long,
    val name: String,
    var excluded: Boolean = false,
    val notes: String = ""
)