package com.example.keyuri.moviereview.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.keyuri.moviereview.R;

/**
 * Created by keyuri on 6/8/2016.
 */
public class Adapter extends CursorAdapter

{
        public Adapter(Context context, Cursor c, int flags){
            super(context, c, flags);

        }

        private String convertCursorRowToUXFormat(Cursor cursor) {
            int idx_movieName = cursor.getColumnIndex(Contract.ReviewsEntry.COLUMN_DISPLAY_TITLE);
            int idx_movieDesp = cursor.getColumnIndex(Contract.ReviewsEntry.COLUMN_SUMMARY_SHORT);
            int idx_moviePublishDate = cursor.getColumnIndex(Contract.ReviewsEntry.COLUMN_PUBLISH_DATE);

            return cursor.getString(1) + " \n *"+  cursor.getString(3) + " \n *" + cursor.getString(2);


        }



        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.listview, parent, false);
            return view;
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView textV = (TextView)view;
            textV.setText(convertCursorRowToUXFormat(cursor));
        }
    }
