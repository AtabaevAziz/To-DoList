package com.example.to_dolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.to_dolist.R;
import com.example.to_dolist.data.ToDoListContract;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int EDIT_TASK_LOADER = 111;
    

    private EditText describeTheListEditText;
    private Uri currentListUri;
    private EditText statusEditText;
    private EditText deadlineEditText;

    int DIALOG_DATE = 1;
    Calendar calendar = Calendar.getInstance();
    int myyear = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        Intent intent = getIntent();

        currentListUri = intent.getData();

        if (currentListUri == null) {
            setTitle("Add a Task");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Task");
            getSupportLoaderManager().initLoader(EDIT_TASK_LOADER,
                    null, this);
        }

        describeTheListEditText = findViewById(R.id.editText);
        statusEditText = findViewById(R.id.ediStatus);
        deadlineEditText = findViewById(R.id.editDeadline);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (currentListUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_list);
            menuItem.setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_list_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_list:
                saveList();
                return  true;
            case R.id.delete_list:
                showDeleteListDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveList() {


        String editText = describeTheListEditText.getText().toString().trim();

        if (TextUtils.isEmpty(editText)) {
            Toast.makeText(this,
                    "Input the describe the text",
                    Toast.LENGTH_LONG).show();
            return;

        }

        String status = statusEditText.getText().toString().trim();

        if (TextUtils.isEmpty(status)) {
            Toast.makeText(this,
                    "Input the status",
                    Toast.LENGTH_LONG).show();
            return;

        }

        String deadline = deadlineEditText.getText().toString().trim();

        if (TextUtils.isEmpty(deadline)) {
            Toast.makeText(this,
                    "Input the deadline",
                    Toast.LENGTH_LONG).show();
            return;

        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK, editText);
        contentValues.put(ToDoListContract.TaskEntry.COLUMN_STATUS, status);
        contentValues.put(ToDoListContract.TaskEntry.COLUMN_DEADLINE, deadline);

        if (currentListUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(ToDoListContract.TaskEntry.CONTENT_URI,
                    contentValues);

            if (uri == null) {
                Toast.makeText(this,
                        "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Task saved", Toast.LENGTH_LONG).show();

            }
        } else {
            int rowsChanged = getContentResolver().update(currentListUri,
                    contentValues, null, null);

            if (rowsChanged == 0) {
                Toast.makeText(this,
                        "Saving of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "List updated", Toast.LENGTH_LONG).show();
            }
        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                ToDoListContract.TaskEntry._ID,
                ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK,
                ToDoListContract.TaskEntry.COLUMN_STATUS,
                ToDoListContract.TaskEntry.COLUMN_DEADLINE,

        };

        return new CursorLoader(this,
                currentListUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int  taskColumIndex = cursor.getColumnIndex(
                    ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK
            );


            String describeTheList = cursor.getString(taskColumIndex);

            describeTheListEditText.setText(describeTheList);

            int  statusColumIndex = cursor.getColumnIndex(
                    ToDoListContract.TaskEntry.COLUMN_STATUS
            );


            String status = cursor.getString(statusColumIndex);

            statusEditText.setText(status);

            int  deadlineIndex = cursor.getColumnIndex(
                    ToDoListContract.TaskEntry.COLUMN_DEADLINE
            );


            String deadline = cursor.getString(deadlineIndex);

            deadlineEditText.setText(deadline);




        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the task?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteList();
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

    private void deleteList() {
        if (currentListUri != null) {
            int rowsDeleted = getContentResolver().delete(currentListUri,
                    null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,
                        "Deleting of list from the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Task is deleted",
                        Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }

    public void onChangeDeadline(View view) {showDialog(DIALOG_DATE);
    }


    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myyear, month, day);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myyear = year - 2000;
            month = monthOfYear + 1;
            day = dayOfMonth;
            deadlineEditText.setText(day + "/" + month + "/" + myyear);
        }
    };
}