package com.example.mytodolist


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var taskStartDateTimeEditText: EditText
    private lateinit var taskEndDateTimeEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskNameEditText = findViewById(R.id.taskNameEditText)
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText)
        taskStartDateTimeEditText = findViewById(R.id.taskStartDateTimeEditText)
        taskEndDateTimeEditText = findViewById(R.id.taskEndDateTimeEditText)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val name = taskNameEditText.text.toString()
            val description = taskDescriptionEditText.text.toString()
            val startDateTime = parseDateTime(taskStartDateTimeEditText.text.toString())
            val endDateTime = parseDateTime(taskEndDateTimeEditText.text.toString())

            val resultIntent = Intent()
            resultIntent.putExtra("task_name", name)
            resultIntent.putExtra("task_description", description)
            resultIntent.putExtra("task_start_datetime", startDateTime)
            resultIntent.putExtra("task_end_datetime", endDateTime)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun parseDateTime(dateTime: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.parse(dateTime)?.time ?: 0
    }
}
