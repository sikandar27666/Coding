// Taskmain.kt

package com.project.voicecontroller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.voicecontroller.databinding.ActivityTaskmainBinding

class Taskmain : AppCompatActivity(), TaskAdapter.OnTaskClickListener {

    private lateinit var binding: ActivityTaskmainBinding
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskmainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskRepository = TaskRepository(this)
        taskAdapter = TaskAdapter(getAllTasks(), this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = taskAdapter

        binding.addButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val description = binding.descriptionEditText.text.toString().trim()

            if (title.isNotEmpty()) {
                val newTask = Task(
                    id = System.currentTimeMillis(),
                    title = title,
                    description = description,
                    completed = false
                )

                taskRepository.addTask(newTask)
                taskAdapter.updateTasks(getAllTasks())

                binding.titleEditText.text.clear()
                binding.descriptionEditText.text.clear()
            } else {
                Log.d("TaskMain", "Title is empty.")
            }
        }

        binding.deleteButton.setOnClickListener {
            val selectedTaskPosition = taskAdapter.getSelectedTaskPosition()
            if (selectedTaskPosition != RecyclerView.NO_POSITION) {
                val selectedTask = taskAdapter.tasks[selectedTaskPosition]

                taskRepository.deleteTask(selectedTask.id)
                taskAdapter.updateTasks(getAllTasks())
                taskAdapter.resetSelectedTaskPosition()

                Log.d("TaskMain", "Task deleted: $selectedTask")
            }
        }

        binding.editButton.setOnClickListener {
            val selectedTaskPosition = taskAdapter.getSelectedTaskPosition()
            if (selectedTaskPosition != RecyclerView.NO_POSITION) {
                val selectedTask = taskAdapter.tasks[selectedTaskPosition]

                // Handle edit logic here
                Log.d("TaskMain", "Edit button clicked for task: $selectedTask")
            }
        }
    }

    private fun getAllTasks(): List<Task> {
        return taskRepository.getAllTasks()
    }

    override fun onTaskClick(task: Task) {
        Log.d("TaskMain", "Task clicked: $task")
    }

    override fun onTaskUpdate(task: Task) {
        taskRepository.updateTask(task)
        Log.d("TaskMain", "Task updated: $task")
    }

    fun onNextButtonClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
