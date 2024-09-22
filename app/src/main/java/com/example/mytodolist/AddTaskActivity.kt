package com.example.mytodolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var taskStartDateTimeEditText: EditText
    private lateinit var taskEndDateTimeEditText: EditText
    private lateinit var saveButton: Button
    private var isEditMode = false
    private var taskPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskNameEditText = findViewById(R.id.taskNameEditText)
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText)
        taskStartDateTimeEditText = findViewById(R.id.taskStartDateTimeEditText)
        taskEndDateTimeEditText = findViewById(R.id.taskEndDateTimeEditText)
        saveButton = findViewById(R.id.saveButton)

        // Check if it's in edit mode
        if (intent != null && intent.hasExtra("task_name")) {
            isEditMode = true
            taskNameEditText.setText(intent.getStringExtra("task_name"))
            taskDescriptionEditText.setText(intent.getStringExtra("task_description"))
            taskStartDateTimeEditText.setText(formatDateTime(intent.getLongExtra("task_start_datetime", 0)))
            taskEndDateTimeEditText.setText(formatDateTime(intent.getLongExtra("task_end_datetime", 0)))
            taskPosition = intent.getIntExtra("task_position", -1)
        }

        saveButton.setOnClickListener {
            val name = taskNameEditText.text.toString()
            val description = taskDescriptionEditText.text.toString()
            val startDateTime = parseDateTime(taskStartDateTimeEditText.text.toString())
            val endDateTime = parseDateTime(taskEndDateTimeEditText.text.toString())

            val resultIntent = Intent().apply {
                putExtra("task_name", name)
                putExtra("task_description", description)
                putExtra("task_start_datetime", startDateTime)
                putExtra("task_end_datetime", endDateTime)
            }

            if (isEditMode) {
                resultIntent.putExtra("task_position", taskPosition)
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun parseDateTime(dateTime: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.parse(dateTime)?.time ?: 0
    }

    private fun formatDateTime(timestamp: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.format(Date(timestamp))
    }
}
