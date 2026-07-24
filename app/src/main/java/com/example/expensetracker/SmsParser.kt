package com.example.expensetracker

object SmsParser {

    fun parseSms(sms: String): Transaction? {

        val type = when {
            sms.contains("debited for", ignoreCase = true) -> "DEBIT"

            sms.contains("credited with", ignoreCase = true) -> "CREDIT"

            else -> return null
        }
        val amountRegex = Regex("""Rs\s(\d+(\.\d+)?)""")

        val match = amountRegex.find(sms) ?: return null

        val amount = match.groupValues[1].toDouble().toInt()

        val dateRegex = Regex("""\d{2}-[A-Za-z]{3}-\d{2}""")

        val date = dateRegex.find(sms)?.value ?: return null

        return Transaction(
            amount = amount,
            type = type,
            date = date
        )
    }
}