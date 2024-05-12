package com.example.todolist.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.Task
import com.example.todolist.ui.theme.TODOListTheme
import java.util.UUID

@Composable
fun TaskView(
    task: Task,
    onCheckedChange: ((Task, Boolean) -> Unit),
    onDelete: ((Task) -> Unit)
) {
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
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Checkbox(checked = task.isDone, onCheckedChange = { onCheckedChange(task, it) })
        IconButton(onClick = { onDelete(task) }) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Удалить задачу"
            )
        }
    }
}