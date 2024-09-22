package com.example.mytodolist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyToDoList", MODE_PRIVATE)
        gson = Gson()

        loadTasksFromPreferences()

        setupRecyclerView()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(tasks, this::onTaskDeleted, this::onTaskUpdated)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter
    }

    private fun setupAddButton() {
        val addButton: FloatingActionButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
    }

    private fun onTaskDeleted(position: Int) {
        tasks.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        saveTasksToPreferences()
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
    }

    // Update this function to accept Task and position
    private fun onTaskUpdated(task: Task, position: Int) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra("task_name", task.name)
        intent.putExtra("task_description", task.description)
        intent.putExtra("task_start_datetime", task.startDateTime)
        intent.putExtra("task_end_datetime", task.endDateTime)
        intent.putExtra("task_position", position) // Pass position for updating
        startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val name = data.getStringExtra("task_name") ?: return
            val description = data.getStringExtra("task_description") ?: return
            val startDateTime = data.getLongExtra("task_start_datetime", 0)
            val endDateTime = data.getLongExtra("task_end_datetime", 0)

            if (requestCode == ADD_TASK_REQUEST_CODE) {
                // Add new task
                val newTask = Task(name, description, startDateTime, endDateTime)
                tasks.add(newTask)
                taskAdapter.notifyItemInserted(tasks.size - 1)
            } else if (requestCode == EDIT_TASK_REQUEST_CODE) {
                // Edit existing task
                val position = data.getIntExtra("task_position", -1)
                if (position != -1) {
                    tasks[position] = Task(name, description, startDateTime, endDateTime)
                    taskAdapter.notifyItemChanged(position)
                }
            }

            saveTasksToPreferences()
        }
    }

    private fun loadTasksFromPreferences() {
        val json = sharedPreferences.getString("tasks", null)
        if (json != null) {
            val type = object : TypeToken<List<Task>>() {}.type
            val loadedTasks: List<Task> = gson.fromJson(json, type)
            tasks.addAll(loadedTasks)
        }
    }

    private fun saveTasksToPreferences() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(tasks)
        editor.putString("tasks", json)
        editor.apply()
    }

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1
        private const val EDIT_TASK_REQUEST_CODE = 2
    }
}
