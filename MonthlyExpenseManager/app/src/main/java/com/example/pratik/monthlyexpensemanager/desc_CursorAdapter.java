package com.example.pratik.monthlyexpensemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.attr.description;

public class desc_CursorAdapter extends CursorAdapter{
    //String ID="";
    public desc_CursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
       // ID=id;
    }
    View view1;
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        view1=LayoutInflater.from(context).inflate(
                R.layout.activity_editor, parent, false);
        return LayoutInflater.from(context).inflate(
                R.layout.description_item, parent, false
        );
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String description="";
        String cost="";
        EditText ed_month=(EditText)view1.findViewById(R.id.editText_Month);
        EditText ed_year=(EditText)view1.findViewById(R.id.editText_Year);

//            String query1 = " SELECT * FROM " + DBOpenHelper.TABLE_desc +
//                    " WHERE " + DBOpenHelper.id + " = " + ID;
//            SQLiteDatabase database;
//            DBOpenHelper helper = new DBOpenHelper(context);
//            database = helper.getWritableDatabase();
//            cursor = database.rawQuery(query1, null);
//        cursor.moveToFirst();
            //cursor.moveToNext();


            description = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.Description));

            cost = cursor.getString(
                    cursor.getColumnIndex(DBOpenHelper.Cost));
//        String description = cursor.getString(
//                cursor.getColumnIndex(DBOpenHelper.Description));
//        String cost=cursor.getString(
//                cursor.getColumnIndex(DBOpenHelper.Cost));

            int pos = description.indexOf(10);
            if (pos != -1) {
                description = description.substring(0, pos) + " ...";
            }

            TextView tv = (TextView) view.findViewById(R.id.item_description);
            tv.setText(description);

            TextView tv1 = (TextView) view.findViewById(R.id.item_cost);
            tv1.setText(cost);



    }
}


