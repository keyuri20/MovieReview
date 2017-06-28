package com.example.keyuri.moviereview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.keyuri.moviereview.data.Contract;
import com.example.keyuri.moviereview.data.movieDbHelper;

import java.util.Map;
import java.util.Set;

public class TestDB extends AndroidTestCase {
    Context context;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();
    }


    public void testInsertLocation() {

        // mContext.deleteDatabase(movieDbHelper.DATABASE_NAME);
        movieDbHelper helper = new movieDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();


        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Contract.ReviewsEntry.COLUMN_DISPLAY_TITLE,"Ki & Ka");
        contentvalues.put(Contract.ReviewsEntry.COLUMN_SUMMARY_SHORT,"good movie");
        contentvalues.put(Contract.ReviewsEntry.COLUMN_PUBLISH_DATE,20160520);

        Cursor cursor = null;

        try{


            long locId = db.insert(Contract.ReviewsEntry.TABLE_NAME,null,contentvalues);

            assertTrue(locId != -1);

            cursor = db.query(Contract.ReviewsEntry.TABLE_NAME, null, null, null, null, null, null);

            assertTrue("No records", cursor.moveToFirst());

            assertTrue(validateCurrentRecord("Records do not match", cursor, contentvalues));


            long movieRowId = db.insert(Contract.ReviewsEntry.TABLE_NAME, null, contentvalues);

                    Cursor movieCursor = db.query(
                            Contract.ReviewsEntry.TABLE_NAME,  // Table to Query
                            null, // leaving "columns" null just returns all the columns.
                            null, // cols for "where" clause
                            null, // values for "where" clause
                            null, // columns to group by
                            null, // columns to filter by row groups
                            null  // sort order
                    );

            assertTrue(validateCurrentRecord("testInsertReadDb reviewsEntry failed to validate",
                    movieCursor, contentvalues));


        }catch(Exception e){
            Log.e("test", e.getMessage());

        }finally{
            db.delete(Contract.ReviewsEntry.TABLE_NAME, null, null);
            db.execSQL("DROP TABLE IF EXISTS "+ Contract.ReviewsEntry.TABLE_NAME);

            if(cursor != null) cursor.close();
            if(db != null)db.close();
        }


    }


    static boolean validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            if (idx == -1) return false;
            String expectedValue = entry.getValue().toString();
            String retrievedValue = valueCursor.getString(idx);

            if (!retrievedValue.equals(expectedValue)) return false;

        }
        return true;
    }



}