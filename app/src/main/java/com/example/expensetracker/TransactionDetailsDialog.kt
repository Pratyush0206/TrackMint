package com.example.expensetracker

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun TransactionDetailsDialog(
    transaction: Transaction,
    repository: TransactionRepository,
    onDismiss: () -> Unit
) {

    val scope = rememberCoroutineScope()

    var note by remember {
        mutableStateOf(transaction.notes)
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Transaction Details")
        },

        text = {

            Column {

                Text("Amount : ₹${transaction.amount}")
                Text("Type : ${transaction.type}")
                Text("Name : ${transaction.name}")
                Text("Date : ${transaction.date}")
                Text(
                    "Excluded: ${
                        if (transaction.excluded)
                            "Yes"
                        else
                            "No"
                    }"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Notes")

                OutlinedTextField(
                    value = note,
                    onValueChange = {
                        note = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    scope.launch {

                        repository.updateTransaction(
                            transaction.copy(
                                notes = note
                            )
                        )

                        onDismiss()
                    }
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {

            Button(
                onClick = onDismiss
            ) {
                Text("Close")
            }
        }
    )
}