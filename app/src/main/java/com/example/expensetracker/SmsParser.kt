package com.example.expensetracker

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

        val dateRegex = Regex("""\d{2}-[A-Za-z]{3}-\d{2}""")

        val date = dateRegex.find(sms)?.value ?: return null

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