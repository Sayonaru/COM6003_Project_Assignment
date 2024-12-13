package com.example.com6003_project_assignment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PatientManagementActivity extends AppCompatActivity {

    private ListView lvPatients;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> patientList;
    private ArrayList<Integer> patientIds;
    private DatabaseHelper databaseHelper;
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_management);

        databaseHelper = new DatabaseHelper(this);

        lvPatients = findViewById(R.id.lvPatients);
        btnAddUser = findViewById(R.id.btnAddUser);
        patientList = new ArrayList<>();
        patientIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patientList);
        lvPatients.setAdapter(adapter);

        loadPatients();

        // On short press of patient in listview, bring up the edit page
        lvPatients.setOnItemClickListener((parent, view, position, id) -> {
            int selectedPatientId = patientIds.get(position);
            Intent intent = new Intent(PatientManagementActivity.this, EditPatientActivity.class);
            intent.putExtra("patient_id", selectedPatientId);
            startActivity(intent);
            finish();
        });

        // On long press of patient in listview, bring up delete dialog
        lvPatients.setOnItemLongClickListener((parent, view, position, id) -> {
            int selectedPatientId = patientIds.get(position);
            showDeleteConfirmationDialog(selectedPatientId);
            return true;
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientManagementActivity.this, AddPatientActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadPatients() {
        patientList.clear();
        patientIds.clear();

        ArrayList<Patient> patients = databaseHelper.getAllPatients();

        for (Patient patient : patients) {
            patientList.add(patient.getFirstName() + " " + patient.getLastName());
            patientIds.add(patient.getId());
        }

        adapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(int patientId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Patient")
                .setMessage("Are you sure you want to delete this patient?")
                .setPositiveButton("Yes", (dialog, which) -> deletePatient(patientId))
                .setNegativeButton("No", null)
                .show();
    }

    private void deletePatient(int patientId) {
        boolean success = databaseHelper.deletePatient(patientId);
        if (success) {
            Toast.makeText(this, "Patient deleted successfully", Toast.LENGTH_SHORT).show();
            loadPatients(); // Call again to reload the listview
        } else {
            Toast.makeText(this, "Failed to delete patient", Toast.LENGTH_SHORT).show();
        }
    }
}

