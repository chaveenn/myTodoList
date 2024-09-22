package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.AddTaskActivity
import com.example.mytodolist.R
import com.example.mytodolist.Task
import com.example.mytodolist.TaskAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
    }

    private fun onTaskUpdated(updatedTask: Task, position: Int) {
        tasks[position] = updatedTask
        taskAdapter.notifyItemChanged(position)
        Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("task_name") ?: return
            val description = data?.getStringExtra("task_description") ?: return
            val startDateTime = data?.getLongExtra("task_start_datetime", 0) ?: return
            val endDateTime = data?.getLongExtra("task_end_datetime", 0) ?: return

            val newTask = Task(name, description, startDateTime, endDateTime)
            tasks.add(newTask)
            taskAdapter.notifyItemInserted(tasks.size - 1)
        }
    }

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1
    }
}
