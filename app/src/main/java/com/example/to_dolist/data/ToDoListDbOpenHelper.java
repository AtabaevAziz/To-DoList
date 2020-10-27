package com.example.to_dolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoListDbOpenHelper extends SQLiteOpenHelper {
    public ToDoListDbOpenHelper(Context context) {
        super(context, ToDoListContract.DATABASE_NAME,
                null, ToDoListContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + ToDoListContract.TaskEntry.TABLE_NAME + "("
                + ToDoListContract.TaskEntry._ID + " INTEGER PRIMARY KEY,"
                + ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK + " TEXT,"
                + ToDoListContract.TaskEntry.COLUMN_STATUS + " TEXT,"
                + ToDoListContract.TaskEntry.COLUMN_DEADLINE + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ToDoListContract.DATABASE_NAME);
        onCreate(db);
    }
}