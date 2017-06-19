package com.example.pratik.monthlyexpensemanager;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static android.R.attr.data;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File(Environment.getExternalStorageDirectory(),
                "Help Desk Voice Flow.doc");
        setContentView(R.layout.activity_instructions);
        ((Activity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String text="";
                EditText instuc = (EditText) findViewById(R.id.textView_instruction);
                try {
                    InputStream is = getAssets().open("Instructions.txt");
                    int size=is.available();
                    byte[] buffer=new byte[size];
                    is.read(buffer);
                    is.close();
                    text = new String(buffer);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                instuc.setText(text);
                setTitleColor(1);
                instuc.setFocusable(false);
            }
        });

    }
}
