package com.example.keyuri.moviereview.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;

/**
 * Created by keyuri on 6/8/2016.
 */
public class Content_Provider {

    public static final int movie = 100;
    public static final int movie_id = 200;
    public static final int movie_title = 300;

    private static final UriMatcher Uri_Matcher = buildUriMatcher();
    private movieDbHelper helper;
    private final String LOG = ContentProvider.class.getSimpleName();


    private static final String queryTitle = Contract.ReviewsEntry.TABLE_NAME+
            "." + Contract.ReviewsEntry.COLUMN_DISPLAY_TITLE + " = ? ";

    private static final String QueryID = Contract.ReviewsEntry.TABLE_NAME+
            "." + Contract.ReviewsEntry._ID + " = ? ";

    @Override
    public boolean onCreate() {

        helper = new movieDbHelper(getContext());
        return true;
    }


    public static UriMatcher buildUriMatcher(){

        String cont = Contract.CONTENT;


        UriMatcher match = new UriMatcher(UriMatcher.NO_MATCH);

        match.addURI(cont, Contract.PATH_REVIEWS, movie);

        match.addURI(cont, Contract.PATH_REVIEWS + "/#", movie_id);

        match.addURI(cont, Contract.PATH_REVIEWS + "/*", movie_title);

        return match;
    }



    @Override
    public String getType(Uri ur) {
        switch(Uri_Matcher.match(ur)){
            case movie:
                return Contract.ReviewsEntry.TYPE;
            case movie_id:
                return Contract.ReviewsEntry.ITEM_TYPE;
            case movie_title:
                return Contract.ReviewsEntry.ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + ur);
        }
    }


    @Override
    public Cursor query(Uri ur, String[] projection, String sel, String[] selArgs, String sort) {
        final SQLiteDatabase db = ThemedSpinnerAdapter.Helper.getWritableDatabase();
        Cursor retCursor;
        switch(Uri_Matcher.match(ur)){
            case movie:
                retCursor = db.query(
                        Contract.ReviewsEntry.TABLE_NAME,
                        projection,
                        sel,
                        selArgs,
                        null,
                        null,
                        sort
                );
                Log.d(LOG, "query: success" + retCursor.getCount());

                break;
            case movie_id:
                long _id = ContentUris.parseId(ur);
                retCursor = db.query(
                        Contract.ReviewsEntry.TABLE_NAME,
                        projection,
                        Contract.ReviewsEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sort
                );
                Log.d(LOG, "query: success" + retCursor.getCount());
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + ur);
        }



        retCursor.setNotificationUri(getContext().getContentResolver(), ur);
        return retCursor;
    }


    @Override
    public Uri insert(Uri ur, ContentValues val) {
        final SQLiteDatabase db = ThemedSpinnerAdapter.Helper.getWritableDatabase();
        long id;
        Uri returnUri;

        switch(Uri_Matcher.match(ur)){
            case movie:
                id = db.insert(Contract.ReviewsEntry.TABLE_NAME, null, val);
                if(id > 0){
                    returnUri = Contract.ReviewsEntry.buildMovieUriUsingMovieID(id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + ur);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + ur);
        }


        getContext().getContentResolver().notifyChange(ur, null);
        return returnUri;
    }


    @Override
    public int delete(Uri ur, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = Helper.getWritableDatabase();
        int rows; // Number of rows effected

        switch(Uri_Matcher.match(ur)){
            case movie:
                rows = db.delete(Contract.MovieContract.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + ur);
        }


        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(ur, null);
        }

        return rows;
    }

    @Override
    public int update(Uri ur, ContentValues val, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = Helper.getWritableDatabase();
        int rows;

        switch(Uri_Matcher.match(ur)){
            case movie:
                rows = db.update(Contract.ReviewsEntry.TABLE_NAME, val, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + ur);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(ur, null);
        }

        return rows;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        Helper.close();
        super.shutdown();
    }
}
