package com.example.keyuri.moviereview.data;

/**
 * Created by keyuri on 5/20/2016.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Defines table and column names for the weather database.
 */
public class Contract {

    public static final String CONTENT = "com.example.keyuri.moviereview";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT);
    public static final String PATH_REVIEWS = "reviews";


    public static final class ReviewsEntry implements BaseColumns {

        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT + "/" + PATH_REVIEWS;
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT + "/" + PATH_REVIEWS;



        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_ID="_id";
        public static final String COLUMN_DISPLAY_TITLE = "display_title";
        public static final String COLUMN_SUMMARY_SHORT = "summary_short";
        public static final String COLUMN_PUBLISH_DATE = "publish_date";


        public static Uri buildUri() {
            return URI;
        }

        public static Uri MovieUriMovieID(long id) {
            return ContentUris.withAppendedId(URI, id);
        }

        public static Uri MovieUriMovieName(String title) {

            return URI.buildUpon().appendEncodedPath(title).build();
        }

        public static String getMovieTitle(URI uri) {
            String[] s = uri.getPath().split("/");
            String Title = s[s.length - 1];
            return Title;
        }

    }

}




