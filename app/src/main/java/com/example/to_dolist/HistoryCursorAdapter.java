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
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.to_dolist.data.ToDoListContract;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class HistoryCursorAdapter extends CursorAdapter {
    public HistoryCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView taskTextView = view.findViewById(R.id.historyTaskTextView);
        String task = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK));
        taskTextView.setText(task);

        TextView doneDateTextView = view.findViewById(R.id.doneDateTextView);
        String doneDate = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_TASK_DONE_DATE));
        try {
            doneDateTextView.setText(changeFormat(doneDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Context ctx = context;
        final int id = cursor.getInt(cursor.getColumnIndex(ToDoListContract.TaskEntry._ID));


        view.setOnTouchListener(new OnSwipeTouchListener(ctx) {
            public void onClick() {
                Toast.makeText(ctx, "clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeLeft() {
                Toast.makeText(ctx, "left", Toast.LENGTH_SHORT).show();
                showDeleteItemDialog();
            }


            private void showDeleteItemDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage("Do you want delete the task?");
                builder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }

            private void deleteItem() {
                int rowsDeleted =
                        ctx.getContentResolver().delete(
                                Uri.withAppendedPath(ToDoListContract.TaskEntry.CONTENT_URI, Integer.toString(id)),
                                null, null);

                if (rowsDeleted == 0) {
                    Toast.makeText(ctx,
                            "Deleting of list from the table failed",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctx,
                            "Task is deleted",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public String changeFormat(String date) throws ParseException {
        if (date.equals("")) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        Date convertedDate = dateFormat.parse(date);

        SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdfnewformat.format(convertedDate);

    }
}