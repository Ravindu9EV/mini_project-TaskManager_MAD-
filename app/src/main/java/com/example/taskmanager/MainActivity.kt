package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.ui.TaskViewModel  // This should now resolve

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViewModel() {
        val database = TaskDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())
        val factory = TaskViewModel.Factory(repository)
        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onItemClick = { task -> openTaskDetail(task) },
            onItemDelete = { task -> deleteTask(task) },
            onItemCheck = { task, isChecked -> toggleTaskCompletion(task, isChecked) }
        )

        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.updateTasks(tasks)

            if (tasks.isEmpty()) {
                binding.textViewEmpty.visibility = android.view.View.VISIBLE
                binding.recyclerViewTasks.visibility = android.view.View.GONE
            } else {
                binding.textViewEmpty.visibility = android.view.View.GONE
                binding.recyclerViewTasks.visibility = android.view.View.VISIBLE
            }
        }

        taskViewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                taskViewModel.clearError()
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddTask.setOnClickListener {
            openTaskDetail(null)
        }
    }

    private fun openTaskDetail(task: Task?) {
        val intent = Intent(this, AddEditTaskActivity::class.java)
        task?.let {
            intent.putExtra("TASK_ID", it.id)
            intent.putExtra("TASK_TITLE", it.title)
            intent.putExtra("TASK_DESCRIPTION", it.description)
        }
        startActivity(intent)
    }

    private fun deleteTask(task: Task) {
        taskViewModel.deleteTask(task)
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
    }

    private fun toggleTaskCompletion(task: Task, isChecked: Boolean) {
        taskViewModel.toggleTaskCompletion(task.id, isChecked)
    }
}