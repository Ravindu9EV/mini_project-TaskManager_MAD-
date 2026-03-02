package com.example.taskmanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Convert Flow to LiveData for easier observation in Activities
    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()

    // For error handling
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun insert(task: Task) {
        viewModelScope.launch {
            try {
                repository.insertTask(task)
            } catch (e: Exception) {
                _error.value = "Error inserting task: ${e.message}"
            }
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
            } catch (e: Exception) {
                _error.value = "Error updating task: ${e.message}"
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
            } catch (e: Exception) {
                _error.value = "Error deleting task: ${e.message}"
            }
        }
    }

    // Method called from MainActivity
    fun deleteTask(task: Task) {
        delete(task)
    }

    fun deleteAll() {
        viewModelScope.launch {
            try {
                repository.deleteAllTasks()
            } catch (e: Exception) {
                _error.value = "Error deleting all tasks: ${e.message}"
            }
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            try {
                if (task.id == 0L) {
                    repository.insertTask(task)
                } else {
                    repository.updateTask(task)
                }
            } catch (e: Exception) {
                _error.value = "Error saving task: ${e.message}"
            }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            try {
                val updatedTask = task.copy(isCompleted = !task.isCompleted)
                repository.updateTask(updatedTask)
            } catch (e: Exception) {
                _error.value = "Error toggling task completion: ${e.message}"
            }
        }
    }

    // Method called from MainActivity for checkbox toggle
    fun toggleTaskCompletion(taskId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                repository.toggleTaskCompletion(taskId, isChecked)
            } catch (e: Exception) {
                _error.value = "Error updating task status: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    class Factory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}