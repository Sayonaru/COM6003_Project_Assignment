package com.example.com6003_project_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditDoctorActivity extends AppCompatActivity {

    private EditText etDoctorName, etSpecialty;
    private Button btnSaveChanges;
    private DatabaseHelper databaseHelper;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);

        etDoctorName = findViewById(R.id.etDoctorName);
        etSpecialty = findViewById(R.id.etSpecialty);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        databaseHelper = new DatabaseHelper(this);

        // Get doctor data from intent
        doctorId = getIntent().getIntExtra("doctor_id", -1); // Default to -1 if nothing was passed through
        String doctorName = getIntent().getStringExtra("doctor_name");
        String specialty = getIntent().getStringExtra("specialty");

        etDoctorName.setText(doctorName);
        etSpecialty.setText(specialty);

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        // Get updated values from input fields
        String updatedDoctorName = etDoctorName.getText().toString().trim();
        String updatedSpecialty = etSpecialty.getText().toString().trim();

        // Validate fields
        if (updatedDoctorName.isEmpty() || updatedSpecialty.isEmpty()) {
            Toast.makeText(this, "Both fields are required!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditDoctorActivity.this, DoctorManagementActivity.class);
            startActivity(intent);
        }

        // Update the database
        boolean isUpdated = databaseHelper.updateDoctor(doctorId, updatedDoctorName, updatedSpecialty);

        if (isUpdated) {
            Toast.makeText(this, "Doctor details updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update doctor details.", Toast.LENGTH_SHORT).show();
        }
    }
}
