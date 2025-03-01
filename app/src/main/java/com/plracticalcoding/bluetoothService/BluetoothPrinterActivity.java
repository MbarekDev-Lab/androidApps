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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.plracticalcoding.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.plracticalcoding.bluetoothService.BluetoothPrinterService;
import com.plracticalcoding.myapplication.R;

import java.util.Set;

public class BluetoothPrinterActivity extends AppCompatActivity implements BluetoothPrinterService.BluetoothCallback {

    private static final String TAG = "MainActivity";

    private BluetoothPrinterService bluetoothService;
    private boolean isServiceBound = false;

    // UI elements
    private Button connectButton;
    private Button sendButton;
    private EditText commandEditText;
    private TextView statusTextView;

    // Bluetooth-related members
    private BluetoothDevice printerDevice = null;
    private BluetoothAdapter bluetoothAdapter;

    // **Add the printer's MAC address here**
    private final String PRINTER_MAC_ADDRESS = "00:11:22:33:44:55";

    // Service connection to manage the service lifecycle
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Cast the binder to get the service instance
            BluetoothPrinterService.LocalBinder binder = (BluetoothPrinterService.LocalBinder) service;
            bluetoothService = binder.getService();
            bluetoothService.setBluetoothCallback(BluetoothPrinterActivity.this);
            isServiceBound = true;
            Log.d(TAG, "Service connected");
            if (printerDevice != null){
                // Attempt to connect when the service is ready
                connectToDevice();
            }else{
                Toast.makeText(BluetoothPrinterActivity.this, "Printer device not found!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Service disconnected, update the flag
            isServiceBound = false;
            Log.d(TAG, "Service disconnected");
        }
    };

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);

        // Get references to UI elements
        connectButton = findViewById(R.id.connectButton);
        sendButton = findViewById(R.id.sendButton);
        commandEditText = findViewById(R.id.commandEditText);
        statusTextView = findViewById(R.id.statusTextView);

        // Get the Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Check if the device was found before.
        findDevice();

        // Set up click listeners for the buttons
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToDevice();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand();
            }
        });
    }

    // Called when the activity is started
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the BluetoothPrinterService
        Intent intent = new Intent(this, BluetoothPrinterService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    // Called when the activity is stopped
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service if bound
        if (isServiceBound) {
            unbindService(connection);
            isServiceBound = false;
        }
    }

    // Method to find the printer device by its MAC address
    private void findDevice() {
        // Check if the bluetooth is enabled
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            // Get the list of paired devices
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (!pairedDevices.isEmpty()) {
                for (BluetoothDevice device : pairedDevices) {
                    // **Use the MAC address to find the printer**
                    if (device.getAddress().equals(PRINTER_MAC_ADDRESS)) {
                        printerDevice = device;
                        Log.d(TAG, "Device found");
                        break;
                    }
                }
            }
        }else{
            Toast.makeText(this, "Bluetooth is disabled!", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to connect to the device
    private void connectToDevice() {
        // Check if the service is bound and not null
        if (bluetoothService == null || !isServiceBound){
            Toast.makeText(this, "Service is not connected yet!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (printerDevice == null){
            Toast.makeText(this, "No device found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Connect to the printer using the service
        if (bluetoothService.connectToPrinter(printerDevice)) {
            statusTextView.setText("Connected!");
        } else {
            statusTextView.setText("Failed to connect");
        }
    }

    // Method to send a command to the printer
    private void sendCommand() {
        // Check if the service is bound and not null
        if (bluetoothService != null && isServiceBound) {
            String command = commandEditText.getText().toString();
            bluetoothService.sendCommandToPrinter(command);
        }else{
            Toast.makeText(this, "Service is not connected yet!", Toast.LENGTH_SHORT).show();
        }
    }

    // Callback method for connection status changes
    @Override
    public void onConnectionStatusChanged(boolean isConnected) {
        runOnUiThread(() -> {
            if (isConnected) {
                statusTextView.setText("Connected!");
            } else {
                statusTextView.setText("Disconnected!");
            }
        });
    }

    // Callback method for received data
    @Override
    public void onDataReceived(String data) {
        runOnUiThread(() -> {
            Toast.makeText(BluetoothPrinterActivity.this, "Data received: " + data, Toast.LENGTH_SHORT).show();
        });
    }

    // Callback method if the printer is not found
    @Override
    public void onPrinterNotFound() {
        runOnUiThread(() -> {
            Toast.makeText(BluetoothPrinterActivity.this, "Printer device not found!", Toast.LENGTH_SHORT).show();
        });
    }
}