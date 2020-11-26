package com.example.to_dolist.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.to_dolist.RecyclerHistoryEntity;

import java.util.ArrayList;

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
                + ToDoListContract.TaskEntry.COLUMN_DEADLINE + " TEXT,"
                + ToDoListContract.TaskEntry.COLUMN_TASK_DONE_DATE + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ToDoListContract.DATABASE_NAME);
        onCreate(db);
    }

    public ArrayList<RecyclerHistoryEntity> listHistory() {
        String sql = "select * from " + ToDoListContract.TaskEntry.TABLE_NAME + " where editStatus=1";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<RecyclerHistoryEntity> historyItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String task = cursor.getString(cursor.getColumnIndex(
                        ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK));
                String doneDate = cursor.getString(cursor.getColumnIndex(
                        ToDoListContract.TaskEntry.COLUMN_TASK_DONE_DATE));
                historyItems.add(new RecyclerHistoryEntity(id, task, doneDate));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return historyItems;
    }
}