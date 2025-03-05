package com.example.pharm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    // Database Name and Version
    private static final String DATABASE_NAME = "pharma.db";
    private static final int DATABASE_VERSION = 41;


    //Cart


    // User Login Table
    public static final String TABLE_USER_LOGIN = "user_login";
    public static final String COLUMN_USERID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Medicine Table
    public static final String TABLE_MEDICINES = "medicines";
    public static final String COLUMN_MEDICINE_ID = "medicine_id";
    public static final String COLUMN_MEDICINE_NAME = "medicine_name";
    public static final String COLUMN_MEDICINE_TYPE = "medicine_type";
    public static final String COLUMN_MEDICINE_QUANTITY = "medicine_quantity";
    public static final String COLUMN_MEDICINE_PRICE = "medicine_price";
    public static final String COLUMN_MEDICINE_EXPIRY_DATE = "medicine_expiry_date";
    public static final String COLUMN_MEDICINE_DESCRIPTION = "medicine_description";

    // Pharmacy Table
    public static final String TABLE_PHARMACIES = "pharmacies";
    public static final String COLUMN_PHARMACY_ID = "pharmacy_id";
    public static final String COLUMN_PHARMACY_NAME = "pharmacy_name";
    public static final String COLUMN_PHARMACY_PHONE = "pharmacy_phone";
    public static final String COLUMN_PHARMACY_EMAIL = "pharmacy_email";
    public static final String COLUMN_PHARMACY_ADDRESS = "pharmacy_address";
    public static final String COLUMN_OPENING_TIME = "opening_time";
    public static final String COLUMN_CLOSING_TIME = "closing_time";
    public static final String COLUMN_PHARMACY_LATITUDE = "pharmacy_latitude";
    public static final String COLUMN_PHARMACY_LONGITUDE = "pharmacy_longitude";

    // Pharmacy-Medicine Mapping Table
    public static final String TABLE_PHARMACY_MEDICINE_MAPPING = "pharmacy_medicine_mapping";
    public static final String COLUMN_MAPPING_ID = "mapping_id";
    public static final String COLUMN_MAPPING_PHARMACY_ID = "pharmacy_id";
    public static final String COLUMN_MAPPING_MEDICINE_ID = "medicine_id";
    public static final String COLUMN_MAPPING_MEDICINE_QUANTITY = "medicine_quantity";
    public static final String COLUMN_MAPPING_AVAILABILITY = "availability";
    public static final String COLUMN_MAPPING_LAST_UPDATED = "last_updated";

    // Order Medicine Table
    public static final String TABLE_ORDER_MEDICINE = "order_medicine";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ORDER_PHARMACY_ID = "pharmacy_id";
    public static final String COLUMN_ORDER_MEDICINE_ID = "medicine_id";
    public static final String COLUMN_ORDER_PHARMACY_NAME = "pharmacy_name";
    public static final String COLUMN_ORDER_MEDICINE_NAME = "medicine_name"; // Add this line
    public static final String COLUMN_ORDER_NAME = "name";
    public static final String COLUMN_ORDER_PHONE = "phone";
    public static final String COLUMN_ORDER_EMAIL = "email";
    public static final String COLUMN_ORDER_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "total_price";
    public static final String COLUMN_ORDER_ADDRESS = "address";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_STATUS = "status";


    // Cart Table
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "cart_id";
    public static final String COLUMN_CART_USER_ID = "user_id";
    public static final String COLUMN_CART_MEDICINE_ID = "medicine_id";
    public static final String COLUMN_CART_PHARMACY_ID = "pharmacy_id";
    public static final String COLUMN_CART_QUANTITY = "quantity";


    // SQL Statement to Create Cart Table
    String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + " (" +
            COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CART_USER_ID + " INTEGER NOT NULL, " +
            COLUMN_CART_MEDICINE_ID + " INTEGER NOT NULL, " +
            COLUMN_CART_PHARMACY_ID + " INTEGER NOT NULL, " +
            COLUMN_CART_QUANTITY + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_CART_USER_ID + ") REFERENCES " + TABLE_USER_LOGIN + "(" + COLUMN_USERID + "), " +
            "FOREIGN KEY (" + COLUMN_CART_MEDICINE_ID + ") REFERENCES " + TABLE_MEDICINES + "(" + COLUMN_MEDICINE_ID + "), " +
            "FOREIGN KEY (" + COLUMN_CART_PHARMACY_ID + ") REFERENCES " + TABLE_PHARMACIES + "(" + COLUMN_PHARMACY_ID + "));";


    // SQL Statement to Create User Login Table
    String CREATE_TABLE_USER_LOGIN = "CREATE TABLE " + TABLE_USER_LOGIN + " (" +
            COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT NOT NULL, " +
            COLUMN_PHONE + " TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PASSWORD + " TEXT NOT NULL);";

    // SQL Statement to Create Medicines Table
    String CREATE_TABLE_MEDICINES = "CREATE TABLE " + TABLE_MEDICINES + " (" +
            COLUMN_MEDICINE_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_MEDICINE_NAME + " TEXT NOT NULL, " +
            COLUMN_MEDICINE_TYPE + " TEXT NOT NULL, " +
            COLUMN_MEDICINE_QUANTITY + " INTEGER NOT NULL, " +
            COLUMN_MEDICINE_PRICE + " REAL NOT NULL, " +
            COLUMN_MEDICINE_EXPIRY_DATE + " TEXT NOT NULL, " +
            COLUMN_MEDICINE_DESCRIPTION + " TEXT NOT NULL);";

    // SQL Statement to Create Pharmacies Table
    String CREATE_TABLE_PHARMACIES = "CREATE TABLE " + TABLE_PHARMACIES + " (" +
            COLUMN_PHARMACY_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_PHARMACY_NAME + " TEXT NOT NULL, " +
            COLUMN_PHARMACY_PHONE + " TEXT NOT NULL, " +
            COLUMN_PHARMACY_EMAIL + " TEXT NOT NULL, " +
            COLUMN_PHARMACY_ADDRESS + " TEXT NOT NULL, " +
            COLUMN_OPENING_TIME + " TEXT NOT NULL, " +
            COLUMN_CLOSING_TIME + " TEXT NOT NULL, " +
            COLUMN_PHARMACY_LATITUDE + " REAL, " +
            COLUMN_PHARMACY_LONGITUDE + " REAL );";

    // SQL statement to Create Pharmacy medicine mapping table
    String CREATE_TABLE_PHARMACY_MEDICINE_MAPPING = "CREATE TABLE " + TABLE_PHARMACY_MEDICINE_MAPPING + " (" +
            COLUMN_MAPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PHARMACY_ID + " INTEGER NOT NULL, " +
            COLUMN_MEDICINE_ID + " INTEGER NOT NULL, " +
            COLUMN_MEDICINE_QUANTITY + " INTEGER NOT NULL, " +
            COLUMN_MAPPING_AVAILABILITY + " INTEGER NOT NULL, " +
            COLUMN_MAPPING_LAST_UPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (" + COLUMN_MAPPING_PHARMACY_ID + ") REFERENCES " + TABLE_PHARMACIES + "(" + COLUMN_PHARMACY_ID + "), " +
            "FOREIGN KEY (" + COLUMN_MAPPING_MEDICINE_ID + ") REFERENCES " + TABLE_MEDICINES + "(" + COLUMN_MEDICINE_ID + "));";

    // SQL Statement to Create Order Medicine Table

    // Inside DatabaseHelper.java
    String CREATE_TABLE_ORDER_MEDICINE = "CREATE TABLE " + TABLE_ORDER_MEDICINE + " (" +
            COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ORDER_USER_ID + " INTEGER NOT NULL, " +
            COLUMN_ORDER_PHARMACY_ID + " INTEGER NOT NULL, " +
            COLUMN_ORDER_MEDICINE_ID + " INTEGER NOT NULL, " +
            COLUMN_ORDER_PHARMACY_NAME + " TEXT NOT NULL, " +
            COLUMN_ORDER_MEDICINE_NAME + " TEXT NOT NULL, " + // Add this line
            COLUMN_ORDER_NAME + " TEXT NOT NULL, " +
            COLUMN_ORDER_PHONE + " TEXT NOT NULL, " +
            COLUMN_ORDER_EMAIL + " TEXT NOT NULL, " +
            COLUMN_ORDER_QUANTITY + " INTEGER NOT NULL, " +
            COLUMN_ORDER_TOTAL_PRICE + " REAL NOT NULL, " +
            COLUMN_ORDER_ADDRESS + " TEXT NOT NULL, " +
            COLUMN_ORDER_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_ORDER_STATUS + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USER_LOGIN + "(" + COLUMN_USERID + "), " +
            "FOREIGN KEY (" + COLUMN_ORDER_PHARMACY_ID + ") REFERENCES " + TABLE_PHARMACIES + "(" + COLUMN_PHARMACY_ID + "), " +
            "FOREIGN KEY (" + COLUMN_ORDER_MEDICINE_ID + ") REFERENCES " + TABLE_MEDICINES + "(" + COLUMN_MEDICINE_ID + "));";

    private Context context;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // SQL Statement to Create Cart Table


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_LOGIN);
        db.execSQL(CREATE_TABLE_MEDICINES);
        db.execSQL(CREATE_TABLE_PHARMACIES);
        db.execSQL(CREATE_TABLE_PHARMACY_MEDICINE_MAPPING);
        db.execSQL(CREATE_TABLE_ORDER_MEDICINE);
        db.execSQL(CREATE_TABLE_CART); // Create the cart table

        Log.d("DB_CREATION", "Tables created successfully!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 41) {
            // Drop existing tables (if necessary)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_LOGIN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHARMACIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHARMACY_MEDICINE_MAPPING);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_MEDICINE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART); // Drop the cart table


            // Recreate tables
            onCreate(db);
        }
    }

    //Cart
    // Add item to cart
    public boolean addToCart(int userId, int medicineId, int pharmacyId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_USER_ID, userId);
        values.put(COLUMN_CART_MEDICINE_ID, medicineId);
        values.put(COLUMN_CART_PHARMACY_ID, pharmacyId);
        values.put(COLUMN_CART_QUANTITY, quantity);

        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }

    // Get cart items for a user and pharmacy
    // Get cart items for a user and pharmacy
    public List<CartItem> getCartItems(int userId, int pharmacyId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Modified query to include pharmacy name
        String query = "SELECT c." + COLUMN_CART_MEDICINE_ID + ", m." + COLUMN_MEDICINE_NAME + ", m." + COLUMN_MEDICINE_PRICE +
                ", c." + COLUMN_CART_QUANTITY + ", p." + COLUMN_PHARMACY_NAME +
                " FROM " + TABLE_CART + " c " +
                " JOIN " + TABLE_MEDICINES + " m ON c." + COLUMN_CART_MEDICINE_ID + " = m." + COLUMN_MEDICINE_ID +
                " JOIN " + TABLE_PHARMACIES + " p ON c." + COLUMN_CART_PHARMACY_ID + " = p." + COLUMN_PHARMACY_ID +
                " WHERE c." + COLUMN_CART_USER_ID + " = ? AND c." + COLUMN_CART_PHARMACY_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(pharmacyId)});

        if (cursor.moveToFirst()) {
            do {
                int medicineId = cursor.getInt(0); // Retrieve medicineId
                String selectedMedicineName = cursor.getString(1);
                double pricePerUnit = cursor.getDouble(2);
                int quantity = cursor.getInt(3);
                String pharmacyName = cursor.getString(4); // Retrieve pharmacy name

                // Create CartItem with medicineId and pharmacy name
                cartItems.add(new CartItem(selectedMedicineName, pricePerUnit, quantity, medicineId, pharmacyName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return cartItems;
    }


    // Remove item from cart
    public boolean removeFromCart(int userId, int medicineId, int pharmacyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CART, COLUMN_CART_USER_ID + " = ? AND " + COLUMN_CART_MEDICINE_ID + " = ? AND " + COLUMN_CART_PHARMACY_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(medicineId), String.valueOf(pharmacyId)}) > 0;
    }


    // Clear cart for a user and pharmacy
    public boolean clearCart(int userId, int pharmacyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CART, COLUMN_CART_USER_ID + " = ? AND " + COLUMN_CART_PHARMACY_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(pharmacyId)}) > 0;
    }


    // Pharmacy get
    public List<PharmacyItem> getAllPharmaciesList() {
        List<PharmacyItem> pharmacies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHARMACIES, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHARMACY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHARMACY_NAME));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHARMACY_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHARMACY_EMAIL));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHARMACY_ADDRESS));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PHARMACY_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PHARMACY_LONGITUDE));
                pharmacies.add(new PharmacyItem(id, name, contact, email, address, latitude, longitude));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pharmacies;
    }


    // User Login Methods (No changes needed)
    public boolean isUsernameAvailable(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_LOGIN + " WHERE " + COLUMN_USERNAME + " =? ", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return !exists;
    }

    //email exists
    public boolean isEmailAvailable(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_LOGIN, new String[]{COLUMN_EMAIL}, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return !exists;
    }

    // check mail exists
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_login WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean registerUser(String username, String phone, String email, String password) {
        if (!isUsernameAvailable(username)) {
            return false;
        }

        if (!isEmailAvailable(email)) {
            return false; // Email already exists
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USER_LOGIN, null, values);
        return result != -1; // Return true if insertion is successful
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USER_LOGIN + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}
        );
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Get Password by Username
    public String getPasswordByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get the password for a given username
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USER_LOGIN + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            // Get the password from the cursor, using the correct column index
            int passwordColumnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            if (passwordColumnIndex != -1) {
                String password = cursor.getString(passwordColumnIndex);
                cursor.close();
                return password; // Return the password if column is found
            }
        }

        // Close the cursor and return null if no password is found or query fails
        if (cursor != null) {
            cursor.close();
        }
        return null; // Return null if username not found or there was an error
    }

    // get pasword by email
    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM user_login WHERE email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            String password = cursor.getString(0);
            cursor.close();
            return password;
        }
        cursor.close();
        return "Not Found";
    }


    // Medicine Management Methods

    // Retrieve medicine by name
    public Cursor getMedicineByName(String medicineName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM medicines WHERE medicine_name=?", new String[]{medicineName});
    }


    // Insert Medicine
    public boolean addMedicine(String medicine_id, String name, String type, String quantity, String price, String expiryDate, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_ID, medicine_id);
        values.put(COLUMN_MEDICINE_NAME, name);
        values.put(COLUMN_MEDICINE_TYPE, type);
        values.put(COLUMN_MEDICINE_QUANTITY, quantity);
        values.put(COLUMN_MEDICINE_PRICE, price);
        values.put(COLUMN_MEDICINE_EXPIRY_DATE, expiryDate);
        values.put(COLUMN_MEDICINE_DESCRIPTION, description);

        // Insert the medicine record and check if the result is -1 (which means failure)
        long result = db.insert(TABLE_MEDICINES, null, values);
        return result != -1;
    }

    // Retrieve All Medicines
    public Cursor getAllMedicines() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEDICINES, null);
    }

    // Map medicine to pharmacy
    public boolean mapMedicineToPharmacy(int pharmacyId, int medicineId, int availability, int medicine_quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pharmacy_id", pharmacyId);
        values.put("medicine_id", medicineId);
        values.put("availability", availability);
        values.put("medicine_quantity", medicine_quantity); // Store fetched quantity

        long result = db.insert("pharmacy_medicine_mapping", null, values);
        return result != -1; // Returns true if insert is successful
    }


    // Retrieve medicines by id
    public Cursor getMedicinesById(String medicineId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM medicines WHERE medicine_id = ?", new String[]{String.valueOf(medicineId)});
    }


    // Retrieve medicine quantity by id
    public int getMedicineQuantity(int medicineId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT medicine_quantity FROM medicines WHERE medicine_id=?", new String[]{String.valueOf(medicineId)});
        int medicine_quantity = 0;

        if (cursor.moveToFirst()) {
            medicine_quantity = cursor.getInt(0); // Get the quantity from the column
        }
        cursor.close();
        return medicine_quantity;
    }


    // Update Medicine
    public int updateMedicine(int id, String name, String type, int quantity, double price, String expiryDate, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_NAME, name);
        values.put(COLUMN_MEDICINE_TYPE, type);
        values.put(COLUMN_MEDICINE_QUANTITY, quantity);
        values.put(COLUMN_MEDICINE_PRICE, price);
        values.put(COLUMN_MEDICINE_EXPIRY_DATE, expiryDate);
        values.put(COLUMN_MEDICINE_DESCRIPTION, description);

        return db.update(TABLE_MEDICINES, values, COLUMN_MEDICINE_ID + "=?", new String[]{String.valueOf(id)});
    }


    // Delete Medicine
    public int deleteMedicine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MEDICINES, COLUMN_MEDICINE_ID + "=?", new String[]{String.valueOf(id)});
    }


    // Pharmacy Management Methods

    //ADD Pharmacy
    public boolean addPharmacy(int id, String name, String phone, String email, String address, String openingTime, String closingTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PHARMACY_ID, id);
        values.put(COLUMN_PHARMACY_NAME, name);
        values.put(COLUMN_PHARMACY_PHONE, phone);
        values.put(COLUMN_PHARMACY_EMAIL, email);
        values.put(COLUMN_PHARMACY_ADDRESS, address);
        values.put(COLUMN_OPENING_TIME, openingTime);
        values.put(COLUMN_CLOSING_TIME, closingTime);

        // Insert the medicine record and check if the result is -1 (which means failure)
        long result = db.insert(TABLE_PHARMACIES, null, values);
        return result != -1;
    }

    //Get nearby pharmacy
    public List<Pharmacy> getNearbyPharmacies(String medicineName) {
        List<Pharmacy> pharmacyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p." + COLUMN_PHARMACY_NAME + ", p." + COLUMN_PHARMACY_PHONE + ", p." + COLUMN_PHARMACY_ADDRESS +
                ", p." + COLUMN_PHARMACY_LATITUDE + ", p." + COLUMN_PHARMACY_LONGITUDE +
                " FROM " + TABLE_PHARMACIES + " p " +
                " JOIN " + TABLE_PHARMACY_MEDICINE_MAPPING + " pm ON p." + COLUMN_PHARMACY_ID + " = pm." + COLUMN_MAPPING_PHARMACY_ID +
                " JOIN " + TABLE_MEDICINES + " m ON pm." + COLUMN_MAPPING_MEDICINE_ID + " = m." + COLUMN_MEDICINE_ID +
                " WHERE m." + COLUMN_MEDICINE_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{medicineName});


        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String phone = cursor.getString(1);
                String address = cursor.getString(2);
                double latitude = cursor.getDouble(3);
                double longitude = cursor.getDouble(4);
                pharmacyList.add(new Pharmacy(name, phone, address, latitude, longitude));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pharmacyList;
    }


    // Get All Pharmacies
    public Cursor getAllPharmacies() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PHARMACIES, null);
    }


    // Retrieve pharmacies by id
    public Cursor getPharmaciesById(String pharmacyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM pharmacies WHERE pharmacy_id = ?", new String[]{String.valueOf(pharmacyId)});
    }

    // Update Pharmacy
    public int updatePharmacy(int id, String name, String phone, String email, String address, String openingTime, String closingTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHARMACY_NAME, name);
        values.put(COLUMN_PHARMACY_PHONE, phone);
        values.put(COLUMN_PHARMACY_EMAIL, email);
        values.put(COLUMN_PHARMACY_ADDRESS, address);
        values.put(COLUMN_OPENING_TIME, openingTime);
        values.put(COLUMN_CLOSING_TIME, closingTime);

        return db.update(TABLE_PHARMACIES, values, COLUMN_PHARMACY_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Delete Pharmacy
    public int deletePharmacy(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PHARMACIES, COLUMN_PHARMACY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean addPharmacyMedicineMapping(String pharmacyId, String medicineId, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHARMACY_ID, pharmacyId);
        values.put(COLUMN_MEDICINE_ID, medicineId);
        values.put(COLUMN_MEDICINE_QUANTITY, quantity);
        values.put(COLUMN_MEDICINE_PRICE, price);

        long result = db.insert(TABLE_PHARMACY_MEDICINE_MAPPING, null, values);
        return result != -1;
    }


    public Cursor getAllPharmacyMedicineMappings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PHARMACY_MEDICINE_MAPPING, null);
    }

    public int updatePharmacyMedicineMapping(int id, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_QUANTITY, quantity);
        values.put(COLUMN_MEDICINE_PRICE, price);

        return db.update(TABLE_PHARMACY_MEDICINE_MAPPING, values, COLUMN_MAPPING_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deletePharmacyMedicineMapping(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PHARMACY_MEDICINE_MAPPING, COLUMN_MAPPING_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Add Order
    // Inside DatabaseHelper.java
    // Inside DatabaseHelper.java
    public boolean addOrder(int userId, int pharmacyId, int medicineId, String pharmacyName, String medicineName, String phone, String email, int quantity, double totalPrice, String name, String status, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, userId);
        values.put(COLUMN_ORDER_PHARMACY_ID, pharmacyId);
        values.put(COLUMN_ORDER_MEDICINE_ID, medicineId);
        values.put(COLUMN_ORDER_PHARMACY_NAME, pharmacyName);
        values.put(COLUMN_ORDER_MEDICINE_NAME, medicineName); // Add medicine name
        values.put(COLUMN_ORDER_NAME, name);
        values.put(COLUMN_ORDER_PHONE, phone);
        values.put(COLUMN_ORDER_EMAIL, email);
        values.put(COLUMN_ORDER_QUANTITY, quantity);
        values.put(COLUMN_ORDER_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_ORDER_ADDRESS, address);
        values.put(COLUMN_ORDER_STATUS, status);

        long result = db.insert(TABLE_ORDER_MEDICINE, null, values);
        return result != -1;
    }

    // Inside DatabaseHelper.java
    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ORDER_MEDICINE, null);
    }

    // Fetch orders by user ID
    public Cursor getOrdersByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT order_id AS _id, * FROM " + TABLE_ORDER_MEDICINE + " WHERE " + COLUMN_ORDER_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );
    }

}
