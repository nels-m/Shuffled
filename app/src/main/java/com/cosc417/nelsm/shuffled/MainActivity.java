/* Nelson Murray - 300263834 - 08.04.2019 */

package com.cosc417.nelsm.shuffled;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private final int max = 8;
    private TextView shWord;
    private TextView showScore;
    private TextView showGuesses;
    private EditText guessIn;
    private ImageButton infoButton;
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
    private int shuffleCount;

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

        infoButton = (ImageButton)findViewById(R.id.infoBtn);
        infoButton.setOnClickListener(this);
        shWord = (TextView)findViewById(R.id.shufWord);
        showScore = (TextView)findViewById(R.id.scoreDisplay);
        showGuesses = (TextView)findViewById(R.id.guessDisplay);

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
            shuffleCount++;
            shuffledWord = shuffleWord(currentWord);
            shWord.setText(shuffledWord);
            if(shuffleCount == 3) {
                shuffleBtn.setEnabled(false);
                shuffleBtn.setPaintFlags(shuffleBtn.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }else if(view == newWordBtn) {
            currentWord = randomWord(id, dictionary);
            score--;
            if(score < 0) {
                score = 0;
            }
            guessCount = 0;
            db.updateUserData(id, currentWord, score, guessCount);
            game(id);
        }else if(view == infoButton) {
            Intent infoPop = new Intent(MainActivity.this, Pop.class);
            startActivity(infoPop);
        }
    }

    public void game(int id) {
        guessCount = 0;
        shuffleCount = 0;
        shuffleBtn.setEnabled(true);
        shuffleBtn.setPaintFlags(shuffleBtn.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        if(isNew) {
            currentWord = randomWord(id, dictionary);
            db.insertUserData(id, currentWord);
            shuffledWord = shuffleWord(currentWord);

            score = 0;

            shWord.setText(shuffledWord);
            guessIn.setText("");
            showScore.setText(Integer.toString(score));
            showGuesses.setText(Integer.toString(guessCount));

            isNew = false;
        }else{
            currentWord = db.getCurrentWord(id);
            shuffledWord = shuffleWord(currentWord);

            score = db.getScoreAndGuess(id)[0];
            guessCount = db.getScoreAndGuess(id)[1];

            shWord.setText(shuffledWord);
            guessIn.setText("");
            showScore.setText(Integer.toString(score));
            showGuesses.setText(Integer.toString(guessCount));
        }
    }

    private void checkWord() {
        String w = guessIn.getText().toString().toUpperCase();

        if (currentWord.equals(w)) {
            db.insertGuessedWord(id, w);
            Toast.makeText(this, "Correct! The word is: " + currentWord, Toast.LENGTH_SHORT).show();
            currentWord = randomWord(id, dictionary);

            if(guessCount <= 1){
                score += 5;
            }else if(guessCount <= 3) {
                score += 3;
            }else if(guessCount <= 5) {
                score += 2;
            }else if(guessCount > 5) {
                score++;
            }

            if(score < 0) {
                score = 0;
            }

            guessCount = 0;
            db.updateUserData(id, currentWord, score, guessCount);
            game(id);
        } else {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            showGuesses.setText(Integer.toString(guessCount));
            guessIn.setText("");
            db.updateUserData(id, currentWord, score, guessCount);
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
