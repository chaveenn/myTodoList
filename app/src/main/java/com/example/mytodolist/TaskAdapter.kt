package com.example.mytodolist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: MutableList<Task>, private val listener: OnTaskClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnTaskClickListener {
        fun onDeleteClick(position: Int)
        fun onUpdateClick(position: Int)
        fun onViewClick(position: Int)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        val taskStartDateTime: TextView = itemView.findViewById(R.id.taskStartDateTime)
        val taskEndDateTime: TextView = itemView.findViewById(R.id.taskEndDateTime)
        val taskViewed: CheckBox = itemView.findViewById(R.id.checkbox_viewed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name
        holder.taskDescription.text = task.description
        holder.taskStartDateTime.text = task.startDateTime
        holder.taskEndDateTime.text = task.endDateTime
        holder.taskViewed.isChecked = task.isViewed

        holder.taskViewed.setOnClickListener { listener.onViewClick(position) }
        holder.itemView.findViewById<View>(R.id.deleteButton).setOnClickListener {
            listener.onDeleteClick(position)
        }
        holder.itemView.findViewById<View>(R.id.updateButton).setOnClickListener {
            listener.onUpdateClick(position)
        }
    }

    override fun getItemCount(): Int = tasks.size
}

