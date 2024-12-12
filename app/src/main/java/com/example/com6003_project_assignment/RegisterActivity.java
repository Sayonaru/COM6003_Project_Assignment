package com.example.com6003_project_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialise views
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);


        // Initialise the database helper, helps catch initialisation errors that occured during startup
        try {
            dbHelper = new DatabaseHelper(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initialising database", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Pressing the register button validates data before data entry and reroute
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Login page redirect
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Check for empty fields
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password
        String passwordValidation = validatePassword(password);
        if (!passwordValidation.equals("Valid")) {
            Toast.makeText(this, passwordValidation, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username already exists
        if (dbHelper.isUsernameTaken(username)) {
            Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert user into the database
        boolean success = dbHelper.addUser(email, username, password);
        if (success) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith(".com");
    }

    private String validatePassword(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        if (!Pattern.compile("[a-zA-Z]").matcher(password).find()) {
            return "Password must contain at least one letter";
        }
        if (!Pattern.compile("\\d").matcher(password).find()) {
            return "Password must contain at least one number";
        }
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            return "Password must contain at least one special character";
        }
        return "Valid";
    }
}

