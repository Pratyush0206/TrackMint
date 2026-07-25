package com.example.expensetracker

class TransactionRepository(
    private val dao: TransactionDao
) {

    suspend fun saveTransactions(transactions: List<Transaction>) {
        dao.insertTransactions(transactions)
    }

    suspend fun getTransactions(): List<Transaction> {
        return dao.getAllTransactions()
    }

    suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction)
    }
}