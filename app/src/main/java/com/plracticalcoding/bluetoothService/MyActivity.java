package com.plracticalcoding.bluetoothService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.plracticalcoding.myapplication.R;

public class MyActivity extends AppCompatActivity {

    private BluetoothPermissionHelper bluetoothPermissionHelper;
    private static final int FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 103;

    private Button connectButton;
    private Button scanButton;
    private Button startServiceButton;
    private Button stopServiceButton;
    private Button openPrinterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initializeBluetoothPermissionHelper();
        initializeUI();
        setupClickListeners();
    }

    private void initializeBluetoothPermissionHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionHelper = new BluetoothPermissionHelper(this);
        }
    }

    private void initializeUI() {
        connectButton = findViewById(R.id.connectButton);
        scanButton = findViewById(R.id.scanButton);
        startServiceButton = findViewById(R.id.startServiceButton);
        stopServiceButton = findViewById(R.id.stopServiceButton);
        openPrinterActivity = findViewById(R.id.openPrinterActivity);
    }

    private void setupClickListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            connectButton.setOnClickListener(v -> bluetoothPermissionHelper.requestBluetoothConnectPermission());
            scanButton.setOnClickListener(v -> bluetoothPermissionHelper.requestBluetoothScanPermission());
        }
        startServiceButton.setOnClickListener(v -> checkForegroundServicePermission());
        stopServiceButton.setOnClickListener(v -> stopBluetoothService());
        openPrinterActivity.setOnClickListener(v -> openPrinterActivity());
    }

    private void checkForegroundServicePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE},
                    FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE);
        } else {
            startBluetoothService();
        }
    }

    private void startBluetoothService() {
        Intent serviceIntent = new Intent(this, BluetoothPrinterService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void stopBluetoothService() {
        Intent serviceIntent = new Intent(this, BluetoothPrinterService.class);
        stopService(serviceIntent);
    }

    private void openPrinterActivity() {
        Intent printerIntent = new Intent(this, BluetoothPrinterActivity.class);
        startActivity(printerIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBluetoothService();
            }
        }
    }
}