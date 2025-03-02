package com.plracticalcoding.bluetoothService;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

@RequiresApi(api = Build.VERSION_CODES.S)
public class BluetoothPermissionHelper {

    private static final String TAG = "BluetoothPermissionHelper";

    // Request codes for different Bluetooth permissions
    public static final int BLUETOOTH_CONNECT_REQUEST_CODE = 100;
    public static final int BLUETOOTH_SCAN_REQUEST_CODE = 101;
    public static final int BLUETOOTH_ADVERTISE_REQUEST_CODE = 102;

    // Bluetooth permissions
    private static final String BLUETOOTH_CONNECT_PERMISSION = Manifest.permission.BLUETOOTH_CONNECT;
    private static final String BLUETOOTH_SCAN_PERMISSION = Manifest.permission.BLUETOOTH_SCAN;
    private static final String BLUETOOTH_ADVERTISE_PERMISSION = Manifest.permission.BLUETOOTH_ADVERTISE;

    private Fragment fragment;
    private Activity activity;

    public BluetoothPermissionHelper(Fragment fragment) {
        this.fragment = fragment;
        this.activity = null;
    }

    public BluetoothPermissionHelper(Activity activity) {
        this.activity = activity;
        this.fragment = null;
    }

    public void requestBluetoothConnectPermission() {
        requestPermission(BLUETOOTH_CONNECT_PERMISSION, BLUETOOTH_CONNECT_REQUEST_CODE);
    }

    public void requestBluetoothScanPermission() {
        requestPermission(BLUETOOTH_SCAN_PERMISSION, BLUETOOTH_SCAN_REQUEST_CODE);
    }

    public void requestBluetoothAdvertisePermission() {
        requestPermission(BLUETOOTH_ADVERTISE_PERMISSION, BLUETOOTH_ADVERTISE_REQUEST_CODE);
    }

    private void requestPermission(String permission, int requestCode) {
        if (isContextAvailable()) {
            if (!isPermissionGranted(permission)) {
                Log.d(TAG, "Permission " + permission + " not granted. Requesting...");
                requestPermissions(permission, requestCode);
            } else {
                Log.d(TAG, "Permission " + permission + " already granted.");
                onPermissionGranted(requestCode);
            }
        } else {
            Log.e(TAG, "Context is not available. Cannot request permission.");
        }
    }

    private boolean isContextAvailable() {
        return fragment != null || activity != null;
    }

    private boolean isPermissionGranted(String permission) {
        if (fragment != null) {
            return ContextCompat.checkSelfPermission(fragment.requireContext(), permission) == PackageManager.PERMISSION_GRANTED;
        } else if (activity != null) {
            return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestPermissions(String permission, int requestCode) {
        if (fragment != null) {
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{permission}, requestCode);
        } else if (activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult called with requestCode: " + requestCode);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission granted for requestCode: " + requestCode);
            onPermissionGranted(requestCode);
        } else {
            Log.d(TAG, "Permission denied for requestCode: " + requestCode);
            onPermissionDenied(requestCode);
        }
    }

    private void onPermissionGranted(int requestCode) {
        Log.d(TAG, "onPermissionGranted for requestCode: " + requestCode);
        switch (requestCode) {
            case BLUETOOTH_CONNECT_REQUEST_CODE:
                Log.d(TAG, "BLUETOOTH_CONNECT permission is granted.");
                break;
            case BLUETOOTH_SCAN_REQUEST_CODE:
                Log.d(TAG, "BLUETOOTH_SCAN permission is granted.");
                break;
            case BLUETOOTH_ADVERTISE_REQUEST_CODE:
                Log.d(TAG, "BLUETOOTH_ADVERTISE permission is granted.");
                break;
        }
    }

    private void onPermissionDenied(int requestCode) {
        Log.d(TAG, "onPermissionDenied for requestCode: " + requestCode);
        switch (requestCode) {
            case BLUETOOTH_CONNECT_REQUEST_CODE:
                Log.d(TAG, "BLUETOOTH_CONNECT permission is denied.");
                break;
            case BLUETOOTH_SCAN_REQUEST_CODE:
                Log.d(TAG, "BLUETOOTH_SCAN permission is denied.");
                break;
            case BLUETOOTH_ADVERTISE_REQUEST_CODE:
                Log.d(TAG, "BLUETOOTH_ADVERTISE permission is denied.");
                break;
        }
    }
}