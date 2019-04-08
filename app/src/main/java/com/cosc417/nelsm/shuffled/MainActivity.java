package com.cosc417.nelsm.shuffled;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> dictionary;
    private final int min = 4;
    private final int max = 7;
    //private TextView testing;
    private TextView shWord;
    private TextView showScore;
    private TextView showGuesses;
    private EditText guessIn;
    private Button guessBtn;
    private Button shuffleBtn;
    private Button newWordBtn;

    private DBHelper db;
    private int score;
    private int id;
    private String currentWord;
    private String shuffledWord;
    private String guess;
    private String username;
    private Boolean isNew;
    private int guessCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeDictionary();

        Intent intent = getIntent();
        db = new DBHelper(this);
        id = intent.getIntExtra("SESSION_ID", -1);
        username = intent.getStringExtra("SESSION_USER");
        isNew = intent.getBooleanExtra("SESSION_NEW", false);

        currentWord = "";

        shWord = (TextView)findViewById(R.id.shufWord);
        showScore = (TextView)findViewById(R.id.scoreDisplay);
        showGuesses = (TextView)findViewById(R.id.guessDisplay);
        //testing = (TextView)findViewById(R.id.testing);

        guessIn = (EditText)findViewById(R.id.guess_in);
        guessBtn = (Button)findViewById(R.id.guess_button);
        guessBtn.setOnClickListener(this);
        shuffleBtn = (Button)findViewById(R.id.shuffle_button);
        shuffleBtn.setOnClickListener(this);
        newWordBtn = (Button)findViewById(R.id.new_button);
        newWordBtn.setOnClickListener(this);

        game(id);
    }

    @Override
    public void onClick(View view) {
        if(view == guessBtn) {
            guessCount++;
            checkWord();
        }else if(view == shuffleBtn) {
            shuffledWord = shuffleWord(currentWord);
            shWord.setText(shuffledWord);
        }else if(view == newWordBtn) {
            currentWord = randomWord(id, dictionary);
            score -= 2;
            if(score < 0) {
                score = 0;
            }
            db.updateUserData(id, currentWord, score);
            game(id);
        }
    }

    public void game(int id) {
        guessCount = 0;

        if(isNew) {
            currentWord = randomWord(id, dictionary);
            db.insertUserData(id, currentWord);
            shuffledWord = shuffleWord(currentWord);
            score = 0;
            shWord.setText(shuffledWord);
            guessIn.setText("");
            showScore.setText(Integer.toString(score));
            showGuesses.setText(Integer.toString(guessCount));
            //testing.setText(currentWord);
            isNew = false;
        }else{
            currentWord = db.getCurrentWord(id);
            shuffledWord = shuffleWord(currentWord);
            score = db.getScore(id);
            shWord.setText(shuffledWord);
            guessIn.setText("");
            showScore.setText(Integer.toString(score));
            showGuesses.setText(Integer.toString(guessCount));
            //testing.setText(currentWord);
        }
    }

    private void checkWord() {
        String w = guessIn.getText().toString().toUpperCase();

        if (currentWord.equals(w)) {
            db.insertGuessedWord(id, w);
            Toast.makeText(this, "Correct! The word is: " + currentWord, Toast.LENGTH_SHORT).show();
            currentWord = randomWord(id, dictionary);

            if(guessCount < 2) {
                score += 3;
            }else if(guessCount <= 3) {
                score += 2;
            }else if(guessCount > 3) {
                score++;
            }

            if(score < 0) {
                score = 0;
            }

            db.updateUserData(id, currentWord, score);
            game(id);
        } else {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            showGuesses.setText(Integer.toString(guessCount));
            guessIn.setText("");
        }
    }

    public void makeDictionary() {
        BufferedReader read = null;
        String line = "";
        dictionary = new ArrayList<String>();

        try {
            read = new BufferedReader(new InputStreamReader(getAssets().open("words_alpha.txt")));

            while((line = read.readLine()) != null) {
                dictionary.add(line);
            }
            read.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String randomWord(int id, ArrayList<String> dictionary) {
        String temp = null;
        String res = null;
        Boolean isGuessed = false;
        Random random = new Random();

        for(int i = 0; i < dictionary.size(); i++) {
            temp = dictionary.get(random.nextInt(dictionary.size()));
        }

        isGuessed = db.isGuessedWord(id, temp);

        if(temp.length() >= min && temp.length() <= max && !isGuessed) {
            res = temp;
        }else {
            res = randomWord(id, dictionary);
        }

        return res;
    }

    public String shuffleWord(String word) {

        Random random = new Random();

        if(word != null && !"".equals(word)) {
            char[] c = word.toCharArray();

            for(int i = 0; i < c.length; i++) {
                int r = random.nextInt(c.length);
                char temp = c[i];
                c[i] = c[r];
                c[r] = temp;
            }
            return new String(c);
        }
        return word;
    }
}
