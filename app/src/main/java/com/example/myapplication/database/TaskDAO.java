package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskDAO {
    private TaskDatabase dbHelper;
    private SQLiteDatabase db;

    public TaskDAO(Context context) {
        dbHelper = new TaskDatabase(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Add a new task
    public long addTask(Task task) {
        open();
        ContentValues values = new ContentValues();
        values.put(TaskDatabase.COLUMN_TITLE, task.getTitle());
        values.put(TaskDatabase.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskDatabase.COLUMN_PRIORITY, task.getPriority());
        values.put(TaskDatabase.COLUMN_DUE_DATE, task.getDueDate());
        values.put(TaskDatabase.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);

        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(new Date());
        values.put(TaskDatabase.COLUMN_CREATED_AT, currentTimestamp);

        long result = db.insert(TaskDatabase.TABLE_TASKS, null, values);
        close();
        return result;
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        open();
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = db.query(TaskDatabase.TABLE_TASKS, null, null, null,
                null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Task task = cursorToTask(cursor);
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        close();
        return tasks;
    }

    // Get task by ID
    public Task getTaskById(int id) {
        open();
        Cursor cursor = db.query(TaskDatabase.TABLE_TASKS, null,
                TaskDatabase.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Task task = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                task = cursorToTask(cursor);
            }
            cursor.close();
        }
        close();
        return task;
    }

    // Update task
    public int updateTask(Task task) {
        open();
        ContentValues values = new ContentValues();
        values.put(TaskDatabase.COLUMN_TITLE, task.getTitle());
        values.put(TaskDatabase.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskDatabase.COLUMN_PRIORITY, task.getPriority());
        values.put(TaskDatabase.COLUMN_DUE_DATE, task.getDueDate());
        values.put(TaskDatabase.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);

        int result = db.update(TaskDatabase.TABLE_TASKS, values,
                TaskDatabase.COLUMN_ID + "=?",
                new String[]{String.valueOf(task.getId())});
        close();
        return result;
    }

    // Delete task
    public int deleteTask(int id) {
        open();
        int result = db.delete(TaskDatabase.TABLE_TASKS,
                TaskDatabase.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        close();
        return result;
    }

    // Delete all tasks
    public int deleteAllTasks() {
        open();
        int result = db.delete(TaskDatabase.TABLE_TASKS, null, null);
        close();
        return result;
    }

    // Get task count
    public int getTaskCount() {
        open();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TaskDatabase.TABLE_TASKS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        close();
        return count;
    }

    // Convert cursor to Task object
    private Task cursorToTask(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabase.COLUMN_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabase.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabase.COLUMN_DESCRIPTION));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabase.COLUMN_PRIORITY));
        String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabase.COLUMN_DUE_DATE));
        int completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabase.COLUMN_COMPLETED));
        String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabase.COLUMN_CREATED_AT));

        return new Task(id, title, description, priority, dueDate, completed == 1, createdAt);
    }
}