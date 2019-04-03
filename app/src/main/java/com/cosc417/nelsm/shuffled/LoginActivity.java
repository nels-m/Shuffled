package com.cosc417.nelsm.shuffled;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText tUsername;
    EditText tPassword;
    Button bLogin;
    TextView tRegister;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);
        tUsername = (EditText)findViewById(R.id.login_username);
        tPassword = (EditText)findViewById(R.id.login_password);
        bLogin = (Button)findViewById(R.id.login);
        tRegister = (TextView) findViewById(R.id.register);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = tUsername.getText().toString();
                String pass = tPassword.getText().toString();
                Boolean result = db.verifyUser(user, pass);
                int id = db.getUserID(user);

                if(result) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent iMainActivity = new Intent (LoginActivity.this, MainActivity.class);

                    iMainActivity.putExtra("SESSION_ID", id);
                    iMainActivity.putExtra("SESSION_USER", user);

                    startActivity(iMainActivity);
                }else{
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(iRegister);
            }
        });
    }

}
