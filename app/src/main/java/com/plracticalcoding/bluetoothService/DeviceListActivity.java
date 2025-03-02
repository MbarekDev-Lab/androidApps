package com.plracticalcoding.bluetoothService;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.plracticalcoding.myapplication.R;

import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    private static final String TAG = "DeviceListActivity";
    private static final String PREF_DEVICE = "pref_device";
    private static final String DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_BLUETOOTH_CONNECT = 1;
    private static final int REQUEST_BLUETOOTH_SCAN = 2;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    private ArrayAdapter<String> newDevicesArrayAdapter;
    private ActivityResultLauncher<Intent> enableBluetoothLauncher;
    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "BroadcastReceiver onReceive: " + action);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (checkBluetoothConnectPermission()) {
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        newDevicesArrayAdapter.add(formatDeviceName(device));
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (newDevicesArrayAdapter.isEmpty()) {
                    newDevicesArrayAdapter.add(getString(R.string.no_devices_found));
                }
            }
        }
    };

    private final AdapterView.OnItemClickListener deviceClickListener = new AdapterView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (checkBluetoothScanPermission()) {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                String info = ((TextView) view).getText().toString();
                if (info.equals(getString(R.string.no_paired_devices)) || info.equals(getString(R.string.no_devices_found))) {
                    return;
                }
                String address = extractDeviceAddress(info);
                BluetoothDevice selectedDevice = bluetoothAdapter.getRemoteDevice(address);
                saveDevice(getApplicationContext(), selectedDevice.getAddress());
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        setResult(Activity.RESULT_CANCELED);
        initializeUI();
        initializeBluetooth();
        initializeDeviceListLauncher();
    }

    private void initializeUI() {
        pairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        newDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

        ListView pairedListView = findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(deviceClickListener);

        ListView newDevicesListView = findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(newDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(deviceClickListener);
    }

    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.bluetooth_not_supported, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initializeDeviceListLauncher() {
        enableBluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        discoverDevices();
                    } else {
                        Toast.makeText(this, R.string.bluetooth_required, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryReceiver, filter);

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothLauncher.launch(enableBtIntent);
        } else {
            discoverDevices();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(discoveryReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void discoverDevices() {
        Log.d(TAG, "discoverDevices");
        if (checkBluetoothConnectPermission()) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            pairedDevicesArrayAdapter.clear();
            if (!pairedDevices.isEmpty()) {
                for (BluetoothDevice device : pairedDevices) {
                    pairedDevicesArrayAdapter.add(formatDeviceName(device));
                }
            } else {
                pairedDevicesArrayAdapter.add(getString(R.string.no_paired_devices));
            }
        }

        if (checkBluetoothScanPermission()) {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_BLUETOOTH_CONNECT || requestCode == REQUEST_BLUETOOTH_SCAN) {
                discoverDevices();
            }
        } else {
            Toast.makeText(this, R.string.bluetooth_permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    // --- Helper Methods ---
    private String formatDeviceName(BluetoothDevice device) {
        return device.getName() + "\n" + device.getAddress();
    }

    private String extractDeviceAddress(String info) {
        return info.substring(info.length() - 17);
    }

    private boolean checkBluetoothConnectPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT);
                return false;
            }
        }
        return true;
    }

    private boolean checkBluetoothScanPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_BLUETOOTH_SCAN);
                return false;
            }
        }
        return true;
    }

    // --- Shared Preferences Methods ---
    public static void saveDevice(Context context, String deviceAddress) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_DEVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(DEVICE_ADDRESS, deviceAddress);
        editor.apply();
    }

    public static BluetoothDevice getDevice(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_DEVICE, Context.MODE_PRIVATE);
        String deviceAddress = prefs.getString(DEVICE_ADDRESS, null);
        if (deviceAddress == null) {
            return null;
        }
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return null;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        return bluetoothAdapter.getRemoteDevice(deviceAddress);
    }
}