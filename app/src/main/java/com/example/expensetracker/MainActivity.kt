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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material3.Icon

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

    var searchQuery by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var selectedMonth by remember {
        mutableStateOf(YearMonth.now())
    }

    var showExcluded by remember {
        mutableStateOf(false)
    }
    var selectedTransaction by remember {
        mutableStateOf<Transaction?>(null)
    }

    val filteredTransactions = transactions.filter {

        val transactionMonth = Instant
            .ofEpochMilli(it.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .let { date ->
                YearMonth.of(date.year, date.month)
            }

        val matchesMonth = transactionMonth == selectedMonth

        val matchesExcluded =
            if (showExcluded)
                it.excluded
            else
                !it.excluded

        val matchesSearch =
            searchQuery.isBlank() ||

                    it.name.contains(searchQuery, ignoreCase = true) ||

                    it.type.contains(searchQuery, ignoreCase = true) ||

                    it.amount.toString().contains(searchQuery) ||

                    it.date.contains(searchQuery, ignoreCase = true)

        matchesMonth && matchesExcluded && matchesSearch
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

    suspend fun syncTransactions() {

        TransactionSyncManager.sync(context)

        transactions = repository.getTransactions()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

            scope.launch {

                syncTransactions()

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

            syncTransactions()

            isLoading = false

        } else {

            permissionLauncher.launch(Manifest.permission.READ_SMS)

        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
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
                Text(
                    "<",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${selectedMonth.month} ${selectedMonth.year}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    if (selectedMonth < YearMonth.now()) {
                        selectedMonth = selectedMonth.plusMonths(1)
                    }
                }
            ) {
                Text(
                    ">",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        syncTransactions()
                    }
                },
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 4.dp
                )
            ) {
                Text(
                    "↻",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Search transactions...")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchQuery = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Track Mint",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Received",
                        fontSize = 18.sp
                    )

                    Text(
                        text = "₹${String.format("%,d", totalCredit)}",
                        fontSize = 18.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Spent",
                        fontSize = 18.sp
                    )

                    Text(
                        text = "₹${String.format("%,d", totalDebit)}",
                        fontSize = 18.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 18.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Net Expenditure",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "₹${String.format("%,d", netBalance)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Show Excluded",
                fontSize = 18.sp
            )

            Switch(
                checked = showExcluded,
                onCheckedChange = {
                    showExcluded = it
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {

            CircularProgressIndicator()

        } else if (filteredTransactions.isEmpty()) {

            Text(
                text = if (showExcluded)
                    "No excluded transactions"
                else
                    "No transactions found for ${selectedMonth.month} ${selectedMonth.year}",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 20.dp)
            )

        } else {

            LazyColumn {
                items(filteredTransactions) { transaction ->

                    Card(
                        onClick = {
                            selectedTransaction = transaction
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
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

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = transaction.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    if (transaction.notes.isNotBlank()) {

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Icon(
                                            imageVector = Icons.Default.StickyNote2,
                                            contentDescription = "Has Note",
                                            tint = Color(0xFFFFC107),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

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
                                    text = if (transaction.type == "CREDIT")
                                        "+ ₹${String.format("%,d", transaction.amount)}"
                                    else
                                        "- ₹${String.format("%,d", transaction.amount)}",
                                    fontSize = 20.sp,
                                    color = if (transaction.type == "CREDIT")
                                        Color(0xFF2E7D32)
                                    else
                                        Color.Red,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        scope.launch {

                                            val updatedTransaction = transaction.copy(
                                                excluded = !transaction.excluded
                                            )

                                            repository.updateTransaction(updatedTransaction)

                                            transactions = repository.getTransactions()
                                        }
                                    }
                                ) {
                                    Text(
                                        if (transaction.excluded)
                                            "Include"
                                        else
                                            "Exclude"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (selectedTransaction != null) {

        TransactionDetailsDialog(
            transaction = selectedTransaction!!,
            repository = repository,
            onDismiss = {

                scope.launch {
                    transactions = repository.getTransactions()
                }

                selectedTransaction = null
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseTrackerTheme {
        Greeting()
    }
}
