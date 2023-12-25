// TaskAdapter.kt

package com.project.voicecontroller

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.voicecontroller.databinding.TaskItemBinding

class TaskAdapter(var tasks: List<Task>, private val listener: OnTaskClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var selectedTaskPosition: Int = RecyclerView.NO_POSITION

    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val titleTextView: TextView = binding.titleTextView
        private val descriptionTextView: TextView = binding.descriptionTextView
        private val completedCheckBox: CheckBox = binding.completedCheckBox

        fun bind(task: Task, position: Int) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            completedCheckBox.isChecked = task.completed

            itemView.setOnClickListener {
                selectedTaskPosition = adapterPosition
                listener.onTaskClick(task)
            }

            completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                task.completed = isChecked
                listener.onTaskUpdate(task)
            }
        }
    }

    interface OnTaskClickListener {
        fun onTaskClick(task: Task)
        fun onTaskUpdate(task: Task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, position)
    }

    override fun getItemCount(): Int = tasks.size

    fun getSelectedTaskPosition(): Int {
        return selectedTaskPosition
    }

    fun resetSelectedTaskPosition() {
        selectedTaskPosition = RecyclerView.NO_POSITION
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
