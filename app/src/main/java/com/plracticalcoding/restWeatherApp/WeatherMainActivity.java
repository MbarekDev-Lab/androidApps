package com.plracticalcoding.restWeatherApp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.plracticalcoding.myapplication.R;

public class WeatherMainActivity extends AppCompatActivity {
    Button buttonWheatherByLocation;
    Button buttonWheatherByCity;
    BottomSheetDialog bottomSheetDialog;

    ActivityResultLauncher<String[]> requestPermissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        buttonWheatherByLocation = findViewById(R.id.buttonWheatherByLocation);
        buttonWheatherByCity = findViewById(R.id.buttonWheatherByCity);

        registerforPermission();

        buttonWheatherByCity.setOnClickListener(view -> {
            // startActivity(new Intent(WeatherMainActivity.this, WeatherByCityActivity.class));
        });


        buttonWheatherByLocation.setOnClickListener(view -> {
            if (hasFineLocationPermission()) {
                checkLocationSetting();
            } else if (hasCoarseLocationPermission()) {
                showBottomSheetDialog("permission", Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});

            }
        });


    }

    public void registerforPermission() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {

            boolean isFineGranted = Boolean.TRUE.equals(isGranted.get(Manifest.permission.ACCESS_FINE_LOCATION));
            boolean isCoarseGranted = Boolean.TRUE.equals(isGranted.get(Manifest.permission.ACCESS_COARSE_LOCATION));

            if (isFineGranted) {
                checkLocationSetting();
            } else if (isCoarseGranted) {
                showBottomSheetDialog("permission", Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                showBottomSheetDialog("permission", Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        });
    }


    private boolean hasFineLocationPermission() {//ContextCompat vs ActivityCompat
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private boolean hasCoarseLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void showBottomSheetDialog(String useFor, String permission) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        Button buttonAllow = bottomSheetDialog.findViewById(R.id.buttonAllow);
        Button buttonDeny = bottomSheetDialog.findViewById(R.id.buttonDeny);
        TextView textViewTitle = bottomSheetDialog.findViewById(R.id.textViewTitle);
        TextView textViewMessage = bottomSheetDialog.findViewById(R.id.textViewMessage);


        if (useFor.equals("location")) {
            if (buttonAllow != null) {
                buttonAllow.setText("Go");
            }
            if (textViewTitle != null) {
                textViewTitle.setText("Location");
            }
            if (textViewMessage != null) {
                textViewMessage.setText("Go to location setting to run the app, location must be on");
            }
        } else {
            if (textViewMessage != null) {
                textViewMessage.setText("To get the weather by Location, this app requires location permission.");
            }
        }


        if (buttonAllow != null) {
            buttonAllow.setOnClickListener(view -> {
                if (useFor.equals("location")) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } else {
                    requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                }
                bottomSheetDialog.dismiss();
            });
        }

        if (buttonDeny != null) {
            buttonDeny.setOnClickListener(view -> {
                bottomSheetDialog.dismiss();
            });
        }
        bottomSheetDialog.show();
    }

    public void checkLocationSetting() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showBottomSheetDialog("location", "");
        } else {
            // Location is enabled, proceed with weather fetching
        }
    }

}
