package com.example.expensetracker

import android.content.Context
import android.provider.Telephony

object SmsReader {

    fun readTransactions(context: Context): List<Transaction> {

        val transactions = mutableListOf<Transaction>()

        val cursor = context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {

            while (it.moveToNext()) {

                val body = it.getString(
                    it.getColumnIndexOrThrow(Telephony.Sms.BODY)
                )

                val timestamp = it.getLong(
                    it.getColumnIndexOrThrow(Telephony.Sms.DATE)
                )

                val id = it.getLong(
                    it.getColumnIndexOrThrow(Telephony.Sms._ID)
                )

                val transaction = SmsParser.parseSms(
                    id = id,
                    sms = body,
                    timestamp = timestamp
                )

                if (transaction != null) {

                    transactions.add(transaction)
                }
            }

        }

        return transactions
    }

}