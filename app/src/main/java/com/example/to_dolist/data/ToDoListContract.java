package com.example.to_dolist.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ToDoListContract
{

    private ToDoListContract() {

    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todolist";

    public static final String SCHEME = "content://";
    public static final String AUTORITY = "com.example.todolist";
    public static final String PATH_TASKS = "tasks";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse(SCHEME + AUTORITY);

    public  static final class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME = "tasks";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DESCRIBE_THE_TASK = "editText";



        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASKS);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTORITY + "/" + PATH_TASKS;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTORITY + "/" + PATH_TASKS;
    }
}