package com.example.com6003_project_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddDoctorActivity extends AppCompatActivity {

    private EditText etName, etSpecialty;
    private Button btnAddDoctor;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor); // Make sure the layout is set to your doctor add layout

        etName = findViewById(R.id.etName);
        etSpecialty = findViewById(R.id.etSpecialty);
        btnAddDoctor = findViewById(R.id.btnAddDoctor);

        dbHelper = new DatabaseHelper(this);

        btnAddDoctor.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String specialty = etSpecialty.getText().toString().trim();

            // Validate input
            if (name.isEmpty() || specialty.isEmpty()) {
                Toast.makeText(AddDoctorActivity.this, "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = dbHelper.addDoctor(name, specialty);
                if (isInserted) {
                    Toast.makeText(AddDoctorActivity.this, "Doctor added successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddDoctorActivity.this, DoctorManagementActivity.class); // Update this to navigate to your doctor management page
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddDoctorActivity.this, "Failed to add doctor.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
