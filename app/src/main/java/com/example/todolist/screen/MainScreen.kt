package com.example.todolist.screen

import MainViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
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
                text = "ToDo List",
                color = Color.Black,
                style = TextStyle(fontSize = 25.sp),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Box(Modifier.fillMaxSize()) {
            if (viewModel.tasks.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
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
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(all = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            Text(
                                text = "Список задач",
                                style = TextStyle(fontSize = 20.sp)
                            )
                            Spacer(Modifier.weight(1f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Image(
                                    painterResource(R.drawable.ic_filter),
                                    contentDescription = "",
                                    colorFilter = ColorFilter.tint(Color.Black),
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .size(20.dp),
                                )
                                Text(
                                    text = viewModel.filterText(),
                                    style = TextStyle(fontSize = 16.sp),
                                    modifier = Modifier.clickable {
                                        viewModel.tapOnFilter()
                                    }
                                )
                            }
                        }
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
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                onClick = {
                    showDialog.value = true
                }
            ) {
                Text("+")
            }
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
                viewModel.add(
                    Task(
                        text,
                        UUID.randomUUID().toString(),
                        priority,
                        false,
                        System.currentTimeMillis()
                    )
                )
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
