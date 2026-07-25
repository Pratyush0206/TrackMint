package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SmsParser {

    fun parseSms(
        id: Long,
        sms: String,
        timestamp: Long
    ): Transaction? {

        val type = when {
            sms.contains("debited", ignoreCase = true) -> "DEBIT"

            sms.contains("credited", ignoreCase = true) -> "CREDIT"

            else -> return null
        }
        val amountRegex = Regex("""Rs\.?\s([\d,]+(\.\d+)?)""")

        val match = amountRegex.find(sms) ?: return null

        val amount = match.groupValues[1]
            .replace(",", "")
            .toDouble()
            .toInt()

        val date = SimpleDateFormat(
            "dd MMM yyyy • hh:mm a",
            Locale.getDefault()
        ).format(Date(timestamp))

        val name = when (type) {
            "DEBIT" ->
                Regex(""";\s*(.*?)\s+credited""", RegexOption.IGNORE_CASE)
                    .find(sms)
                    ?.groupValues?.get(1)
                    ?: "Unknown"

            "CREDIT" ->
                Regex("""from\s+(.*?)\.""", RegexOption.IGNORE_CASE)
                    .find(sms)
                    ?.groupValues?.get(1)
                    ?: "Unknown"

            else -> "Unknown"
        }

        return Transaction(
            id = id,
            amount = amount,
            type = type,
            date = date,
            timestamp = timestamp,
            name = name
        )
    }
}