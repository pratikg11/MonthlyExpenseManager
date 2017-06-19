package com.example.pratik.monthlyexpensemanager;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class desc_EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor1;
    private EditText editor2;
    private String noteFilter;
    private String oldText_desc;
    private String oldText_cost;
    String id_insert;
    String id1;
    Long id_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc__editor);

        editor1 = (EditText) findViewById(R.id.editText1);
        //editor1.setSelection(10);
        editor2 = (EditText) findViewById(R.id.editText2);
        //editor2.setSelection(10);
        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(desc_DataProvider.CONTENT_ITEM_TYPE);
        //if (uri != null) {
            Bundle extras = intent.getExtras();
            id_1 = extras.getLong("ID");
            id1 = extras.getString("ID1");


        if (uri == null) {
            action = Intent.ACTION_INSERT;
            editor1.requestFocus();
            setTitle("New Entry");
        } else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Entry");
            noteFilter = DBOpenHelper.Desc_id + "=" + id_1;

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.DESC_ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText_desc = cursor.getString(cursor.getColumnIndex(DBOpenHelper.Description));
            oldText_cost = cursor.getString(cursor.getColumnIndex(DBOpenHelper.Cost));
            editor1.setText(oldText_desc);
            editor2.setText(oldText_cost);
            editor1.requestFocus();
        }
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
                            getContentResolver().delete(desc_DataProvider.CONTENT_URI,
                                    noteFilter, null);

                            Toast.makeText(desc_EditorActivity.this,
                                    "Expense Deleted",
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
        String newText_desc = editor1.getText().toString().trim();
        String newText_cost = editor2.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText_desc.length() == 0 || newText_cost.length()==0) {
                    setResult(RESULT_CANCELED);
                    Toast.makeText(this, "Data not inserted..Please fill both fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    insertNote(newText_desc,newText_cost,id1);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText_desc.length() == 0 && newText_cost.length()==0) {
                    deleteNote();
                }
                else if (oldText_desc.equals(newText_desc) && oldText_cost.equals(newText_cost)) {
                    setResult(RESULT_CANCELED);
                }
                else {
                    updateNote(newText_desc,newText_cost);
                }

        }
        finish();
    }

    private void updateNote(String newText_desc,String newText_cost) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.Description, newText_desc);
        values.put(DBOpenHelper.Cost, newText_cost);
        getContentResolver().update(desc_DataProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String newText_desc,String newText_cost,String id) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id_insert = extras.getString("ID_insert");
        int count= Integer.parseInt(id_insert);
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.Description, newText_desc);
        values.put(DBOpenHelper.Cost, newText_cost);
        values.put(DBOpenHelper.id,String.valueOf(count));
        getContentResolver().insert(desc_DataProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
