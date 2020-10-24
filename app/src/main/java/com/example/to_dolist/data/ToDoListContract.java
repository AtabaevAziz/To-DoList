package com.example.to_dolist.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ToDoListContract
{

    private ToDoListContract() {

    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "to_dolist";

    public static final String SCHEME = "content://";
    public static final String AUTORITY = "com.example.to_dolist";
    public static final String PATH_LISTS = "lists";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse(SCHEME + AUTORITY);

    public  static final class ListEntry implements BaseColumns {

        public static final String TABLE_NAME = "lists";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DESCRIBE_THE_LIST = "editText";



        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LISTS);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTORITY + "/" + PATH_LISTS;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTORITY + "/" + PATH_LISTS;
    }
}