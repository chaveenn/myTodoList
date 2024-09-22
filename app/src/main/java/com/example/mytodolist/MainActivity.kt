package com.example.mytodolist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var tasks: MutableList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasks = loadTasks()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(tasks, this)
        recyclerView.adapter = taskAdapter

        val addButton: FloatingActionButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            addTask()
        }
    }

    private fun addTask() {
        // Create a new task here
        val task = Task("New Task", "Description", "Start Date", "End Date", false)
        tasks.add(task)
        saveTasks()
        taskAdapter.notifyItemInserted(tasks.size - 1)
        setAlarm(task)
    }

    private fun saveTasks() {
        val sharedPref = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putStringSet("tasks", tasks.map { "${it.name}|${it.description}|${it.startDateTime}|${it.endDateTime}|${it.isViewed}" }.toSet())
        editor.apply()
    }

    private fun loadTasks(): MutableList<Task> {
        val sharedPref = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
        val taskStrings = sharedPref.getStringSet("tasks", setOf()) ?: return mutableListOf()
        return taskStrings.map {
            val parts = it.split("|")
            Task(parts[0], parts[1], parts[2], parts[3], parts[4].toBoolean())
        }.toMutableList()
    }

    private fun setAlarm(task: Task) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("taskName", task.name)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent) // Example
    }

    override fun onDeleteClick(position: Int) {
        tasks.removeAt(position)
        saveTasks()
        taskAdapter.notifyItemRemoved(position)
    }

    override fun onUpdateClick(position: Int) {
        tasks[position].name = "Updated Task"
        saveTasks()
        taskAdapter.notifyItemChanged(position)
    }

    override fun onViewClick(position: Int) {
        tasks[position].isViewed = !tasks[position].isViewed
        saveTasks()
        taskAdapter.notifyItemChanged(position)
    }
}
