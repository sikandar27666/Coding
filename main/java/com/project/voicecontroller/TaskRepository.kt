// TaskRepository.kt

package com.project.voicecontroller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.project.voicecontroller.TaskDBHelper.Companion.COLUMN_COMPLETED
import com.project.voicecontroller.TaskDBHelper.Companion.COLUMN_DESCRIPTION
import com.project.voicecontroller.TaskDBHelper.Companion.COLUMN_ID
import com.project.voicecontroller.TaskDBHelper.Companion.COLUMN_TITLE
import com.project.voicecontroller.TaskDBHelper.Companion.TABLE_TASKS

class TaskRepository(context: Context) {

    private val database: SQLiteDatabase = TaskDBHelper(context).writableDatabase

    fun addTask(task: Task): Long {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }

        return database.insert(TABLE_TASKS, null, values)
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val cursor: Cursor? = database.query(TABLE_TASKS, null, null, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val task = Task(
                        it.getLong(it.getColumnIndex(COLUMN_ID)),
                        it.getString(it.getColumnIndex(COLUMN_TITLE)),
                        it.getString(it.getColumnIndex(COLUMN_DESCRIPTION)),
                        it.getInt(it.getColumnIndex(COLUMN_COMPLETED)) == 1
                    )
                    tasks.add(task)
                } while (it.moveToNext())
            }
        }

        return tasks
    }

    fun updateTask(task: Task) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }

        database.update(TABLE_TASKS, values, "$COLUMN_ID=?", arrayOf(task.id.toString()))
    }

    fun deleteTask(taskId: Long) {
        database.delete(TABLE_TASKS, "$COLUMN_ID=?", arrayOf(taskId.toString()))
    }
}
