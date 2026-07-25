package com.example.expensetracker

import android.content.Context

object TransactionSyncManager {

    suspend fun sync(context: Context) {

        val dao = DatabaseProvider.db.transactionDao()

        val repository = TransactionRepository(dao)

        val smsTransactions = SmsReader.readTransactions(context)

        repository.saveTransactions(smsTransactions)

    }
}