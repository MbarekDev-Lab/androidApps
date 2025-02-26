package com.plracticalcoding.bluetoothService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothPrinterService extends Service {

    // Constants for logging, notifications, and Bluetooth
    private static final String TAG = "BluetoothPrinterService";
    private static final String CHANNEL_ID = "BluetoothPrinterServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    // Standard SPP UUID (Serial Port Profile)
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Binder for activities to interact with the service
    private final IBinder binder = new LocalBinder();

    // Bluetooth-related members
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice printerDevice;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread readThread;

    // Connection status
    private boolean isConnected = false;

    // Handler and Runnable for connection retries
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable retryConnectionRunnable;

    // Callback interface for the activity to receive updates
    public interface BluetoothCallback {
        void onConnectionStatusChanged(boolean isConnected);
        void onDataReceived(String data);
        void onPrinterNotFound();
    }

    private BluetoothCallback bluetoothCallback;

    public void setBluetoothCallback(BluetoothCallback callback) {
        this.bluetoothCallback = callback;
    }

    // BroadcastReceiver to listen for Bluetooth state changes (e.g., turning on/off)
    private final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null) {
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        Log.w(TAG,"Bluetooth was turned off");
                        disconnect();
                    }
                }
            }
        }
    };

    // Binder class for the activity
    public class LocalBinder extends Binder {
        BluetoothPrinterService getService() {
            return BluetoothPrinterService.this;
        }
    }

    // Called when the service is first created
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: BluetoothPrinterService created");

        // Get the Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Create the notification channel (for Android Oreo and above)
        createNotificationChannel();

        // Start the service in the foreground with a notification
        startForeground(NOTIFICATION_ID, buildNotification());

        // Register the Bluetooth state receiver
        registerBluetoothStateReceiver();

        // Set up the retry connection runnable
        retryConnectionRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Attempting to reconnect...");
                connectToPrinter(printerDevice);
            }
        };
    }

    // Called when the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Keep the service running even if it's killed by the system
    }

    // Called when an activity binds to the service
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Method to connect to the printer
    public boolean connectToPrinter(BluetoothDevice device) {
        if (isConnected) {
            Log.d(TAG, "Already connected");
            return true;
        }

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            return false;
        }

        this.printerDevice = device;

        try {
            if (device == null){
                Log.e(TAG, "Printer device not found!");
                if (bluetoothCallback != null){
                    bluetoothCallback.onPrinterNotFound();
                }
                return false;
            }
            Log.d(TAG, "Attempting to connect to device: " + device.getName());
            socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID);
            socket.connect(); // Blocking call - will throw IOException if fails
            isConnected = true;
            // Start communication threads here
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            startReadThread();
            handler.removeCallbacks(retryConnectionRunnable); // Stop retry attempts if successful
            Log.d(TAG, "Connected to device: " + device.getName());
            if (bluetoothCallback != null) {
                bluetoothCallback.onConnectionStatusChanged(true);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to device: " + device.getName(), e);
            isConnected = false;
            retryConnection();
            if (bluetoothCallback != null) {
                bluetoothCallback.onConnectionStatusChanged(false);
            }
            return false;
        }
    }
    // Method to start the read thread
    private void startReadThread() {
        readThread = new Thread(() -> {
            byte[] buffer = new byte[1024];
            int bytes;
            while (isConnected) {
                try {
                    // Read from the InputStream
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        String receivedData = new String(buffer, 0, bytes);
                        Log.d(TAG, "Received data: " + receivedData);
                        if (bluetoothCallback != null) {
                            bluetoothCallback.onDataReceived(receivedData);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error reading from InputStream", e);
                    break; // Exit loop on error
                }
            }
        });
        readThread.start();
    }

    // Method to send a command to the printer
    public void sendCommandToPrinter(String command) {
        if (outputStream == null) {
            Log.e(TAG, "Error: outputStream is null");
            return;
        }
        new Thread(() -> {
            try {
                outputStream.write(command.getBytes());
                outputStream.flush(); // Ensure data is sent immediately
                Log.d(TAG, "Data sent: " + command);
            } catch (IOException e) {
                Log.e(TAG, "Error writing to OutputStream", e);
            }
        }).start();
    }

    // Method to disconnect from the printer
    public void disconnect() {
        isConnected = false;
        handler.removeCallbacks(retryConnectionRunnable);
        if (socket != null) {
            try {
                if (readThread != null){
                    readThread.interrupt();
                }
                inputStream.close();
                outputStream.close();
                socket.close();
                if (bluetoothCallback != null) {
                    bluetoothCallback.onConnectionStatusChanged(false);
                }
                Log.d(TAG, "Disconnected from printer");
            } catch (IOException e) {
                Log.e(TAG, "Error closing socket", e);
            }
        }
    }

    // Method to retry the connection after a delay
    private void retryConnection() {
        handler.postDelayed(retryConnectionRunnable, 5000); // Retry after 5 seconds
    }

    // Called when the service is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: BluetoothPrinterService destroyed");
        disconnect();
        unregisterReceiver(bluetoothStateReceiver);
    }

    // Method to build the notification for the foreground service
    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, BluetoothPrinterActivity.class);
        //Toast.makeText(BluetoothPrinterActivity.this, "Data received: " + data, Toast.LENGTH_SHORT).show();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Bluetooth Printer Service")
                .setContentText("Connected to printer")
                .setSmallIcon(R.drawable.ic_bluetooth) // Replace with your icon
                .setContentIntent(pendingIntent)
                .build();
    }

    // Method to create the notification channel (for Android Oreo and above)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Bluetooth Printer Service Channel",
                    NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // Method to register the Bluetooth state receiver
    private void registerBluetoothStateReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateReceiver, filter);
    }

    // Method to check the connection status
    public boolean isConnected() {
        return isConnected;
    }
}