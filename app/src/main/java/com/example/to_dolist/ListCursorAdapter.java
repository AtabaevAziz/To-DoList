package com.example.to_dolist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.to_dolist.data.ToDoListContract;

public class ListCursorAdapter extends CursorAdapter {
    public ListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView describeTheListTextView = view.findViewById(R.id.toDoListTextView);

        String describeTheList = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.ListEntry.COLUMN_DESCRIBE_THE_LIST));


        describeTheListTextView.setText(describeTheList);

    }
}
