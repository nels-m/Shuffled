package com.cosc417.nelsm.shuffled;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        int id = i.getIntExtra("SESSION_ID", 0);
        String user = i.getStringExtra("SESSION_USER");


    }
}
