package com.example.pratik.monthlyexpensemanager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DataProvider extends ContentProvider{

    private static final String AUTHORITY = "com.example.pratik.monthlyexpensemanager.dataprovider";
    private static final String BASE_PATH = "dates";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int DATES = 1;
    private static final int DATES_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "date";

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, DATES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH +  "/#", DATES_ID);
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
        return database.query(DBOpenHelper.TABLE_date, DBOpenHelper.ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.Year + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_date,
                null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_date, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_date,
                values, selection, selectionArgs);
    }
}