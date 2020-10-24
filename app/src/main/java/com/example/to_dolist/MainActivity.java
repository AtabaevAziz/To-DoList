package com.example.to_dolist;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.to_dolist.data.ToDoListContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LIST_LOADER = 123;
    ListCursorAdapter listCursorAdapter;

    ListView dataListView;

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
                        AddListActivity.class);
                startActivity(intent);

            }
        });

        listCursorAdapter = new ListCursorAdapter(this,
                null, false);
        dataListView.setAdapter(listCursorAdapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(MainActivity.this,
                        AddListActivity.class);
                Uri currentMemberUri = ContentUris
                        .withAppendedId(ToDoListContract.ListEntry.CONTENT_URI, id);
                intent.setData(currentMemberUri);
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(LIST_LOADER,
                null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ToDoListContract.ListEntry._ID,
                ToDoListContract.ListEntry.COLUMN_DESCRIBE_THE_LIST,

        };

        CursorLoader cursorLoader = new CursorLoader(this,
                ToDoListContract.ListEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        listCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        listCursorAdapter.swapCursor(null);

    }

}