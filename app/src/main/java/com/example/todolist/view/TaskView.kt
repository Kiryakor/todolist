package com.example.todolist.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.Task
import com.example.todolist.ui.theme.TODOListTheme
import java.util.UUID

@Composable
fun TaskView(
    task: Task,
    onCheckedChange: ((Task, Boolean) -> Unit),
    onDelete: ((Task) -> Unit)
) {
    val removeDialog = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Canvas(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(24.dp),
            onDraw = {
                drawCircle(colorFor(task.priority))
            },
        )
        Text(
            text = task.title.replaceFirstChar(Char::titlecase),
            maxLines = 2,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp).weight(3f),
        )
        Checkbox(
            checked = task.isDone,
            onCheckedChange = { onCheckedChange(task, it) },
            modifier = Modifier.size(24.dp)
        )
        IconButton(onClick = { removeDialog.value = true }) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Удалить задачу"
            )
        }
    }
    if (removeDialog.value) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text(text = "Удалить задачу") },
            text = { Text("Вы уверены, что хотите удалить задачу?") },
            confirmButton = {
                Button({
                    removeDialog.value = false
                    onDelete(task)
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                Button(
                    onClick = { removeDialog.value = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    TODOListTheme {
        TaskView(
            Task("dfsfdsfsdfds adsd das da das dsa d", UUID.randomUUID().toString(), priority = Priority.first, false, System.currentTimeMillis()),
            onCheckedChange = { _, _ -> },
            onDelete = { _ -> })
    }
}