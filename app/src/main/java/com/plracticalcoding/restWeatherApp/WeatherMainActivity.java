package com.plracticalcoding.restWeatherApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
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
import com.plracticalcoding.restWeatherApp.util.Constants;

public class WeatherMainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private int deniedAllPermissionsCount;
    private int deniedLocationOnlyPermissionCount;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    private enum DialogType {
        PERMISSION,
        APP_SETTINGS,
        ENABLE_GPS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        sharedPreferences = getSharedPreferences(Constants.SharedPrefrencesname, MODE_PRIVATE);
        deniedAllPermissionsCount = sharedPreferences.getInt(Constants.keyForAllSharedPrefrencesCount, 0);
        deniedLocationOnlyPermissionCount = sharedPreferences.getInt(Constants.keyForOnlySharedPrefrencesCount, 0);

        Button buttonWeatherByLocation = findViewById(R.id.buttonWheatherByLocation);
        Button buttonWeatherByCity = findViewById(R.id.buttonWheatherByCity);

        registerForPermission();

        buttonWeatherByCity.setOnClickListener(view -> {
            // startActivity(new Intent(WeatherMainActivity.this, WeatherByCityActivity.class));
        });

        buttonWeatherByLocation.setOnClickListener(view -> handleWeatherByLocationClick());
    }

    private void handleWeatherByLocationClick() {
        if (hasFineLocationPermission()) {
            checkLocationAndProceed();
        } else if (hasCoarseLocationPermission()) {
            handleFineLocationPermissionDenial();
        } else {
            handleAllPermissionsDenial();
        }
    }

    private void registerForPermission() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            boolean isFineGranted = Boolean.TRUE.equals(permissions.get(Constants.FINAL_LOCATION));
            boolean isCoarseGranted = Boolean.TRUE.equals(permissions.get(Constants.FINAL_LOCATION_COARSE));

            if (isFineGranted) {
                checkLocationAndProceed();
            } else if (isCoarseGranted) {
                handleFineLocationPermissionDenial();
            } else {
                handleAllPermissionsDenial();
            }
        });
    }

    private void handleFineLocationPermissionDenial() {
        deniedLocationOnlyPermissionCount++;
        savePermissionCount(Constants.keyForOnlySharedPrefrencesCount, deniedLocationOnlyPermissionCount);

        if (deniedLocationOnlyPermissionCount > 2) {
            showBottomSheetDialog(DialogType.APP_SETTINGS);
        } else {
            showBottomSheetDialog(DialogType.PERMISSION);
        }
    }

    private void handleAllPermissionsDenial() {
        deniedAllPermissionsCount++;
        savePermissionCount(Constants.keyForAllSharedPrefrencesCount, deniedAllPermissionsCount);

        if (deniedAllPermissionsCount > 2) {
            showBottomSheetDialog(DialogType.APP_SETTINGS);
        } else {
            showBottomSheetDialog(DialogType.PERMISSION);
        }
    }

    private void checkLocationAndProceed() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showBottomSheetDialog(DialogType.ENABLE_GPS);
        } else {
            getWeatherByLocation();
        }
    }

    private void getWeatherByLocation() {
        // startActivity(new Intent(this, WeatherByLocationActivity.class));
    }

    private void showBottomSheetDialog(DialogType dialogType) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        TextView textViewTitle = bottomSheetDialog.findViewById(R.id.textViewTitle);
        TextView textViewMessage = bottomSheetDialog.findViewById(R.id.textViewMessage);
        Button buttonAllow = bottomSheetDialog.findViewById(R.id.buttonAllow);
        Button buttonDeny = bottomSheetDialog.findViewById(R.id.buttonDeny);

        if (buttonAllow != null) {
            buttonAllow.setText(R.string.allow);
        }
        if (buttonDeny != null) {
            buttonDeny.setText(R.string.deny);
        }

        switch (dialogType) {
            case PERMISSION:
                if (textViewTitle != null) {
                    textViewTitle.setText(R.string.location_permission_title);
                }
                if (textViewMessage != null) {
                    textViewMessage.setText(R.string.location_permission_message);
                }
                if (buttonAllow != null) {
                    buttonAllow.setOnClickListener(v -> {
                        requestLocationPermissions();
                        bottomSheetDialog.dismiss();
                    });
                }
                if (buttonDeny != null) {
                    buttonDeny.setOnClickListener(v -> bottomSheetDialog.dismiss());
                }
                break;
            case APP_SETTINGS:
                if (textViewTitle != null) {
                    textViewTitle.setText(R.string.permission_denied_title);
                }
                if (textViewMessage != null) {
                    textViewMessage.setText(R.string.location_permission_denied_message);
                }
                if (buttonAllow != null) {
                    buttonAllow.setText(R.string.go_to_settings);
                    buttonAllow.setOnClickListener(v -> {
                        openAppSettings();
                        bottomSheetDialog.dismiss();
                    });
                }
                if (buttonDeny != null) {
                    buttonDeny.setOnClickListener(v -> bottomSheetDialog.dismiss());
                }
                break;
            case ENABLE_GPS:
                if (textViewTitle != null) {
                    textViewTitle.setText(R.string.enable_gps_title);
                }
                if (textViewMessage != null) {
                    textViewMessage.setText(R.string.enable_gps_dialog_message);
                }
                if (buttonAllow != null) {
                    buttonAllow.setText(R.string.go_to_settings);
                    buttonAllow.setOnClickListener(v -> {
                        openLocationSettings();
                        bottomSheetDialog.dismiss();
                    });
                }
                if (buttonDeny != null) {
                    buttonDeny.setOnClickListener(v -> bottomSheetDialog.dismiss());
                }
                break;
        }

        bottomSheetDialog.show();
    }

    private void requestLocationPermissions() {
        requestPermissionLauncher.launch(new String[]{Constants.FINAL_LOCATION, Constants.FINAL_LOCATION_COARSE});
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void openLocationSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private boolean hasFineLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Constants.FINAL_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasCoarseLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Constants.FINAL_LOCATION_COARSE) == PackageManager.PERMISSION_GRANTED;
    }

    private void savePermissionCount(String key, int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, count);
        editor.apply();
    }
}
