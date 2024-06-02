package com.example.sklep_pr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLogin, editTextPassword;
    DataBase db;
    TextView login,haslo;

    @SuppressLint({"MissingInflatedId", "WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            db = new DataBase(this);
            editTextLogin = findViewById(R.id.login);
            editTextPassword = findViewById(R.id.haslo);

            Button logB= findViewById(R.id.logB);
            Button rejB = findViewById(R.id.rejB);
            login = findViewById(R.id.login);
            haslo = findViewById(R.id.haslo);


        logB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextLogin.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Please enter login and password", Toast.LENGTH_SHORT).show();
                } else {
                    boolean userExists = db.checkUser2(username, password);
                    if (userExists) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Please register", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        rejB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
