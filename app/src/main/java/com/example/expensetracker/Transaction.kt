package com.example.expensetracker

data class Transaction(
    val amount: Int,
    val type: String,
    val date: String,
    val timestamp: Long,
    val name: String,
    var excluded: Boolean = false
)