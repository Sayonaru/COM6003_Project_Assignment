package com.example.com6003_project_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import android.util.Base64;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 6; // Increments as major database changes occur in development

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    // Patient Table
    private static final String TABLE_PATIENTS = "patients";
    private static final String COL_PATIENT_ID = "patient_id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_DOB = "dob";
    private static final String COL_GENDER = "gender";

    // Doctor Table
    private static final String TABLE_DOCTORS = "doctors";
    private static final String COL_DOCTOR_ID = "doctor_id";
    private static final String COL_DOCTOR_NAME = "doctor_name";
    private static final String COL_SPECIALTY = "specialty";

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

        String createPatientsTable = "CREATE TABLE " + TABLE_PATIENTS + " ("
                + COL_PATIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FIRST_NAME + " TEXT NOT NULL, "
                + COL_LAST_NAME + " TEXT NOT NULL, "
                + COL_DOB + " TEXT NOT NULL, "
                + COL_GENDER + " TEXT NOT NULL)";
        db.execSQL(createPatientsTable);

        String createDoctorsTable = "CREATE TABLE " + TABLE_DOCTORS + " ("
                + COL_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DOCTOR_NAME + " TEXT NOT NULL, "
                + COL_SPECIALTY + " TEXT NOT NULL)";
        db.execSQL(createDoctorsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
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

    private String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
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

    public boolean addPatient(String firstName, String lastName, String dob, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_FIRST_NAME, firstName);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_DOB, dob);
        values.put(COL_GENDER, gender);

        long result = db.insert(TABLE_PATIENTS, null, values);

        if(result == -1) {
            Log.e("DB_ERROR", "Failed to insert patient.");
            return false;
        } else {
            Log.d("DB_SUCCESS", "Patient added with ID: " + result);
            return true;
        }
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

    public boolean deletePatient(int patientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PATIENTS, COL_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)});
        return rowsAffected > 0;
    }

    public ArrayList<Patient> getAllPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Patient patient = new Patient(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_PATIENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DOB)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_GENDER))
                );
                patients.add(patient);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return patients;
    }

    public boolean updatePatient(int patientId, String firstName, String lastName, String dob, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FIRST_NAME, firstName);
        contentValues.put(COL_LAST_NAME, lastName);
        contentValues.put(COL_DOB, dob);
        contentValues.put(COL_GENDER, gender);

        int rowsUpdated = db.update(TABLE_PATIENTS, contentValues, COL_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)});
        return rowsUpdated > 0; // Return true if at least one row was updated
    }

    public boolean addDoctor(String doctorName, String specialty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_DOCTOR_NAME, doctorName);
        values.put(COL_SPECIALTY, specialty);

        long result = db.insert(TABLE_DOCTORS, null, values);

        if (result == -1) {
            Log.e("DB_ERROR", "Failed to insert doctor.");
            return false;
        } else {
            Log.d("DB_SUCCESS", "Doctor added with ID: " + result);
            return true;
        }
    }

    public ArrayList<Doctor> getAllDoctors() {
        ArrayList<Doctor> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_DOCTOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCTOR_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SPECIALTY))
                );
                doctorList.add(doctor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return doctorList;
    }

    public boolean updateDoctor(int doctorId, String doctorName, String specialty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DOCTOR_NAME, doctorName);
        contentValues.put(COL_SPECIALTY, specialty);

        int rowsUpdated = db.update(TABLE_DOCTORS, contentValues, COL_DOCTOR_ID + " = ?", new String[]{String.valueOf(doctorId)});
        return rowsUpdated > 0; // Return true if at least one row was updated
    }

    public boolean deleteDoctor(int doctorId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_DOCTORS, COL_DOCTOR_ID + " = ?", new String[]{String.valueOf(doctorId)});
        return rowsAffected > 0;
    }
}
