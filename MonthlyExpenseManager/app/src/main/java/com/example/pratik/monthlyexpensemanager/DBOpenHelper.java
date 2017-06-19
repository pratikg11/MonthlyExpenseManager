package com.example.pratik.monthlyexpensemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{

    //Constants for db name and version
    private static final String DATABASE_NAME = "calender1.db";
    private static final int DATABASE_VERSION = 9;

    //Constants for identifying table and columns
    public static final String TABLE_date = "date";
    public static final String TABLE_desc = "item_desc";
    public static final String Date_id = "_id";
    public static final String Desc_id = "_id";
    public static final String Month = "month";
    public static final String Year = "year";
    public static final String DESC_Month = "desc_month";
    public static final String DESC_Year = "desc_year";
    public static final String Description = "content";
    public static final String Cost = "item_cost";
    public static final String id = "ID";

    public static final String[] ALL_COLUMNS =
            {Date_id, Month, Year};

    public static final String[] DESC_ALL_COLUMNS =
            {Desc_id, id, DESC_Month, DESC_Year, Description, Cost};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_date + " (" +
                    Date_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Month + " TEXT, " +
                    Year + " TEXT " +

                    ")";
    private static final String TABLE_CREATE1 =
            "CREATE TABLE " + TABLE_desc + " (" +
                    Desc_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    id + " TEXT," +
                    DESC_Month + " TEXT, " +
                    DESC_Year + " TEXT, " +
                    Description + " TEXT, " +
                    Cost + " TEXT " +
                    ")";


    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_date);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_desc);
        onCreate(db);
    }
}