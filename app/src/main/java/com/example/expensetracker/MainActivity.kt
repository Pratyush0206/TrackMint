package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting( modifier: Modifier = Modifier) {

    var amount by remember{
        mutableStateOf("")
    }
    var transactions by remember{
        mutableStateOf(listOf<Transaction>())
    }
    val totalDebit = transactions
        .filter { it.type == "DEBIT" }
        .sumOf { it.amount }

    Column(
        modifier=modifier.fillMaxSize().padding(16.dp)
    ){
        Text(
            text = "Expense Tracker",
            fontSize=28.sp
        )
        Spacer(modifier=Modifier.height(20.dp))

        Text(
            text = "Total Debited: ₹$totalDebit",
            fontSize = 22.sp
        )

        OutlinedTextField(
            value=amount,
            onValueChange = {
                amount=it
            },
            label={
                Text("Enter amount")
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
//                if (amount.toIntOrNull() !=null) {
//                    transactions = transactions + Transaction(
//                        amount = amount.toInt(),
//                        type = "DEBIT",
//                        date = "24 Jul 2026"
//                    )
//                    amount = ""
//                }
                    val sms = "Dear Customer, Acct XXXX is credited with Rs 8000.00 on 09-Jul-26 from SUCHITA PAULUS. UPI:xxxxxxx-ICICI Bank."

                    val transaction = SmsParser.parseSms(sms)

                    println(transaction)
                }
        ){
            Text("Add expense")
        }
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(transactions){transaction->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),

                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${transaction.amount}",
                        fontSize = 22.sp
                    )
                    Button(
                        onClick = {
                            transactions = transactions - transaction
                        }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseTrackerTheme {
        Greeting()
    }
}
