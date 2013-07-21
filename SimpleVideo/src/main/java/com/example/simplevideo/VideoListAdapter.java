package com.example.simplevideo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


/**
 * Created by stephen on 13-7-20.
 */
public class VideoListAdapter extends CursorAdapter {

    public VideoListAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem, parent, false);
        ((TextView)view.findViewById(R.id.listitem_title)).setText(cursor.getString(cursor.getColumnIndex(SimpleVideo.TITLE)));
        ((TextView)view.findViewById(R.id.listitem_desc)).setText(cursor.getString(cursor.getColumnIndex(SimpleVideo.DESCRIPTION)));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView)view.findViewById(R.id.listitem_title)).setText(cursor.getString(cursor.getColumnIndex(SimpleVideo.TITLE)));
        ((TextView)view.findViewById(R.id.listitem_desc)).setText(cursor.getString(cursor.getColumnIndex(SimpleVideo.DESCRIPTION)));
    }


}
