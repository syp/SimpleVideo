package com.example.simplevideo;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by stephen on 13-7-18.
 */
public class SimpleVideo implements BaseColumns {
    public static final String SIMPLE_AUTHORITY = "com.example.simplevideo";
    public static final Uri CONTENT_URI = Uri.parse("content://"+SIMPLE_AUTHORITY+"/video");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.simplevideo";
    public static final String CONTENT_VIDEO_TYPE = "vnd.android.cursor.item/com.example.simplevideo";

    public static final String VIDEO = "video";
    public static final String TITLE="title";
    public static final String DESCRIPTION="description";
    public static final String URI="uri";
    public static final String MEDIA_ID="media_id";

}
