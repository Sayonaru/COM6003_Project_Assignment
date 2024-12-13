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

public class DoctorManagementActivity extends AppCompatActivity {

    private ListView lvDoctors;
    private Button btnAddDoctor;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> doctorList;
    private ArrayList<Integer> doctorIds;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_management);

        databaseHelper = new DatabaseHelper(this);

        lvDoctors = findViewById(R.id.lvDoctors);
        btnAddDoctor = findViewById(R.id.btnAddDoctor);
        doctorList = new ArrayList<>();
        doctorIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorList);
        lvDoctors.setAdapter(adapter);

        loadDoctors();

        btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorManagementActivity.this, AddDoctorActivity.class);
                startActivity(intent);
            }
        });

        lvDoctors.setOnItemClickListener((parent, view, position, id) -> {
            int selectedDoctorId = doctorIds.get(position);
            Intent intent = new Intent(DoctorManagementActivity.this, EditDoctorActivity.class);
            intent.putExtra("doctor_id", selectedDoctorId);
            startActivity(intent);
            finish();
        });

        lvDoctors.setOnItemLongClickListener((parent, view, position, id) -> {
            int selectedDoctorId = doctorIds.get(position);
            showDeleteConfirmationDialog(selectedDoctorId);
            return true;
        });
    }

    private void loadDoctors() {
        doctorList.clear();
        doctorIds.clear();

        ArrayList<Doctor> doctors = databaseHelper.getAllDoctors();
        for (Doctor doctor : doctors) {
            doctorList.add(doctor.getName() + " - " + doctor.getSpecialty());
            doctorIds.add(doctor.getId());
        }

        adapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(int doctorId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Doctor")
                .setMessage("Are you sure you want to delete this doctor?")
                .setPositiveButton("Yes", (dialog, which) -> deleteDoctor(doctorId))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteDoctor(int doctorId) {
        boolean success = databaseHelper.deleteDoctor(doctorId);
        if (success) {
            Toast.makeText(this, "Doctor deleted successfully", Toast.LENGTH_SHORT).show();
            loadDoctors();
        } else {
            Toast.makeText(this, "Failed to delete doctor", Toast.LENGTH_SHORT).show();
        }
    }
}
