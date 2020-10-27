package com.example.to_dolist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.to_dolist.data.ToDoListContract;

public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView taskTextView = view.findViewById(R.id.taskTextView);
        String task = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK));
        taskTextView.setText(task);

        TextView statusTextView = view.findViewById(R.id.statusTextView);
        String status = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_STATUS));
        statusTextView.setText(status);

        TextView deadlineTextView = view.findViewById(R.id.deadlineTextView);
        String deadline = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_DEADLINE));
        deadlineTextView.setText(deadline);



    }
}
