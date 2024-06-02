package com.example.sklep_pr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Profile extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ImageView imageView;
    DataBase db;
    String username;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        imageView = findViewById(R.id.image_profile);
        db = new DataBase(this);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(Profile.this, Home.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    startActivity(new Intent(Profile.this, Shop.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    return true;
                }
                return false;
            }
        });

        TextView ksywaTextView = findViewById(R.id.ksywa);
        ksywaTextView.setText(username);

        Button addImageButton = findViewById(R.id.dodaj);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allPermissionsGranted()) {
                    ActivityCompat.requestPermissions(Profile.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                } else {
                    openImagePicker();
                }
            }
        });

        Button wyloguj = findViewById(R.id.wyloguj);
        wyloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String orderDetails = intent.getStringExtra("orderDetails");
        String totalPrice = intent.getStringExtra("totalPrice");
        String name = intent.getStringExtra("name");
        String surname = intent.getStringExtra("surname");
        String date = intent.getStringExtra("date");

        if (orderDetails != null && !orderDetails.isEmpty()) {
            boolean inserted = db.addOrderDetails(username, orderDetails, totalPrice, name, surname, date);
            if (inserted) {
                Toast.makeText(this, "Szczegóły zamówienia zapisane", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Błąd podczas zapisywania", Toast.LENGTH_SHORT).show();
            }
        }

        List<String> orderDetailsList = db.getOrderDetails(username);
        OrderAdapter orderDetailsAdapter = new OrderAdapter(this, orderDetailsList);
        ListView orderDetailsListView = findViewById(R.id.lista);
        orderDetailsListView.setAdapter(orderDetailsAdapter);

        loadProfileImage();
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            } else {
                openImagePicker();
            }
        }
    }

    private void openImagePicker() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String imageUrl = getPathFromURI(selectedImage);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (username != null && !username.isEmpty()) {
                    boolean isUpdated = db.updateUserImageUrl(username, imageUrl);
                    if (isUpdated) {
                        Toast.makeText(this, "Zdjęcie zaktualizowane", Toast.LENGTH_SHORT).show();
                        if (imageView != null) {
                            Glide.with(this).load(imageUrl).into(imageView);
                        } else {
                            Log.v("ProfileActivity", "imageView is null");
                        }
                    } else {
                        Toast.makeText(this, "Błąd podczas aktualizacji zdjęcia", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Nie znaleziono nazwy użytkownika", Toast.LENGTH_SHORT).show();
                }
            } else {
                imageView.setImageResource(R.drawable.profilrdb);
            }
        }
    }

    private void loadProfileImage() {
        String imageUrl = db.getUserImageUrl(username);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.profilrdb);
        }
    }

    private String getPathFromURI(Uri selectedImage) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }


}