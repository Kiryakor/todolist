package com.example.todolist.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.CreateTaskDialog
import com.example.todolist.MainViewModel
import com.example.todolist.Task
import com.example.todolist.TaskView
import com.example.todolist.ui.theme.TODOListTheme

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    Column {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.systemBars
            )
        )
        Text(
            text = "Трекер задач",
            style = TextStyle(fontSize=25.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        if (viewModel.tasks.isEmpty()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = true)
            ) {
                Spacer(modifier = Modifier.weight(weight = 1f, fill = true))
                Text(
                    text = "У вас еще нет добавленных задач",
                    style = TextStyle(fontSize = 25.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                Spacer(modifier = Modifier.weight(weight = 1f, fill = true))
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = true)
            ) {
                viewModel.tasks.forEach { task ->
                    TaskView(
                        task,
                        onCheckedChange = { task, isDone ->
                            viewModel.update(task, isDone)
                        },
                        onDelete = { task ->
                            viewModel.remove(task)
                        }
                    )
                }
            }
        }
        Button(
            onClick = {
                showDialog.value = true
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Добавить задачу")
        }
        Spacer(
            Modifier.windowInsetsBottomHeight(
                WindowInsets.systemBars
            )
        )
    }
    if (showDialog.value) {
        CreateTaskDialog(
            onDismiss = {
                showDialog.value = false
            },
            onSubmit = {
                viewModel.add(Task(it))
                showDialog.value = false
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    TODOListTheme {
        MainScreen(MainViewModel(LocalContext.current))
    }
}
