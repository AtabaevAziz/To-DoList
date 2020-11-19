package com.example.to_dolist;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.to_dolist.data.ToDoListContract;

import java.util.Date;

public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView taskTextView = view.findViewById(R.id.taskTextView);
        String task = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK));
        taskTextView.setText(task);

        CheckBox statusCheckBox = view.findViewById(R.id.statusCheckBox);
        String status = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_STATUS));
        if (status.equals("1")) {
          statusCheckBox.setChecked(true);
        } else {
          statusCheckBox.setChecked(false);
        }

        TextView deadlineTextView = view.findViewById(R.id.deadlineTextView);
        String deadline = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_DEADLINE));
        deadlineTextView.setText(deadline);

        final Context ctx = context;
        final int id = cursor.getInt(cursor.getColumnIndex(ToDoListContract.TaskEntry._ID));

        statusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String selection = ToDoListContract.TaskEntry._ID + "=?";
                String itemIDArgs = Integer.toString(id);
                String[] selectionArgs = {itemIDArgs};

                ContentValues contentValues = new ContentValues();
                contentValues.put(ToDoListContract.TaskEntry.COLUMN_STATUS, isChecked);

                if (isChecked) {
                    contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_DONE_DATE, new Date().toString());

                    Toast.makeText(ctx,
                            "CHECKED",
                            Toast.LENGTH_LONG).show();
                } else {
                    contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_DONE_DATE, "");
                    Toast.makeText(ctx,
                            "UNCHECKED",
                            Toast.LENGTH_LONG).show();
                }

                ctx.getContentResolver().update(
                        Uri.withAppendedPath(ToDoListContract.TaskEntry.CONTENT_URI, Integer.toString(id)),
                        contentValues,
                        selection, selectionArgs);
                return;

            }
        });


    }

}
