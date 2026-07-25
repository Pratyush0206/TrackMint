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
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import java.time.Instant
import java.time.ZoneId
import java.time.YearMonth
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch

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

    val scope = rememberCoroutineScope()

    var transactions by remember {
        mutableStateOf(listOf<Transaction>())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var selectedMonth by remember {
        mutableStateOf(YearMonth.now())
    }

    val filteredTransactions = transactions.filter {

        val transactionMonth = Instant
            .ofEpochMilli(it.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .let { date ->
                YearMonth.of(date.year, date.month)
            }

        transactionMonth == selectedMonth && !it.excluded
    }

    val totalDebit = filteredTransactions
        .filter { it.type == "DEBIT" }
        .sumOf { it.amount }

    val totalCredit = filteredTransactions
        .filter { it.type == "CREDIT" }
        .sumOf { it.amount }

    val netBalance = totalDebit - totalCredit

    val context = LocalContext.current

    val dao = DatabaseProvider.db.transactionDao()
    val repository = TransactionRepository(dao)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

            scope.launch {

                val smsTransactions = SmsReader.readTransactions(context)

                repository.saveTransactions(smsTransactions)

                transactions = repository.getTransactions()

                isLoading = false

            }

        } else {

            println("SMS Permission Denied")

        }
    }

    LaunchedEffect(Unit) {

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val smsTransactions = SmsReader.readTransactions(context)

            repository.saveTransactions(smsTransactions)

            transactions = repository.getTransactions()

            isLoading = false

        } else {

            permissionLauncher.launch(Manifest.permission.READ_SMS)

        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    selectedMonth = selectedMonth.minusMonths(1)
                }
            ) {
                Text("<")
            }

            Text(
                text = "${selectedMonth.month} ${selectedMonth.year}",
                fontSize = 20.sp
            )

            Button(
                onClick = {
                    if (selectedMonth < YearMonth.now()) {
                        selectedMonth = selectedMonth.plusMonths(1)
                    }
                }
            ) {
                Text(">")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Expense Tracker",
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total Credited: ₹$totalCredit",
                    fontSize = 20.sp,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total Debited: ₹$totalDebit",
                    fontSize = 20.sp,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Net Expenditure: ₹$netBalance",
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {

            CircularProgressIndicator()

        }

        else if (filteredTransactions.isEmpty()) {

            Text(
                text = "No transactions found for ${selectedMonth.month} ${selectedMonth.year}",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 20.dp)
            )

        } else {

            LazyColumn {
                items(filteredTransactions) { transaction ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(
                                    text = transaction.type,
                                    fontSize = 18.sp,
                                    color = if (transaction.type == "CREDIT")
                                        Color(0xFF2E7D32)
                                    else
                                        Color.Red
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = transaction.name,
                                    fontSize = 16.sp,
                                    fontWeight=FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = transaction.date,
                                    fontSize = 14.sp
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {

                                Text(
                                    text = "₹${transaction.amount}",
                                    fontSize = 20.sp,
                                    color = if (transaction.type == "CREDIT")
                                        Color(0xFF2E7D32)
                                    else
                                        Color.Red
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        scope.launch {

                                            val updatedTransaction = transaction.copy(
                                                excluded = true
                                            )

                                            repository.updateTransaction(updatedTransaction)

                                            transactions = repository.getTransactions()
                                        }
                                    }
                                ) {
                                    Text("Exclude")
                                }
                            }
                        }
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
