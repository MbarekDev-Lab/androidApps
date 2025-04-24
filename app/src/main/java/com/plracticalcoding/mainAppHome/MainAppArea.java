package com.plracticalcoding.mainAppHome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.multithreadingAndroid.workManager.MultithreadingAndroidActivity;
import com.plracticalcoding.myapplication.R;

public class MainAppArea extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_app_area);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button multithreadingActivity = findViewById(R.id.areaMultithreadingActivity);

        multithreadingActivity.setOnClickListener(v -> {
            Toast.makeText(this, "MULTITHREADING APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MultithreadingAndroidActivity.class));
        });


    }
}