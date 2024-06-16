package com.example.todolist

import MainViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.todolist.screen.MainScreen
import com.example.todolist.ui.theme.TODOListTheme
import com.example.todolist.view.Priority
import java.util.UUID


class MainActivity : ComponentActivity() {

    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.getInstance().setUpLogger(this)
        Logger.getInstance().logOpenApp()

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
    var initIsDone: Boolean = false,
    val date: Long = System.currentTimeMillis()
) {
    var isDone by mutableStateOf(initIsDone)

    fun convertTask(): SaveTask {
        return  SaveTask(title, id, isDone, priority, date)
    }
}

data class SaveTask(
    val title: String,
    val id: String,
    var isDone: Boolean,
    val priority: Priority,
    val date: Long
) {
    fun convertTask(): Task {
        return Task(title, id, priority, isDone, date)
    }
}