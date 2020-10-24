package com.example.to_dolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ToDoListContentProvider extends ContentProvider {

    ToDoListDbOpenHelper dbOpenHelper;

    private static final int LISTS = 111;
    private static final int LIST_ID = 222;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(ToDoListContract.AUTORITY, ToDoListContract.PATH_LISTS, LISTS);
        uriMatcher.addURI(ToDoListContract.AUTORITY, ToDoListContract.PATH_LISTS
                + "/#", LIST_ID);
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
            case LISTS:
                cursor = db.query(ToDoListContract.ListEntry.TABLE_NAME,projection,selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case LIST_ID:
                selection = ListEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ListEntry.TABLE_NAME, projection, selection,
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

        String list = values.getAsString(ListEntry.COLUMN_DESCRIBE_THE_LIST);
        if (list == null) {
            throw new IllegalArgumentException("You have to input list");
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case LISTS:
                long id = db.insert(ListEntry.TABLE_NAME,null,values);
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
            case LISTS:

                rowsDeleted = db.delete(ListEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case LIST_ID:
                selection = ListEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ListEntry.TABLE_NAME, selection, selectionArgs);
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

        if (values.containsKey(ListEntry.COLUMN_DESCRIBE_THE_LIST)) {
            String list = values.getAsString(ListEntry.COLUMN_DESCRIBE_THE_LIST);
            if (list == null) {
                throw new IllegalArgumentException("You have to input list");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match) {
            case LISTS:

                rowsUpdated = db.update(ListEntry.TABLE_NAME, values, selection, selectionArgs);

                break;

            case LIST_ID:
                selection = ListEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(ListEntry.TABLE_NAME, values,
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
            case LISTS:

                return ListEntry.CONTENT_MULTIPLE_ITEMS;

            case LIST_ID:
                return ListEntry.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }

}