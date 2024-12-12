package com.example.com6003_project_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import android.util.Base64;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 1;

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static SecretKey SECRET_KEY;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (SECRET_KEY == null) { // Only generate a secret key if there isn't already one, so to not mess up any decryption or matching fields
            try{
                SECRET_KEY = generateAESKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EMAIL + " TEXT, "
                + COL_USERNAME + " TEXT, "
                + COL_PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean addUser(String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(COL_EMAIL, encrypt(email, SECRET_KEY));
            values.put(COL_USERNAME, encrypt(username, SECRET_KEY));
            values.put(COL_PASSWORD, hashPassword(password));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String encryptedUsername;
        try {
            encryptedUsername = encrypt(username, SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String hashedPassword = hashPassword(password);

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ? ";

        Cursor cursor = db.rawQuery(query, new String[]{encryptedUsername, hashedPassword});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

    private String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Using SHA-256 which is most optimal for passwords of 8+ length
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String encryptedUsername;
        try {
            encryptedUsername = encrypt(username, SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Assume the username is taken if encryption fails
        }

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{encryptedUsername});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
}