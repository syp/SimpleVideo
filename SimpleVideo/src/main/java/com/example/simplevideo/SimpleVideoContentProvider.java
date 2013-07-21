package com.example.simplevideo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;


/**
 * Created by stephen on 13-7-18.
 */
public class SimpleVideoContentProvider extends ContentProvider {

    private static final String DATABASE_NAME="simple_video.db";
    private static final int DATABASE_VERSION = 1;
    private static final String VIDEO_TABLE_NAME = "video";
    private SQLiteDatabase mDb;

    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE "+ VIDEO_TABLE_NAME +" ("+
                    SimpleVideo._ID + " INTEGER PRIMARY KEY, " +
                    SimpleVideo.TITLE + " TEXT, " +
                    SimpleVideo.DESCRIPTION + " TEXT, " +
                    SimpleVideo.URI + " TEXT);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+VIDEO_TABLE_NAME);
            onCreate(db);
        }


    }

    private static UriMatcher uriMatcher;
    private static final int VIDEOS = 1;
    private static final int VIDEO_ID = 2;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(SimpleVideo.SIMPLE_AUTHORITY, SimpleVideo.VIDEO, VIDEOS);
        uriMatcher.addURI(SimpleVideo.SIMPLE_AUTHORITY, SimpleVideo.VIDEO+"/#", VIDEO_ID);


    }

    @Override
    public boolean onCreate() {
        mDb = new DatabaseHelper(this.getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        switch(uriMatcher.match(uri)){
            case VIDEOS:
                c = mDb.query(VIDEO_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), SimpleVideo.CONTENT_URI);
                break;
            case VIDEO_ID:
                long video_id = ContentUris.parseId(uri);
                c = mDb.query(VIDEO_TABLE_NAME,projection,SimpleVideo._ID+"="+video_id+
                        (TextUtils.isEmpty(selection)?"":" AND ("+selection+")"),
                        selectionArgs,null,null,sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), SimpleVideo.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI "+uri);
        }

        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case VIDEOS:
                return SimpleVideo.CONTENT_TYPE;
            case VIDEO_ID:
                return SimpleVideo.CONTENT_VIDEO_TYPE;
            default:
                throw new IllegalArgumentException("Unknown video type: "+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(uriMatcher.match(uri) == VIDEOS){
            throw new IllegalArgumentException("Unsupported URI for insert: "+uri);
        }
        long rowId = mDb.insert(VIDEO_TABLE_NAME, SimpleVideo.TITLE,values );
        if(rowId > 0){
            Uri videoUri = ContentUris.withAppendedId(SimpleVideo.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(videoUri,null);
            return videoUri;
        }
        throw new SQLiteException("Failed to insert into row" + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int affected;
        switch (uriMatcher.match(uri)){
            case VIDEOS:
                affected = mDb.delete(VIDEO_TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO_ID:
                long rowId = ContentUris.parseId(uri);
                affected = mDb.delete(VIDEO_TABLE_NAME, SimpleVideo._ID +"="+rowId+
                        (TextUtils.isEmpty(selection)?"":" AND ("+selection+")"),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int affected;
        switch (uriMatcher.match(uri)){
            case VIDEOS:
                affected = mDb.update(VIDEO_TABLE_NAME, values, selection, selectionArgs);
                break;
            case VIDEO_ID:
                long rowId= ContentUris.parseId(uri);
                affected = mDb.update(VIDEO_TABLE_NAME, values, SimpleVideo._ID+"="+rowId+
                        (TextUtils.isEmpty(selection)?"":" AND ("+selection+")"),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;

    }
}
