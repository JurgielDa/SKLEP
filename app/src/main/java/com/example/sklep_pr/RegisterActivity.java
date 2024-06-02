package com.example.sklep_pr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    String TAG = "222";

    Button rejB,back;
    EditText login,haslo;
    private DataBase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        rejB = findViewById(R.id.rejB);
        login = findViewById(R.id.login);
        haslo=findViewById(R.id.haslo);
        db = new DataBase(this);
        back = findViewById(R.id.back);

        rejB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = login.getText().toString();
                String password = haslo.getText().toString();
                Log.v(TAG,username+""+password);

                if (db.checkUser(username)) {
                    Toast.makeText(RegisterActivity.this, "Użytkownik o takim loginie już istnieje", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.addUser(username, password)) {
                        Toast.makeText(RegisterActivity.this, "Stworzono", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Błąd", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
