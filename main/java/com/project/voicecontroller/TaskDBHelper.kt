package com.project.voicecontroller
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_TASKS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "task_db"
        private const val DATABASE_VERSION = 1

        // Task table
        const val TABLE_TASKS = "tasks"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_COMPLETED = "completed"

        // SQL statement to create the tasks table
        private const val CREATE_TABLE_TASKS = "CREATE TABLE $TABLE_TASKS(" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_COMPLETED INTEGER DEFAULT 0" +
                ")"
    }
}
