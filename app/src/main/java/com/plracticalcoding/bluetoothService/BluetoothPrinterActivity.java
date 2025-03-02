package com.plracticalcoding.bluetoothService;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.plracticalcoding.myapplication.R;

public class BluetoothPrinterActivity extends AppCompatActivity implements BluetoothPrinterService.BluetoothCallback {

    private static final String TAG = "BluetoothPrinterActivity";
    private static final int REQUEST_BLUETOOTH_CONNECT = 1;
    private BluetoothPrinterService bluetoothService;
    private boolean isServiceBound = false;

    // UI elements
    private Button connectButton;
    private Button sendButton;
    private Button selectDeviceButton;
    private Button reconnectButton;
    private Button disconnectButton;
    private EditText commandEditText;
    private TextView statusTextView;

    // Bluetooth-related members
    private BluetoothAdapter bluetoothAdapter;
    private ActivityResultLauncher<Intent> deviceListLauncher;

    // Service connection to manage the service lifecycle
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Service connected");
            BluetoothPrinterService.LocalBinder binder = (BluetoothPrinterService.LocalBinder) service;
            bluetoothService = binder.getService();
            bluetoothService.setBluetoothCallback(BluetoothPrinterActivity.this);
            isServiceBound = true;
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service disconnected");
            isServiceBound = false;
            bluetoothService = null;
            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);
        initializeUI();
        initializeBluetooth();
        setupClickListeners();
        initializeDeviceListLauncher();
    }

    private void initializeUI() {
        connectButton = findViewById(R.id.connectButton);
        sendButton = findViewById(R.id.sendButton);
        commandEditText = findViewById(R.id.commandEditText);
        statusTextView = findViewById(R.id.statusTextView);
        selectDeviceButton = findViewById(R.id.selectDeviceButton);
        reconnectButton = findViewById(R.id.reconnectButton);
        disconnectButton = findViewById(R.id.disconnectButton);
    }

    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.bluetooth_not_supported, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupClickListeners() {
        connectButton.setOnClickListener(v -> connectToDevice());
        sendButton.setOnClickListener(v -> sendCommand());
        selectDeviceButton.setOnClickListener(v -> startDeviceListActivity());
        reconnectButton.setOnClickListener(v -> reconnectToDevice());
        disconnectButton.setOnClickListener(v -> disconnectDevice());
    }

    private void initializeDeviceListLauncher() {
        deviceListLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        connectToDevice();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindToService();
    }

    private void bindToService() {
        Intent intent = new Intent(this, BluetoothPrinterService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindFromService();
    }

    private void unbindFromService() {
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
            bluetoothService = null;
        }
    }

    private void startDeviceListActivity() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, R.string.bluetooth_not_enabled, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, DeviceListActivity.class);
        deviceListLauncher.launch(intent);
    }

    private void connectToDevice() {
        if (!checkBluetoothPermissions()) {
            return;
        }
        if (!isServiceReady()) {
            return;
        }
        BluetoothDevice device = DeviceListActivity.getDevice(getApplicationContext());
        if (device == null) {
            Toast.makeText(this, R.string.no_device_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        connectToPrinter(device);
    }

    private void connectToPrinter(BluetoothDevice device) {
        if (bluetoothService.connectToPrinter(device)) {
            statusTextView.setText(getString(R.string.connecting_to, device.getName()));
            updateUI();
        } else {
            statusTextView.setText(R.string.failed_to_connect);
            updateUI();
        }
    }

    private boolean checkBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT);
            return false;
        }
        return true;
    }

    private boolean isServiceReady() {
        if (bluetoothService == null || !isServiceBound) {
            Toast.makeText(this, R.string.service_not_connected, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void reconnectToDevice() {
        if (bluetoothService != null) {
            bluetoothService.reconnectToPrinter();
        }
    }

    private void disconnectDevice() {
        if (bluetoothService != null) {
            bluetoothService.disconnect();
        }
    }

    private void sendCommand() {
        if (isServiceReady()) {
            String command = commandEditText.getText().toString();
            bluetoothService.sendCommandToPrinter(command);
        }
    }

    @Override
    public void onConnectionStatusChanged(boolean isConnected, String deviceName) {
        runOnUiThread(() -> {
            if (isConnected) {
                statusTextView.setText(getString(R.string.connected_to, deviceName));
            } else {
                statusTextView.setText(R.string.disconnected);
            }
            updateUI();
        });
    }

    @Override
    public void onDataReceived(String data) {
        runOnUiThread(() -> Toast.makeText(BluetoothPrinterActivity.this, getString(R.string.data_received, data), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onPrinterNotFound() {
        runOnUiThread(() -> Toast.makeText(BluetoothPrinterActivity.this, R.string.printer_device_not_found, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onConnectionFailed(String message) {
        runOnUiThread(() -> {
            Toast.makeText(BluetoothPrinterActivity.this, message, Toast.LENGTH_SHORT).show();
            statusTextView.setText(message);
            updateUI();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectToDevice();
            } else {
                Toast.makeText(this, R.string.bluetooth_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUI() {
        if (bluetoothService != null && bluetoothService.isConnected()) {
            enableConnectedUI();
        } else {
            disableConnectedUI();
        }
    }

    private void enableConnectedUI() {
        sendButton.setEnabled(true);
        reconnectButton.setEnabled(true);
        disconnectButton.setEnabled(true);
        connectButton.setEnabled(false);
    }

    private void disableConnectedUI() {
        sendButton.setEnabled(false);
        reconnectButton.setEnabled(false);
        disconnectButton.setEnabled(false);
        connectButton.setEnabled(true);
    }
}