package com.cosc417.nelsm.shuffled;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> dictionary;
    private int minLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        int id = i.getIntExtra("SESSION_ID", 0);
        String user = i.getStringExtra("SESSION_USER");

        // this comment is to test commit

    }

    public void makeDictionary() {
        dictionary = new ArrayList<String>();

        BufferedReader dictionaryIn = null;
        AssetManager am = this.getAssets();

        try {
            dictionaryIn = new BufferedReader(new InputStreamReader(am.open("words_alpha.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
