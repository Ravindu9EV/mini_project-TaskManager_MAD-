package com.example.taskmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.databinding.ActivityAddEditTaskBinding
import com.example.taskmanager.ui.TaskViewModel
import com.example.taskmanager.utils.ValidationUtils

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        loadIntentData()
        setupClickListeners()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        val database = TaskDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())
        val factory = TaskViewModel.Factory(repository)
        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
    }

    private fun loadIntentData() {
        taskId = intent.getLongExtra("TASK_ID", 0)
        if (taskId != 0L) {
            title = "Edit Task"
            binding.editTextTitle.setText(intent.getStringExtra("TASK_TITLE"))
            binding.editTextDescription.setText(intent.getStringExtra("TASK_DESCRIPTION"))
        } else {
            title = "Add Task"
        }
    }

    private fun setupClickListeners() {
        binding.buttonSave.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = binding.editTextTitle.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()

        val validationResult = ValidationUtils.validateTaskInput(title, description)

        if (!validationResult.isValid) {
            Toast.makeText(this, validationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return
        }

        val currentTime = System.currentTimeMillis()
        val task = Task(
            id = taskId,
            title = title,
            description = description,
            isCompleted = false,
            createdAt = if (taskId == 0L) currentTime else currentTime,
            updatedAt = currentTime
        )

        taskViewModel.saveTask(task)
        Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}