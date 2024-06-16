import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.todolist.Logger
import com.example.todolist.SaveTask
import com.example.todolist.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

enum class FilterType {
    date, priority, done
}
final class MainViewModel(private val context: Context) : ViewModel() {
    private var _tasks = ArrayList<Task>().toMutableStateList()

    var filterType = mutableStateOf(FilterType.priority)

    init {
        load()
    }

    val tasks: List<Task>
        get() {
            when(filterType.value) {
                FilterType.date -> return _tasks.sortedBy { it.date }
                FilterType.priority -> return _tasks.sortedBy { it.priority }
                FilterType.done -> return  _tasks.sortedBy { it.isDone }
            }
        }

    fun filterText(): String {
        when(filterType.value) {
            FilterType.date -> return "по дате создания"
            FilterType.priority -> return "по приоритету"
            FilterType.done -> return "открытые"
        }
    }

    fun tapOnFilter() {
        when(filterType.value) {
            FilterType.date -> filterType.value = FilterType.priority
            FilterType.priority -> filterType.value = FilterType.done
            FilterType.done -> filterType.value = FilterType.date
        }
        saveTasks()
    }

    fun remove(item: Task) {
        Logger.getInstance().logRemoveTask()
        _tasks.remove(item)
        saveTasks()
    }

    fun add(item: Task) {
        Logger.getInstance().logAddTask()
        _tasks.add(item)
        saveTasks()
    }

    fun update(task: Task, isDone: Boolean) {
        if (isDone) {
            Logger.getInstance().logDoneTask()
        }

        _tasks.find { it.id == task.id }?.let { task ->
            task.isDone = isDone
        }
        saveTasks()
    }

    private fun load() {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
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
            context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(tasks.map { it.convertTask() })
        editor.putString("tasks-key", json);
        editor.apply();
    }
}