package com.example.to_dolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.to_dolist.data.ToDoListContract.TaskEntry;

public class ToDoListContentProvider extends ContentProvider {

    ToDoListDbOpenHelper dbOpenHelper;

    private static final int TASKS = 111;
    private static final int TASK_ID = 222;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(ToDoListContract.AUTORITY, ToDoListContract.PATH_TASKS, TASKS);
        uriMatcher.addURI(ToDoListContract.AUTORITY, ToDoListContract.PATH_TASKS
                + "/#", TASK_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new ToDoListDbOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {



        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case TASKS:
                cursor = db.query(ToDoListContract.TaskEntry.TABLE_NAME,projection,selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case TASK_ID:
                selection = ToDoListContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ToDoListContract.TaskEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {

        String task = values.getAsString(TaskEntry.COLUMN_DESCRIBE_THE_TASK);
        if (task == null) {
            throw new IllegalArgumentException("You have to input task");
        }

        String status = values.getAsString(TaskEntry.COLUMN_STATUS);
        if (status == null) {
            throw new IllegalArgumentException("You have to input status");
        }

        String deadline = values.getAsString(TaskEntry.COLUMN_DEADLINE);
        if (deadline == null) {
            throw new IllegalArgumentException("You have to input deadline");
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case TASKS:
                long id = db.insert(TaskEntry.TABLE_NAME,null,values);
                if (id == -1) {
                    Log.e("insertMethod", "Insertion of data in the table failed for " + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);


            default:
                throw new IllegalArgumentException("Insertion of data in the table failed for " + uri);

        }

    }

    @Override
    public int delete(Uri uri,  String selection,  String[] selectionArgs) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsDeleted;

        switch (match) {
            case TASKS:

                rowsDeleted = db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TASK_ID:
                selection = ToDoListContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ToDoListContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Can't delete this URI " + uri);

        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(TaskEntry.COLUMN_DESCRIBE_THE_TASK)) {
            String list = values.getAsString(TaskEntry.COLUMN_DESCRIBE_THE_TASK);
            if (list == null) {
                throw new IllegalArgumentException("You have to input task");
            }
        }

        if (values.containsKey(TaskEntry.COLUMN_STATUS)) {
            String status = values.getAsString(TaskEntry.COLUMN_STATUS);
            if (status == null) {
                throw new IllegalArgumentException("You have to input status");
            }
        }

        if (values.containsKey(TaskEntry.COLUMN_DEADLINE)) {
            String deadline = values.getAsString(TaskEntry.COLUMN_DEADLINE);
            if (deadline == null) {
                throw new IllegalArgumentException("You have to input deadline");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match) {
            case TASKS:

                rowsUpdated = db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

                break;

            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(TaskEntry.TABLE_NAME, values,
                        selection, selectionArgs);

                break;

            default:
                throw new IllegalArgumentException("Can't update this URI " + uri);

        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case TASKS:

                return TaskEntry.CONTENT_MULTIPLE_ITEMS;

            case TASK_ID:
                return TaskEntry.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }
}