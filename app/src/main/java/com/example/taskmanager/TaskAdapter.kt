package com.example.taskmanager

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.data.Task

class TaskAdapter(
    private val onItemClick: (Task) -> Unit,
    private val onItemDelete: (Task) -> Unit,
    private val onItemCheck: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val description: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val cbComplete: CheckBox = itemView.findViewById(R.id.cbComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description

        holder.cbComplete.setOnCheckedChangeListener(null)
        holder.cbComplete.isChecked = currentItem.isCompleted

        // Visual Polish: Add a strikethrough effect if the task is completed
        if (currentItem.isCompleted) {
            holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.description.paintFlags = holder.description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.description.paintFlags = holder.description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Set click listeners
        holder.btnEdit.setOnClickListener { onItemClick(currentItem) }
        holder.btnDelete.setOnClickListener { onItemDelete(currentItem) }

        // Item click for editing (optional)
        holder.itemView.setOnClickListener { onItemClick(currentItem) }

        // Trigger the database update when checked/unchecked
        holder.cbComplete.setOnCheckedChangeListener { _, isChecked ->
            onItemCheck(currentItem, isChecked)
        }
    }

    // Helper method to update the list
    fun updateTasks(newTasks: List<Task>) {
        submitList(newTasks.toList())
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}