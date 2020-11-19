package com.example.to_dolist;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.to_dolist.data.ToDoListContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER = 123;
    TaskCursorAdapter taskCursorAdapter;

    ListView dataListView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListView = findViewById(R.id.dataListView);

        FloatingActionButton floatingActionButton =
                findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        AddTaskActivity.class);
                startActivity(intent);

            }
        });



        taskCursorAdapter = new TaskCursorAdapter(this,
                null, false);
        dataListView.setAdapter(taskCursorAdapter);


        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(MainActivity.this,
                        AddTaskActivity.class);
                Uri currentTaskUri = ContentUris
                        .withAppendedId(ToDoListContract.TaskEntry.CONTENT_URI, id);
                intent.setData(currentTaskUri);
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(TASK_LOADER,
                null, this);
    }

//    private void showDeleteItemDialog(final int id) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Do you want delete the task?");
//        builder.setPositiveButton("Delete",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteItem(id);
//                    }
//                });
//        builder.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (dialog != null) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//    }
//
//    private void deleteItem(int id) {
//            int rowsDeleted =
//            this.getContentResolver().delete(
//                    Uri.withAppendedPath(ToDoListContract.TaskEntry.CONTENT_URI, Integer.toString(id)),
//                    null, null);
//
//            if (rowsDeleted == 0) {
//                Toast.makeText(this,
//                        "Deleting of list from the table failed",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this,
//                        "Task is deleted",
//                        Toast.LENGTH_LONG).show();
//            }
//
//            finish();
//    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ToDoListContract.TaskEntry._ID,
                ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK,
                ToDoListContract.TaskEntry.COLUMN_STATUS,
                ToDoListContract.TaskEntry.COLUMN_DEADLINE,

        };

        CursorLoader cursorLoader = new CursorLoader(this,
                ToDoListContract.TaskEntry.CONTENT_URI,
                projection,
                "editStatus=?",
                new String[]{"0"},
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        taskCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        taskCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_icon:
                showHistory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showHistory(){
        Intent intent = new Intent(MainActivity.this,
                HistoryActivity.class);
        startActivity(intent);

    }
}