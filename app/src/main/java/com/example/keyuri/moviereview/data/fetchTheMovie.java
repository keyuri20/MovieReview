package com.example.keyuri.moviereview.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.keyuri.moviereview.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keyuri on 6/8/2016.
 */

public class fetchTheMovie extends AsyncTask<Void,Void,List<String>>
{
        private final String LOG_TAG = "fetchTheMovie";
        private final Context mContext;
        public fetchTheMovie(Context context) {
            mContext = context;
        }


        public long addReview(String movie, String publish_Date, String description) {
            long review_Id;


            Cursor mCursor = mContext.getContentResolver().query(
                    Contract.ReviewsEntry.URI,
                    new String[]{Contract.ReviewsEntry._ID},
                    Contract.ReviewsEntry.COLUMN_DISPLAY_TITLE + " = ?",
                    new String[]{movie},
                    null);

            if (mCursor.moveToFirst()) {
                int Id = mCursor.getColumnIndex(Contract.ReviewsEntry._ID);
                review_Id = mCursor.getLong(Id);
            } else {

                ContentValues reviews = new ContentValues();


                reviews.put(Contract.ReviewsEntry.COLUMN_DISPLAY_TITLE, movie);
                reviews.put(Contract.ReviewsEntry.COLUMN_PUBLISH_DATE, publish_Date);
                reviews.put(Contract.ReviewsEntry.COLUMN_SUMMARY_SHORT, description);



                Uri insert = mContext.getContentResolver().insert(
                        Contract.ReviewsEntry.URI,
                        reviews
                );


                review_Id = ContentUris.parseId(insert);
            }

            mCursor.close();

            return review_Id;
        }


        @Override
        protected List<String> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            final String MOVIE_BASE_URL =
                    "http://api.nytimes.com/svc/movies/v2/reviews/all.json?";
            final String THOUSAND_BEST_PARAM = "PARAM";
            final String APPID_PARAM = "APPID";
            final String ValueIndicator = "Y";

            try {

            URL url = new URL("http://api.nytimes.com/svc/movies/v2/reviews/all.json?&api-key=f3af8f0d78bc8f138a81e6f1b3af600f:5:75095957");
                Log.v(LOG_TAG,"URL: "+url);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();


                Log.v(LOG_TAG, "Json String" + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            List<String> movieTitle = new ArrayList<String>();
            movieTitle = getStringJson(movieJsonStr);
            if (movieTitle != null)
                return movieTitle;

            else
                return null;


        }

        public List<String> getStringJson(String jsonMovieReview) {
            List<String> movieTitle = new ArrayList<String>();

            JSONObject jsonObject = null, JsonObject = null;
            JSONArray result = null;

            String movie_title = null, Description = null, movie_Publish_Date = null;

            int i = 0;
            try {
                jsonObject = new JSONObject(jsonMovieReview);
                result = jsonObject.getJSONArray("results");



                for (i = 0; i < result.length(); i++) {
                    JsonObject = result.getJSONObject(i);
                    movie_title = JsonObject.getString("display_title");
                    Description = JsonObject.getString("summary_short");
                    movie_Publish_Date = JsonObject.getString("publication_date");
                    movieTitle.add(movie_title);
                    movieTitle.add(movie_Publish_Date);
                    movieTitle.add(Description);
                    long movieID = addMovieReview(movie_title, movie_Publish_Date, Description);
                    Log.d(LOG_TAG, " inserting data   : " + movieID);

                }
                Log.d(LOG_TAG, "Fetch Movie Task Complete.     " + (i) + " Inserted");


            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return movieTitle;
        }
    }

