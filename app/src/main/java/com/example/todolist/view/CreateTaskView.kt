package com.example.todolist.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.TODOListTheme

enum class Priority {
    first, second, third, fourth
}

fun colorFor(priority: Priority): Color {
    when(priority) {
        Priority.first -> return Color(0xFFBA68C8)
        Priority.second -> return Color.Red
        Priority.third -> return Color.Blue
        Priority.fourth -> return Color.Yellow
    }
}

@Composable
fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onSubmit: (text: String, priority: Priority) -> Unit
) {
    val message = remember { mutableStateOf("") }
    var priority = remember { mutableStateOf(Priority.first) }
    var isError = rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Отмена")
            }
        },
        confirmButton = {
            Button(onClick = {
                if (message.value.isNotEmpty()) {
                    onSubmit(message.value, priority.value)
                } else {
                    isError.value = true
                }
            }) {
                Text(text = "Сохранить")
            }
        },
        title = {
            Text(
                text = "Задача",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                TextField(
                    value = message.value,
                    placeholder = { Text(text = "Название задачи") },
                    supportingText = {
                        if (isError.value) {
                            Text(
                                text = "Текст не может быть пустым",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    isError = isError.value,
                    onValueChange = { newText ->
                        message.value = newText
                        if (isError.value && newText.isNotEmpty()) {
                            isError.value = false
                        }
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Приоритет:"
                    )
                    SelectColor(
                        colorFor(Priority.first),
                        priority.value == Priority.first,
                        onTap = {
                            priority.value = Priority.first
                        }
                    )
                    SelectColor(
                        colorFor(Priority.second),
                        priority.value == Priority.second,
                        onTap = {
                            priority.value = Priority.second
                        }
                    )
                    SelectColor(
                        colorFor(Priority.third),
                        priority.value == Priority.third,
                        onTap = {
                            priority.value = Priority.third
                        }
                    )
                    SelectColor(
                        colorFor(Priority.fourth),
                        priority.value == Priority.fourth,
                        onTap = {
                            priority.value = Priority.fourth
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun SelectColor(
    color: Color,
    isSelect: Boolean,
    onTap: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Canvas(
            modifier = Modifier
                .size(24.dp)
                .clickable { onTap() },
            onDraw = {
                drawCircle(color)
            },
        )
        if (isSelect) {
            Image(
                painter = painterResource(id = android.R.drawable.checkbox_on_background),
                contentDescription = ""
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateTaskDialogPreview() {
    TODOListTheme {
        CreateTaskDialog(onDismiss = {}, onSubmit = { text, priority -> })
    }
}