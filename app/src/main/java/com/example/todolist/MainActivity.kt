package com.example.todolist

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.theme.TODOListTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID


class MainActivity : ComponentActivity() {

    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = MainViewModel(context = this)
        setContent {
            TODOListTheme {
                MainScreen(viewModel)
            }
        }
    }
}

data class Task(
    val title: String,
    val id: String = UUID.randomUUID().toString(),
    var initIsDone: Boolean = false
) {
    var isDone by mutableStateOf(initIsDone)

    fun convertTask(): SaveTask {
        return  SaveTask(title, id, isDone)
    }
}

data class SaveTask(val title: String, val id: String, var isDone: Boolean) {
    fun convertTask(): Task {
        return Task(title, id, isDone)
    }
}

class MainViewModel(private val context: Context) : ViewModel() {
    private var _tasks = ArrayList<Task>().toMutableStateList()

    init {
        load()
    }

    val tasks: List<Task>
        get() = _tasks

    fun remove(item: Task) {
        _tasks.remove(item)
    }

    fun add(item: Task) {
        _tasks.add(item)
        saveTasks()
    }

    fun update(task: Task, isDone: Boolean) {
        _tasks.find { it.id == task.id }?.let { task ->
            task.isDone = isDone
        }
        saveTasks()
    }

    private fun load() {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("tasks-key 5", null)
        if (json != null) {
            val type = object : TypeToken<List<SaveTask>>() {}.type
            val tasks = gson.fromJson<List<SaveTask>>(json, type).map { it.convertTask() }
            _tasks.addAll(tasks)
        }
    }
    private fun saveTasks() {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(tasks.map { it.convertTask() })
        Log.d("kkk 1", json)
        editor.putString("tasks-key 5", json);
        editor.apply();
    }
}

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
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = true)
        ) {
            viewModel.tasks.forEach { task ->
                TaskView(task, onCheckedChange = { task, isDone ->
                    viewModel.update(task, isDone)
                })
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

@Composable
fun TaskView(task: Task, onCheckedChange: ((Task, Boolean) -> Unit)) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = task.title.replaceFirstChar(Char::titlecase),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Checkbox(checked = task.isDone, onCheckedChange = { onCheckedChange(task, it) })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    TODOListTheme {
        MainScreen(MainViewModel(LocalContext.current))
    }
}

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