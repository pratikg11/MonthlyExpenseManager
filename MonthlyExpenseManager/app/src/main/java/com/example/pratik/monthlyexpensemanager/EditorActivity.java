package com.example.pratik.monthlyexpensemanager;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 1002;
    private CursorAdapter cursorAdapter;
    String[] Months ={"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
    };
    String[] Years ={"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030","2031","2032",
            "2033","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"};
    int count;
    private String action;
    private AutoCompleteTextView editor_Month;
    private AutoCompleteTextView editor_Year;
    private String noteFilter;
    private String noteFilter1;
    private String oldText_Month;
    private String oldText_Year;
    private TextView editor_totalCost;
    int id2;
    String id1="";
    String id_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editor_Month = (AutoCompleteTextView)findViewById(R.id.editText_Month);
        editor_Year = (AutoCompleteTextView) findViewById(R.id.editText_Year);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.select_dialog_item,Months);
        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.select_dialog_item,Years);
        editor_Month.setThreshold(1);
        editor_Year.setThreshold(1);
        editor_Month.setAdapter(adapter);
        editor_Year.setAdapter(adapter1);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(DataProvider.CONTENT_ITEM_TYPE);

        if(uri != null) {
            id1= uri.getLastPathSegment();
        }
        if(uri==null)
        {
            Bundle extras1 = intent.getExtras();
            id1 = extras1.getString("ID_m");
            id2=Integer.parseInt(id1)+1;
            id1=String.valueOf(id2);
        }
        cursorAdapter = new desc_CursorAdapter(this, null, 0);
        ListView list1 = (ListView) findViewById(android.R.id.list);
        getLoaderManager().initLoader(0, null, this);


        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
            editor_Month.requestFocus();
            FloatingActionButton bt = (FloatingActionButton) findViewById(R.id.fab);
            TextView desc = (TextView) findViewById(R.id.textView_Description);
            TextView cost = (TextView) findViewById(R.id.textView_Cost);
            desc.setVisibility(View.GONE);
            list1.setVisibility(View.GONE);
            cost.setVisibility(View.GONE);
            bt.setVisibility(View.GONE);
        } else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Expense");

            noteFilter = DBOpenHelper.Date_id + "=" + uri.getLastPathSegment();
            noteFilter1 = DBOpenHelper.id + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText_Month = cursor.getString(cursor.getColumnIndex(DBOpenHelper.Month));
            oldText_Year=cursor.getString(cursor.getColumnIndex(DBOpenHelper.Year)) ;
            editor_Month.setText(oldText_Month);
            editor_Year.setText(oldText_Year);
            editor_Month.setFocusable(false);
            editor_Year.setFocusable(false);
        }
        list1.setAdapter(cursorAdapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EditorActivity.this, desc_EditorActivity.class);
                Uri uri = Uri.parse(desc_DataProvider.CONTENT_URI + "/" + id_insert);
                Bundle extras = new Bundle();
                extras.putLong("ID",id);
                extras.putString("ID1",id1);
                intent.putExtras(extras);
                intent.putExtra(desc_DataProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }

        return true;
    }

    private void deleteNote() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(DataProvider.CONTENT_URI,
                                    noteFilter, null);
                            getContentResolver().delete(desc_DataProvider.CONTENT_URI,
                                    noteFilter1, null);
                            Toast.makeText(EditorActivity.this,
                                    "Month Deleted",
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            		finish();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

    }

    private void finishEditing() {
        String newText_Month = editor_Month.getText().toString().trim();
        String newText_Year= editor_Year.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText_Month.length() == 0 || newText_Year.length() == 0) {
                    setResult(RESULT_CANCELED);
                    Toast.makeText(this, "Data not inserted..Please fill both fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    insertNote(newText_Month,newText_Year);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText_Month.length() == 0 || newText_Year.length() == 0) {
                    deleteNote();
                } else if (oldText_Month.equals(newText_Month) && oldText_Year.equals(newText_Year)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText_Month,newText_Year);
                }

        }
        finish();
    }

    private void updateNote(String newText_Month,String newText_Year) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.Month, newText_Month);
        values.put(DBOpenHelper.Year, newText_Year);
        getContentResolver().update(DataProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String newText_Month,String newText_Year) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.Month, newText_Month);
        values.put(DBOpenHelper.Year, newText_Year);
        getContentResolver().insert(DataProvider.CONTENT_URI, values);

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        count = saved_values.getInt("count",1);

        SharedPreferences saved_values1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=saved_values1.edit();
        editor.putInt("count",count+1);
        editor.commit();
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String query1 =DBOpenHelper.id + " = " + id1;
        return new CursorLoader(this, desc_DataProvider.CONTENT_URI,
                null, query1, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
        int count=0;
        String query1 = "SELECT "+DBOpenHelper.Cost +" FROM "+DBOpenHelper.TABLE_desc +" WHERE "+ DBOpenHelper.id + " = " + id1;;
            SQLiteDatabase database;
            DBOpenHelper helper = new DBOpenHelper(this);
            database = helper.getWritableDatabase();
            data = database.rawQuery(query1, null);
        data.moveToFirst();
        while(!data.isAfterLast())
        {
            count=count+Integer.parseInt(data.getString(0));
            data.moveToNext();
        }
            editor_totalCost = (TextView) findViewById(R.id.textView_totalCostValue);
        editor_totalCost.setText(String.valueOf(count));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void buttonClick(View view)
    {
        finishEditing();
    }
    public void desc_openEditorForNewNote(View view) {
        Intent intent = new Intent(EditorActivity.this, desc_EditorActivity.class);
        Bundle extras = new Bundle();

        extras.putString("ID_insert",id1);
        intent.putExtras(extras);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}

