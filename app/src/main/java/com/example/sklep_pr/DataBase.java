package com.example.sklep_pr;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_CAR_ID = "car_id";
    public static final String COLUMN_CAR_NAME = "car_name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String TABLE_TIRES = "tires";
    public static final String COLUMN_TIRE_ID = "tire_id";
    public static final String COLUMN_TIRE_NAME = "tire_name";
    public static final String COLUMN_TIRE_DESCRIPTION = "description";
    public static final String COLUMN_TIRE_PRICE = "price";
    public static final String TABLE_BRAKES = "brakes";
    public static final String COLUMN_BRAKE_ID = "brake_id";
    public static final String COLUMN_BRAKE_NAME = "brake_name";
    public static final String COLUMN_BRAKE_DESCRIPTION = "description";
    public static final String COLUMN_BRAKE_PRICE = "price";
    public static final String TABLE_OIL = "oil";
    public static final String COLUMN_OIL_ID = "oil_id";
    public static final String COLUMN_OIL_NAME = "oil_name";
    public static final String COLUMN_OIL_DESCRIPTION = "description";
    public static final String COLUMN_OIL_PRICE = "price";
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_DETAILS = "order_details";
    public static final String COLUMN_ORDER_USERNAME = "username";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_NAME = "order_name";
    public static final String COLUMN_ORDER_SURNAME = "order_surname";
    public static final String COLUMN_ORDER_PRICE = "order_price";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + COLUMN_CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CAR_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PRICE + " REAL" + ")";
        db.execSQL(CREATE_CARS_TABLE);

        String CREATE_TIRES_TABLE = "CREATE TABLE " + TABLE_TIRES + "("
                + COLUMN_TIRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TIRE_NAME + " TEXT,"
                + COLUMN_TIRE_DESCRIPTION + " TEXT,"
                + COLUMN_TIRE_PRICE + " REAL" + ")";
        db.execSQL(CREATE_TIRES_TABLE);

        String CREATE_BRAKES_TABLE = "CREATE TABLE " + TABLE_BRAKES + "("
                + COLUMN_BRAKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BRAKE_NAME + " TEXT,"
                + COLUMN_BRAKE_DESCRIPTION + " TEXT,"
                + COLUMN_BRAKE_PRICE + " REAL" + ")";
        db.execSQL(CREATE_BRAKES_TABLE);

        String CREATE_OIL_TABLE = "CREATE TABLE " + TABLE_OIL + "("
                + COLUMN_OIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_OIL_NAME + " TEXT,"
                + COLUMN_OIL_DESCRIPTION + " TEXT,"
                + COLUMN_OIL_PRICE + " REAL" + ")";
        db.execSQL(CREATE_OIL_TABLE);

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_USERNAME + " TEXT,"
                + COLUMN_ORDER_DETAILS + " TEXT,"
                + COLUMN_ORDER_DATE + " TEXT,"
                + COLUMN_ORDER_NAME + " TEXT,"
                + COLUMN_ORDER_SURNAME + " TEXT,"
                + COLUMN_ORDER_PRICE + " REAL " + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

        insertSampleCarData(db);
        insertSampleTireData(db);
        insertSampleBrakeData(db);
        insertSampleOilData(db);
    }

    public String getUserImageUrl(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String imageUrl = null;
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_IMAGE_URL},
                COLUMN_USERNAME + " = ?", new String[]{username},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
            cursor.close();
        }

        return imageUrl;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_IMAGE_URL + " TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE " + TABLE_CARS + " (" + COLUMN_CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CAR_NAME + " TEXT)");
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_CARS + " ADD COLUMN " + COLUMN_DESCRIPTION + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_CARS + " ADD COLUMN " + COLUMN_PRICE + " REAL");
        }
        if (oldVersion < 5) {
            db.execSQL("CREATE TABLE " + TABLE_TIRES + " (" +
                    COLUMN_TIRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TIRE_NAME + " TEXT," +
                    COLUMN_TIRE_DESCRIPTION + " TEXT," +
                    COLUMN_TIRE_PRICE + " REAL)");
            db.execSQL("CREATE TABLE " + TABLE_BRAKES + " (" +
                    COLUMN_BRAKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_BRAKE_NAME + " TEXT," +
                    COLUMN_BRAKE_DESCRIPTION + " TEXT," +
                    COLUMN_BRAKE_PRICE + " REAL)");
            db.execSQL("CREATE TABLE " + TABLE_OIL + " (" +
                    COLUMN_OIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_OIL_NAME + " TEXT," +
                    COLUMN_OIL_DESCRIPTION + " TEXT," +
                    COLUMN_OIL_PRICE + " REAL)");
            insertSampleTireData(db);
            insertSampleBrakeData(db);
            insertSampleOilData(db);
        }
        if (oldVersion < 6) {
            db.execSQL("ALTER TABLE " + TABLE_ORDERS + " ADD COLUMN " + COLUMN_ORDER_USERNAME + " TEXT");
        }
        if (oldVersion < 8) {
            db.execSQL("ALTER TABLE " + TABLE_ORDERS + " ADD COLUMN " + COLUMN_ORDER_PRICE + " REAL");
        }
    }

    private void insertSampleCarData(SQLiteDatabase db) {
        String[] carNames = {"Toyota Model A", "Honda Model B", "Ford Model C", "Chevrolet Model D", "BMW Model E"};
        String[] carDescriptions = {"A reliable and fuel-efficient car", "A compact car with modern features", "A robust car with excellent performance", "A family car with spacious interiors", "A luxury car with high-end features"};
        double[] carPrices = {25000.0, 30000.0, 20000.0, 22000.0, 45000.0};

        for (int i = 0; i < carNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CAR_NAME, carNames[i]);
            values.put(COLUMN_DESCRIPTION, carDescriptions[i]);
            values.put(COLUMN_PRICE, carPrices[i]);
            db.insert(TABLE_CARS, null, values);
        }
    }

    private void insertSampleTireData(SQLiteDatabase db) {
        String[] tireNames = {"Winter Tire", "Summer Tire", "All-Season Tire", "Performance Tire", "Off-Road Tire"};
        String[] tireDescriptions = {"High-performance winter tire", "Durable summer tire", "Versatile all-season tire", "High-speed performance tire", "Rugged off-road tire"};
        double[] tirePrices = {120.0, 100.0, 110.0, 150.0, 130.0};

        for (int i = 0; i < tireNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TIRE_NAME, tireNames[i]);
            values.put(COLUMN_TIRE_DESCRIPTION, tireDescriptions[i]);
            values.put(COLUMN_TIRE_PRICE, tirePrices[i]);
            db.insert(TABLE_TIRES, null, values);
        }
    }

    private void insertSampleBrakeData(SQLiteDatabase db) {
        String[] brakeNames = {"Disc Brake", "Drum Brake", "Ceramic Brake", "Carbon Brake", "ABS Brake"};
        String[] brakeDescriptions = {"High-quality disc brake", "Reliable drum brake", "Durable ceramic brake", "Lightweight carbon brake", "Advanced ABS brake system"};
        double[] brakePrices = {80.0, 70.0, 90.0, 150.0, 200.0};

        for (int i = 0; i < brakeNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BRAKE_NAME, brakeNames[i]);
            values.put(COLUMN_BRAKE_DESCRIPTION, brakeDescriptions[i]);
            values.put(COLUMN_BRAKE_PRICE, brakePrices[i]);
            db.insert(TABLE_BRAKES, null, values);
        }
    }

    private void insertSampleOilData(SQLiteDatabase db) {
        String[] oilNames = {"Synthetic Oil", "Conventional Oil", "High-Mileage Oil", "Diesel Oil", "Racing Oil"};
        String[] oilDescriptions = {"Premium synthetic motor oil", "Standard conventional oil", "Oil for high-mileage engines", "Oil for diesel engines", "High-performance racing oil"};
        double[] oilPrices = {40.0, 30.0, 35.0, 45.0, 50.0};

        for (int i = 0; i < oilNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_OIL_NAME, oilNames[i]);
            values.put(COLUMN_OIL_DESCRIPTION, oilDescriptions[i]);
            values.put(COLUMN_OIL_PRICE, oilPrices[i]);
            db.insert(TABLE_OIL, null, values);
        }
    }

    public boolean addOrderDetails(String username, String orderDetails, String price, String name, String surname, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ORDER_USERNAME, username);
        contentValues.put(COLUMN_ORDER_DETAILS, orderDetails);
        contentValues.put(COLUMN_ORDER_PRICE, price);
        contentValues.put(COLUMN_ORDER_NAME, name);
        contentValues.put(COLUMN_ORDER_SURNAME, surname);
        contentValues.put(COLUMN_ORDER_DATE, date);

        long result = db.insert(TABLE_ORDERS, null, contentValues);
        db.close();
        return result != -1;
    }

    public List<String> getOrderDetails(String username) {
        List<String> orderDetailsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS,
                new String[]{COLUMN_ORDER_DETAILS, COLUMN_ORDER_DATE, COLUMN_ORDER_NAME, COLUMN_ORDER_SURNAME, COLUMN_ORDER_PRICE},
                COLUMN_ORDER_USERNAME + "=?",
                new String[]{username},
                null, null, COLUMN_ORDER_DATE + " DESC");

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String orderDetails = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DETAILS));
            @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE));
            @SuppressLint("Range") String orderName = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_NAME));
            @SuppressLint("Range") String orderSurname = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_SURNAME));
            @SuppressLint("Range") String orderPrice = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRICE));
            orderDetailsList.add(orderDetails + " - " + orderDate + " - " + orderName + " - " + orderSurname + " - " + orderPrice);
        }
        cursor.close();
        db.close();
        return orderDetailsList;
    }

    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUser2(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password});
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }

    public boolean updateUserImageUrl(String username, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_URL, imageUrl);
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
        db.close();
        return rowsAffected > 0;
    }

    public String getOrderDetailsFromDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String orderDetails = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DETAILS));
            @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRICE));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_NAME));
            @SuppressLint("Range") String surname = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_SURNAME));
            cursor.close();
            return "Order details: " + orderDetails + ", Price: " + price + ", Name: " + name + " " + surname;
        }
        return null;
    }

    public String getDescriptionCar(String tableName, String model) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"description"}, COLUMN_CAR_NAME + " = ?", new String[]{model}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            cursor.close();
            return description;
        }
        return "Opis nie jest dostępny";
    }

    public String getDescriptionTire(String tableName, String model) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"description"}, COLUMN_TIRE_NAME + " = ?", new String[]{model}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            cursor.close();
            return description;
        }
        return "Opis nie jest dostępny";
    }

    public String getDescriptionBrake(String tableName, String model) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"description"}, COLUMN_BRAKE_NAME + " = ?", new String[]{model}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            cursor.close();
            return description;
        }
        return "Opis nie jest dostępny";
    }

    public String getDescriptionOil(String tableName, String model) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"description"}, COLUMN_OIL_NAME + " = ?", new String[]{model}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            cursor.close();
            return description;
        }
        return "Opis nie jest dostępny";
    }

}