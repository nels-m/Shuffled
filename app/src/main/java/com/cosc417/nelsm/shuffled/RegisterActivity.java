package com.cosc417.nelsm.shuffled;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText tUsername;
    EditText tPassword;
    EditText tCnfPassword;
    Button bRegister;
    TextView tLogin;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);
        tUsername = (EditText)findViewById(R.id.username);
        tPassword = (EditText)findViewById(R.id.password);
        tCnfPassword = (EditText)findViewById(R.id.conf_password);
        bRegister = (Button)findViewById(R.id.btn_register);
        tLogin = (TextView) findViewById(R.id.t_login);

        tLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(iLogin);
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String user = tUsername.getText().toString().trim();
                String pass = tPassword.getText().toString().trim();
                String conf_pass = tCnfPassword.getText().toString().trim();
                Boolean alreadyUser = db.checkUsername(user);

                if (!alreadyUser) {
                    if (pass.equals(conf_pass)) {
                        long res = db.addUser(user, pass);

                        if (res > 0) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent backToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(backToLogin);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Problem With Registration", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(RegisterActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Username is Taken", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
