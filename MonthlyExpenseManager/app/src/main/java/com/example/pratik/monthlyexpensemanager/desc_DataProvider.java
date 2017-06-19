package com.example.pratik.monthlyexpensemanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class desc_DataProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.pratik.monthlyexpensemanager.desc_dataprovider";
    private static final String BASE_PATH = "descs";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // Constant to identify the requested operation
    private static final int DESCS = 3;
    private static final int DESCS_ID = 4;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "desc";

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, DESCS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", DESCS_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return database.query(DBOpenHelper.TABLE_desc, DBOpenHelper.DESC_ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.DESC_Year + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_desc,
                null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_desc, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_desc,
                values, selection, selectionArgs);
    }
}
