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

public class AddPatientActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etDoB, etGender;
    private Button btnAddPatient;
    private DatabaseHelper dbHelper;
    private int year, month, day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDoB = findViewById(R.id.etDoB);
        etGender = findViewById(R.id.etGender);
        btnAddPatient = findViewById(R.id.btnAddPatient);

        dbHelper = new DatabaseHelper(this);

        // Get the current date for default in the date picker
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        etDoB.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddPatientActivity.this, // Making the date picker appear when the edittext field is pressed for easier access and guaranteeing validity
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // Set the selected date to the EditText field
                        monthOfYear = monthOfYear + 1; // Month is 0-based
                        String date = dayOfMonth + "/" + monthOfYear + "/" + year;
                        etDoB.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String dob = etDoB.getText().toString().trim();
                String gender = etGender.getText().toString().trim();

                // Validate input
                if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(AddPatientActivity.this, "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.addPatient(firstName, lastName, dob, gender);
                    if (isInserted) {
                        Toast.makeText(AddPatientActivity.this, "Patient added successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddPatientActivity.this, PatientManagementActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddPatientActivity.this, "Failed to add patient.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

