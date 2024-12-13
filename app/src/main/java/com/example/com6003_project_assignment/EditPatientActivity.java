package com.example.com6003_project_assignment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditPatientActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etDoB, etGender;
    private Button btnSaveChanges;
    private DatabaseHelper databaseHelper;
    private int year, month, day;
    private int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDoB = findViewById(R.id.etDoB);
        etGender = findViewById(R.id.etGender);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        databaseHelper = new DatabaseHelper(this);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        etDoB.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditPatientActivity.this, // Making the date picker appear when the edittext field is pressed for easier access and guaranteeing validity
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // Set the selected date to the EditText field
                        monthOfYear = monthOfYear + 1; // Month is 0-based
                        String date = dayOfMonth + "/" + monthOfYear + "/" + year;
                        etDoB.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Get patient data from intent
        patientId = getIntent().getIntExtra("patient_id", -1); // Default to -1 if nothing was passed through
        String firstName = getIntent().getStringExtra("first_name");
        String lastName = getIntent().getStringExtra("last_name");
        String dob = getIntent().getStringExtra("dob");
        String gender = getIntent().getStringExtra("gender");

        // Autofill the fields with patient data ready to be edited
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etDoB.setText(dob);
        etGender.setText(gender);

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        // Get updated values from input fields
        String updatedFirstName = etFirstName.getText().toString().trim();
        String updatedLastName = etLastName.getText().toString().trim();
        String updatedDob = etDoB.getText().toString().trim();
        String updatedGender = etGender.getText().toString().trim();

        // Validate fields
        if (updatedFirstName.isEmpty() || updatedLastName.isEmpty() || updatedDob.isEmpty() || updatedGender.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the database
        boolean isUpdated = databaseHelper.updatePatient(patientId, updatedFirstName, updatedLastName, updatedDob, updatedGender);

        if (isUpdated) {
            Toast.makeText(this, "Patient details updated successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditPatientActivity.this, PatientManagementActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to update patient details.", Toast.LENGTH_SHORT).show();
        }
    }
}
