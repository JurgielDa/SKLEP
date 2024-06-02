package com.example.sklep_pr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Dane extends AppCompatActivity {

    private EditText editName, editSurname, editEmail;
    private TextView totalPriceTextView;
    private Button buyNowButton;
    private String TAG = "sklep111";
    private static final int REQUEST_CODE_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dane_activity);

        editName = findViewById(R.id.edit_name);
        editSurname = findViewById(R.id.edit_surname);
        editEmail = findViewById(R.id.edit_email);

        totalPriceTextView = findViewById(R.id.total_price);
        buyNowButton = findViewById(R.id.buy_now_button);

        String totalPriceString = getIntent().getStringExtra("totalPrice");
        totalPriceTextView.setText(totalPriceString);

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderDetails =  getIntent().getStringExtra("carSelection") + ", "
                        + getIntent().getStringExtra("tireSelection") + ", "
                        + getIntent().getStringExtra("brakeSelection") + ", "
                        + getIntent().getStringExtra("oilSelection");
                String totalPrice = getIntent().getStringExtra("totalPrice");
                String name = editName.getText().toString();
                String surname = editSurname.getText().toString();
                String email = editEmail.getText().toString();

                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Log.v(TAG, orderDetails);

                sendEmail(email, orderDetails, totalPrice);

                Intent intent = new Intent(Dane.this, Profile.class);
                intent.putExtra("orderDetails", orderDetails);
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("name", name);
                intent.putExtra("surname", surname);
                intent.putExtra("date", currentDate);
                startActivityForResult(intent, REQUEST_CODE_PROFILE);
            }
        });
    }

    private void sendEmail(String email, String orderDetails, String totalPrice) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Twoje zamówienie");
        emailIntent.putExtra(Intent.EXTRA_TEXT, orderDetails + "\nCena całkowita: " + totalPrice);

        try {
            startActivity(Intent.createChooser(emailIntent, "Wysyłanie emaila"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Dane.this, "Brak klienta e-mail ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Uprawnienie nie zostało przyznane", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
