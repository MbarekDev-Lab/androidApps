package com.plracticalcoding.restWeatherApp;

import android.Manifest;
import android.app.Instrumentation;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.restWeatherApp.util.Constants;

public class WeatherMainActivity extends AppCompatActivity {
    Button buttonWheatherByLocation;
    Button buttonWheatherByCity;

    ActivityResultLauncher<String[]> requestPermissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        buttonWheatherByLocation = findViewById(R.id.buttonWheatherByLocation);
        buttonWheatherByCity = findViewById(R.id.buttonWheatherByCity);

        buttonWheatherByLocation.setOnClickListener(view -> {
            // startActivity(new Intent(WeatherMainActivity.this, WeatherByLocationActivity.class));
            if (hasFineLocationPermission()) {

            } else if (hasCoarseLocationPermission()) {

            } else {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});

            }


        });

        buttonWheatherByCity.setOnClickListener(view -> {
            // startActivity(new Intent(WeatherMainActivity.this, WeatherByCityActivity.class));
        });


    }

    public void registerforPermission() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {

            Boolean b1 = isGranted.get(Constants.FINAL_LOCATION);
            Boolean b2 = isGranted.get(Constants.FINAL_LOCATION_COARSE);

            if (b1 != null && b2 != null) ;
            boolean isFineGranted = b1;
            boolean isCoarseGranted = b2;
            if (isFineGranted) {

            } else if (isCoarseGranted) {

            } else {

            }
        });
    }


    private boolean hasFineLocationPermission() {//ContextCompat vs ActivityCompat
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }


    private boolean hasCoarseLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }


}