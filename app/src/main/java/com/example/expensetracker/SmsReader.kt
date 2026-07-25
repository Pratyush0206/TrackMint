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

                val transaction = SmsParser.parseSms(body)

                val id = it.getLong(
                    it.getColumnIndexOrThrow(Telephony.Sms._ID)
                )

                val sender = it.getString(
                    it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
                )
                val timestamp = it.getLong(
                    it.getColumnIndexOrThrow(Telephony.Sms.DATE)
                )

                if (transaction != null) {

                    println("ID: $id")
                    println("Sender: $sender")
                    println("Timestamp: $timestamp")
                    println(body)
                    println(transaction)
                    println("----------------------")

                    transactions.add(transaction)
                }
            }

        }

        return transactions
    }

}