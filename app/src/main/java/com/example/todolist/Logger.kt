package com.example.todolist
import androidx.activity.ComponentActivity
import com.my.tracker.MyTracker

final class Logger private constructor() {
    companion object {

        @Volatile
        private var instance: Logger? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Logger().also { instance = it }
            }
    }

    fun setUpLogger(activity: ComponentActivity) {
        MyTracker.initTracker("97533413380578450096", activity.getApplication())
        MyTracker.trackLaunchManually(activity)
    }

    fun logOpenApp() {
//        val eventParams = HashMap<String, String>()
//        eventParams["someParamKey1"] = "someParamValue1"
//        eventParams["someParamKey2"] = "someParamValue2"
        MyTracker.trackEvent("openApp")
        MyTracker.flush()
    }

    fun logAddTaskBtnTap() {
        MyTracker.trackEvent("addTaskBtnTap")
        MyTracker.flush()
    }

    fun logRemoveTask() {
        MyTracker.trackEvent("removeTask")
        MyTracker.flush()
    }

    fun logAddTask() {
        MyTracker.trackEvent("addTask")
        MyTracker.flush()
    }

    fun logDoneTask() {
        MyTracker.trackEvent("doneTask")
        MyTracker.flush()
    }
}