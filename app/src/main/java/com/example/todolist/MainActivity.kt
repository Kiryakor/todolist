package com.example.todolist

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.todolist.screen.MainScreen
import com.example.todolist.ui.theme.TODOListTheme
import com.example.todolist.view.Priority
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
    val priority: Priority,
    var initIsDone: Boolean = false
) {
    var isDone by mutableStateOf(initIsDone)

    fun convertTask(): SaveTask {
        return  SaveTask(title, id, isDone, priority)
    }
}

data class SaveTask(val title: String, val id: String, var isDone: Boolean, val priority: Priority) {
    fun convertTask(): Task {
        return Task(title, id, priority, isDone)
    }
}

enum class FilterType {
    date, priority
}
class MainViewModel(private val context: Context) : ViewModel() {
    private var _tasks = ArrayList<Task>().toMutableStateList()

    var filterType = mutableStateOf(FilterType.priority)

    init {
        load()
    }

    val tasks: List<Task>
        get() {
            when(filterType.value) {
                FilterType.date -> return _tasks
                FilterType.priority -> return _tasks.sortedBy { it.priority }
            }
        }

    fun filterText(): String {
        when(filterType.value) {
            FilterType.date -> return "по дате"
            FilterType.priority -> return "по приоритету"
        }
    }

    fun remove(item: Task) {
        _tasks.remove(item)
        saveTasks()
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
        val json = sharedPreferences.getString("tasks-key", null)
        if (json != null) {
            Log.d("test", json)
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
        editor.putString("tasks-key", json);
        editor.apply();
    }
}