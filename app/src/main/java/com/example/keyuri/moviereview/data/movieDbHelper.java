package com.example.keyuri.moviereview.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class movieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";

    public movieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + Contract.ReviewsEntry.TABLE_NAME + " (" +
                Contract.ReviewsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.ReviewsEntry.COLUMN_DISPLAY_TITLE + " STRING NOT NULL, " +
                Contract.ReviewsEntry.COLUMN_SUMMARY_SHORT + " STRING NOT NULL, " +
                Contract.ReviewsEntry.COLUMN_PUBLISH_DATE + " INTEGER NOT NULL " +
                " );";
        Log.v("SQL QUERY: ", SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.ReviewsEntry.TABLE_NAME);
        //onCreate(sqLiteDatabase);

    }
}


