package com.example.sklep_pr;

import static com.example.sklep_pr.DataBase.TABLE_BRAKES;
import static com.example.sklep_pr.DataBase.TABLE_CARS;
import static com.example.sklep_pr.DataBase.TABLE_OIL;
import static com.example.sklep_pr.DataBase.TABLE_TIRES;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class Shop extends AppCompatActivity {
    private Spinner tireSpinner, brakeSpinner, oilSpinner, carSpinner;
    private TextView tireDescriptionTextView, brakeDescriptionTextView, oilDescriptionTextView, carDescriptionTextView, totalPriceTextView;
    private Button orderButton;

    private DataBase db;
    private ArrayList<String> carModels, tireModels, brakeModels, oilModels;
    private Integer[] carImages, tireImages, brakeImages, oilImages;
    private double[] carPrices, tirePrices, brakePrices, oilPrices;

    private static final String PREFS_NAME = "ShopPreferences";
    private static final String PREF_CAR_SELECTION = "carSelection";
    private static final String PREF_TIRE_SELECTION = "tireSelection";
    private static final String PREF_BRAKE_SELECTION = "brakeSelection";
    private static final String PREF_OIL_SELECTION = "oilSelection";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        db = new DataBase(this);
        carSpinner = findViewById(R.id.car_spinner);
        tireSpinner = findViewById(R.id.opony);
        brakeSpinner = findViewById(R.id.tarcze);
        oilSpinner = findViewById(R.id.olej);
        totalPriceTextView = findViewById(R.id.total_price);
        carDescriptionTextView = findViewById(R.id.car_description);
        tireDescriptionTextView = findViewById(R.id.oponyopis);
        brakeDescriptionTextView = findViewById(R.id.tarczeopis);
        oilDescriptionTextView = findViewById(R.id.olejopis);
        orderButton = findViewById(R.id.zamow);

        initializeData();

        setupCarSpinner();
        setupTireSpinner();
        setupBrakeSpinner();
        setupOilSpinner();

        loadSelections();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()== R.id.nav_home) {
                        startActivity(new Intent(Shop.this, Home.class));
                        return true;
                }
                if (item.getItemId()== R.id.nav_search) {
                    startActivity(new Intent(Shop.this, Shop.class));
                    return true;
                }
                if (item.getItemId()== R.id.nav_profile) {
                    startActivity(new Intent(Shop.this, Profile.class));
                    return true;
                }
                return false;
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Shop.this, Dane.class);
                intent.putExtra("totalPrice", totalPriceTextView.getText().toString());
                intent.putExtra("carSelection", (String) carSpinner.getSelectedItem());
                intent.putExtra("tireSelection", (String) tireSpinner.getSelectedItem());
                intent.putExtra("brakeSelection", (String) brakeSpinner.getSelectedItem());
                intent.putExtra("oilSelection", (String) oilSpinner.getSelectedItem());
                startActivity(intent);

                resetSelections();
                resetTotalPrice();
            }
        });
    }

    private void initializeData() {
        carModels = new ArrayList<>(Arrays.asList("Wybierz Samochód", "Toyota Model A", "Honda Model B", "Ford Model C", "Chevrolet Model D", "BMW Model E"));
        carImages = new Integer[]{0, R.drawable.toyota, R.drawable.honda, R.drawable.ford, R.drawable.chevrolet, R.drawable.bmw};
        carPrices = new double[]{0.0, 25000.0, 30000.0, 20000.0, 22000.0, 45000.0};

        tireModels = new ArrayList<>(Arrays.asList("Wybierz opony", "Winter Tire", "Summer Tire", "All-Season Tire", "Performance Tire", "Off-Road Tire"));
        tireImages = new Integer[]{0, R.drawable.winter, R.drawable.summer, R.drawable.allseaseon, R.drawable.perf, R.drawable.offroad};
        tirePrices = new double[]{0.0, 120.0, 100.0, 110.0, 150.0, 130.0};

        brakeModels = new ArrayList<>(Arrays.asList("Wybierz tarcze", "Disc Brake", "Drum Brake", "Ceramic Brake", "Carbon Brake", "ABS Brake"));
        brakeImages = new Integer[]{0, R.drawable.disc, R.drawable.drum, R.drawable.ceramic, R.drawable.carbon, R.drawable.abs};
        brakePrices = new double[]{0.0, 80.0, 70.0, 90.0, 150.0, 200.0};

        oilModels = new ArrayList<>(Arrays.asList("Wybierz olej", "Synthetic Oil", "Conventional Oil", "High-Mileage Oil", "Diesel Oil", "Racing Oil"));
        oilImages = new Integer[]{0, R.drawable.synthetic, R.drawable.conventional, R.drawable.millage, R.drawable.diesel, R.drawable.racing};
        oilPrices = new double[]{0.0, 40.0, 30.0, 35.0, 45.0, 50.0};
    }

    private void setupCarSpinner() {
        CarSpinnerAdapter adapter = new CarSpinnerAdapter(this, carModels, carImages);
        carSpinner.setAdapter(adapter);

        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = carModels.get(position);
                if (position != 0) {
                    double selectedPrice = carPrices[position];
                    totalPriceTextView.setText(String.valueOf(selectedPrice));
                    String description = db.getDescriptionCar(TABLE_CARS, selectedModel);
                    carDescriptionTextView.setText(description);
                } else {
                    totalPriceTextView.setText("");
                    carDescriptionTextView.setText("");
                }
                updateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                totalPriceTextView.setText("");
                carDescriptionTextView.setText("");
            }
        });
    }

    private void setupTireSpinner() {
        TireSpinnerAdapter adapter = new TireSpinnerAdapter(this, tireModels, tireImages);
        tireSpinner.setAdapter(adapter);

        tireSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = tireModels.get(position);
                if (position != 0) {
                    String description = db.getDescriptionTire(TABLE_TIRES, selectedModel);
                    tireDescriptionTextView.setText(description);
                } else {
                    tireDescriptionTextView.setText("");
                }
                updateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tireDescriptionTextView.setText("");
            }
        });
    }

    private void setupBrakeSpinner() {
        BrakeSpinnerAdapter adapter = new BrakeSpinnerAdapter(this, brakeModels, brakeImages);
        brakeSpinner.setAdapter(adapter);

        brakeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = brakeModels.get(position);
                if (position != 0) {
                    String description = db.getDescriptionBrake(TABLE_BRAKES, selectedModel);
                    brakeDescriptionTextView.setText(description);
                } else {
                    brakeDescriptionTextView.setText("");
                }
                updateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                brakeDescriptionTextView.setText("");
            }
        });
    }

    private void setupOilSpinner() {
        OilSpinnerAdapter adapter = new OilSpinnerAdapter(this, oilModels, oilImages);
        oilSpinner.setAdapter(adapter);

        oilSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = oilModels.get(position);
                if (position != 0) {
                    String description = db.getDescriptionOil(TABLE_OIL, selectedModel);
                    oilDescriptionTextView.setText(description);
                } else {
                    oilDescriptionTextView.setText("");
                }
                updateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                oilDescriptionTextView.setText("");
            }
        });
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;

        int carPosition = carSpinner.getSelectedItemPosition();
        int tirePosition = tireSpinner.getSelectedItemPosition();
        int brakePosition = brakeSpinner.getSelectedItemPosition();
        int oilPosition = oilSpinner.getSelectedItemPosition();

        if (carPosition != 0) {
            totalPrice += carPrices[carPosition];
        }
        if (tirePosition != 0) {
            totalPrice += tirePrices[tirePosition];
        }
        if (brakePosition != 0) {
            totalPrice += brakePrices[brakePosition];
        }
        if (oilPosition != 0) {
            totalPrice += oilPrices[oilPosition];
        }

        totalPriceTextView.setText(String.valueOf(totalPrice));
    }

    private void saveSelections() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(PREF_CAR_SELECTION, carSpinner.getSelectedItemPosition());
        editor.putInt(PREF_TIRE_SELECTION, tireSpinner.getSelectedItemPosition());
        editor.putInt(PREF_BRAKE_SELECTION, brakeSpinner.getSelectedItemPosition());
        editor.putInt(PREF_OIL_SELECTION, oilSpinner.getSelectedItemPosition());

        editor.apply();
    }

    private void loadSelections() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        try {
            int carSelection = prefs.getInt(PREF_CAR_SELECTION, 0);
            int tireSelection = prefs.getInt(PREF_TIRE_SELECTION, 0);
            int brakeSelection = prefs.getInt(PREF_BRAKE_SELECTION, 0);
            int oilSelection = prefs.getInt(PREF_OIL_SELECTION, 0);
            carSpinner.setSelection(carSelection);
            tireSpinner.setSelection(tireSelection);
            brakeSpinner.setSelection(brakeSelection);
            oilSpinner.setSelection(oilSelection);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSelections();
    }

    private void resetSelections() {
        carSpinner.setSelection(0);
        tireSpinner.setSelection(0);
        brakeSpinner.setSelection(0);
        oilSpinner.setSelection(0);
    }

    private void resetTotalPrice() {
        totalPriceTextView.setText("0.0");
    }
}