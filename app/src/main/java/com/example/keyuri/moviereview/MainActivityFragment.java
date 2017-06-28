package com.example.keyuri.moviereview;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> movieAdapter;

    public MainActivityFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh){
            //Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_LONG).show();
            FetchMovieTask task = new FetchMovieTask();
            task.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] movies = {
                "Hangover", "Dark knight", "Batman vs Superman", "Home Alone", "JungleBook"
        };
        List<String> moviesList = new ArrayList<String>(Arrays.asList(movies));

        movieAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.list_item_textview, moviesList);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_review);
        listView.setAdapter(movieAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> AdapterView, View view, int position, long l) {
                String review = (String) movieAdapter.getItem(position);
                Toast.makeText(getActivity(), review, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = "FetchMovieTask";
        @Override
        protected void onPostExecute(String[] result) {
                movieAdapter.clear();
                movieAdapter.addAll(result);
            }


        @Override
        protected String[] doInBackground(Void...params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        String format = "json";

        try {

            final String MOVIE_BASE_URL =
                    "http://api.nytimes.com/svc/movies/v2/reviews/all.json?";
            final String APPID_PARAM = "APPID";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_REVIEW_API_KEY)
                    .build();

            URL url = new URL("http://api.nytimes.com/svc/movies/v2/reviews/all.json?&api-key=f3af8f0d78bc8f138a81e6f1b3af600f:5:75095957");
            Log.v(LOG_TAG,"URL: "+url);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();
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

        try {
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
        }
        private String[] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String OWM_TITLE = "display_title";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            int numReviews=movieJson.getInt("num_results");
            String[] resultStrs=new String[numReviews];
            JSONArray movieArray = movieJson.getJSONArray("results");
            for(int i=0;i<numReviews;i++)
            {
                JSONObject review=movieArray.getJSONObject(i);
                String name=review.getString(OWM_TITLE);
                resultStrs[i]=name;

            }
            return resultStrs;

        }
    }

}