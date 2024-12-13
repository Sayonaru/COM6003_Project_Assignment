package com.example.com6003_project_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void goToPatientManagement(View view) {
        Intent intent = new Intent(DashboardActivity.this, PatientManagementActivity.class);
        startActivity(intent);
    }
    public void goToDoctorManagement(View view) {
        Intent intent = new Intent(DashboardActivity.this, DoctorManagementActivity.class);
        startActivity(intent);
    }
}


