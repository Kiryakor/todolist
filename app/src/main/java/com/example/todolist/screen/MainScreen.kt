package com.example.todolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.MainViewModel
import com.example.todolist.Task
import com.example.todolist.ui.theme.TODOListTheme
import com.example.todolist.view.CreateTaskDialog
import com.example.todolist.view.TaskView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("EEE, d MMM yyyy")
    val current = formatter.format(time)

    Column(
        modifier = Modifier.background(Color(0xFFEDEDED))
    ) {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.systemBars
            )
        )
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Text(
                text = current.replaceFirstChar(Char::titlecase),
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 6.dp)
            )
            Text(
                text = "Трекер задач",
                color = Color.Black,
                style = TextStyle(fontSize = 25.sp),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        if (viewModel.tasks.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = true)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 32.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                ) {
                    Text(
                        text = "У вас еще нет добавленных задач",
                        style = TextStyle(fontSize = 16.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(all = 32.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(weight = 1f, fill = true)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(all = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(vertical = 8.dp)
                ) {
                    viewModel.tasks.forEachIndexed { index, task ->
                        TaskView(
                            task,
                            onCheckedChange = { task, isDone ->
                                viewModel.update(task, isDone)
                            },
                            onDelete = { task ->
                                viewModel.remove(task)
                            }
                        )
                        if (index + 1 < viewModel.tasks.count()) {
                            Divider(thickness = 1.dp, color = Color(0xFFEDEDED))
                        }
                    }
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
            onSubmit = { text, priority ->
                viewModel.add(Task(text, UUID.randomUUID().toString(), priority))
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
