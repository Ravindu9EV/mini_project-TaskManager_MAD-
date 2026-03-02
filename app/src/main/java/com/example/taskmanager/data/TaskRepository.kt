package com.example.taskmanager.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun getTask(taskId: Long): Task? = taskDao.getTaskById(taskId)

    // These method names match what your ViewModel is calling
    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun deleteAllTasks() {
        // You'll need to add this method to your Dao if you want it
         taskDao.deleteAllTasks()
    }

    suspend fun toggleTaskCompletion(taskId: Long, isCompleted: Boolean) {
        taskDao.updateCompletionStatus(taskId, isCompleted)
    }
}