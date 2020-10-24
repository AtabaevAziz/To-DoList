package com.example.to_dolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.to_dolist.R;
import com.example.to_dolist.data.ToDoListContract;

public class AddListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int EDIT_MEMBER_LOADER = 111;
    

    private EditText describeTheListEditText;
    private Uri currentListUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        Intent intent = getIntent();

        currentListUri = intent.getData();

        if (currentListUri == null) {
            setTitle("Add a List");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the List");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER,
                    null, this);
        }

        describeTheListEditText = findViewById(R.id.editText);


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
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoListContract.ListEntry.COLUMN_DESCRIBE_THE_LIST, editText);

        if (currentListUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(ToDoListContract.ListEntry.CONTENT_URI,
                    contentValues);

            if (uri == null) {
                Toast.makeText(this,
                        "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Data saved", Toast.LENGTH_LONG).show();

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
                ToDoListContract.ListEntry._ID,
                ToDoListContract.ListEntry.COLUMN_DESCRIBE_THE_LIST,

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
            int firstNameColumIndex = cursor.getColumnIndex(
                    ToDoListContract.ListEntry.COLUMN_DESCRIBE_THE_LIST
            );


            String describeTheList = cursor.getString(firstNameColumIndex);

            describeTheListEditText.setText(describeTheList);



        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
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
                        "List is deleted",
                        Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }

}