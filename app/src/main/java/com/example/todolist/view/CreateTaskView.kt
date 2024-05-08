package com.example.todolist.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onSubmit: (text: String) -> Unit
) {
    val message = remember{ mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Отмена")
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit(message.value) }) {
                Text(text = "Сохранить")
            }
        },
        title = {
            Text(
                text = "Новая задача",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            TextField(
                value = message.value,
                onValueChange = { newText -> message.value = newText }
            )
        }
    )
}