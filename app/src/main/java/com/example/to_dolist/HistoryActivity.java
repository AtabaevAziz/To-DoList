package com.example.to_dolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.to_dolist.data.ToDoListContract;

import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int HISTORY_TASK_LOADER = 111;
    HistoryCursorAdapter historyCursorAdapter;
    ListView historyListView;

    private Uri currentListUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        historyListView = findViewById(R.id.historyListView);

        historyCursorAdapter = new HistoryCursorAdapter(this,
                null, false);
        historyListView.setAdapter(historyCursorAdapter);

        Intent intent = getIntent();

        currentListUri = intent.getData();

        if (currentListUri == null) {
            setTitle("History");
            invalidateOptionsMenu();
        }

        getSupportLoaderManager().initLoader(HISTORY_TASK_LOADER,
                null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                ToDoListContract.TaskEntry._ID,
                ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK,
                ToDoListContract.TaskEntry.COLUMN_STATUS,
                ToDoListContract.TaskEntry.COLUMN_DEADLINE,
                ToDoListContract.TaskEntry.COLUMN_TASK_DONE_DATE,

        };
        CursorLoader cursorLoader = new CursorLoader(this,
                ToDoListContract.TaskEntry.CONTENT_URI,
                projection,
                "editStatus=?",
                new String[]{"1"},
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
      historyCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        historyCursorAdapter.swapCursor(null);
    }

}