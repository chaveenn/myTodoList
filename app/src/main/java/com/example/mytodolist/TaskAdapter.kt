package com.example.mytodolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(
    private val tasks: List<Task>,
    private val onTaskDeleted: (Int) -> Unit,
    private val onTaskUpdated: (Task, Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        val taskStartDateTime: TextView = itemView.findViewById(R.id.taskStartDateTime)
        val taskEndDateTime: TextView = itemView.findViewById(R.id.taskEndDateTime)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val checkboxViewed: CheckBox = itemView.findViewById(R.id.checkbox_viewed)

        init {
            deleteButton.setOnClickListener {
                onTaskDeleted(adapterPosition)
            }
            updateButton.setOnClickListener {
                val task = tasks[adapterPosition]
                // Logic for updating the task
                val updatedTask = task.copy(name = "${task.name} (Updated)")
                onTaskUpdated(updatedTask, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name
        holder.taskDescription.text = task.description
        holder.taskStartDateTime.text = formatDate(task.startDateTime)
        holder.taskEndDateTime.text = formatDate(task.endDateTime)
        holder.checkboxViewed.isChecked = false // Initialize checkbox state
    }

    override fun getItemCount() = tasks.size

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
}
