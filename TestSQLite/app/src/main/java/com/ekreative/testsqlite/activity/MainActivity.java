package com.ekreative.testsqlite.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ekreative.testsqlite.R;
import com.ekreative.testsqlite.db.FeedReaderContract;
import com.ekreative.testsqlite.db.FeedReaderDbHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private ListView list;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(MainActivity.this);
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();


        findViewById(R.id.button_add).setOnClickListener(this);
        findViewById(R.id.button_read).setOnClickListener(this);
        list = (ListView)findViewById(R.id.list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "title");
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle");

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                Log.i(TAG, "Added row ID: " + newRowId);
                break;
            case R.id.button_read:
                // Define a projection that specifies which columns from the database
                // you will actually use after this query.
                String[] projection = {
                        FeedReaderContract.FeedEntry._ID,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
                };

                // Filter results WHERE "title" = 'My Title'
                /*String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
                String[] selectionArgs = { "My Title" };*/

                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

                Cursor cursor = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        null,//selection,                                // The columns for the WHERE clause
                        null,//selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );
                /*List itemIds = new ArrayList<>();
                while(cursor.moveToNext()) {
                    long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                    itemIds.add(itemId);
                }
                Log.i(TAG, "Ids: " + itemIds.toString());
                */
                //==
                //cursor.close();
                String[] from = new String[] { FeedReaderContract.FeedEntry._ID,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE };
                int[] to = new int[] { R.id.txt_id, R.id.txt_title, R.id.txt_subtitle};
                adapter = new SimpleCursorAdapter(this, R.layout.item_list, cursor, from, to, 0);
                list.setAdapter(adapter);
                //==
                break;
        }

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
