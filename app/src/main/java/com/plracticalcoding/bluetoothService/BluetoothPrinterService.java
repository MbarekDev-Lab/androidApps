package com.plracticalcoding.bluetoothService;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothPrinterService extends Service {

    private static final String TAG = "BluetoothPrinterService";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SerialPortService ID
    private BluetoothSocket bluetoothSocket;
    private ConnectedThread connectedThread;
    private BluetoothDevice connectedDevice;
    private boolean isConnected = false;

    // Interface for callback methods
    public interface BluetoothCallback {
        void onConnectionStatusChanged(boolean isConnected, String deviceName);

        void onDataReceived(String data);

        void onPrinterNotFound();

        void onConnectionFailed(String message);
    }

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    // Callback listener
    private BluetoothCallback callback;

    // Method to set the callback listener
    public void setBluetoothCallback(BluetoothCallback callback) {
        this.callback = callback;
    }

    public class LocalBinder extends Binder {
        public BluetoothPrinterService getService() {
            return BluetoothPrinterService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean connectToPrinter(BluetoothDevice device) {
        Log.d(TAG, "Connecting to printer: " + device.getName());
        try {
            closeConnection();
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            connectedDevice = device;
            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
            isConnected = true;
            notifyConnectionStatus(true, device.getName());
            Log.d(TAG, "Connected to printer: " + device.getName());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Connection failed", e);
            notifyConnectionFailed("Failed to connect: " + e.getMessage());
            isConnected = false;
            return false;
        }
    }

    public void reconnectToPrinter() {
        if (connectedDevice != null && !isConnected) {
            Log.d(TAG, "Attempting to reconnect to: " + connectedDevice.getName());
            connectToPrinter(connectedDevice);
        } else {
            Log.d(TAG, "Not trying to reconnect:  isConnected: "+ isConnected +" , connectedDevice: "+ connectedDevice);
        }
    }

    public void sendCommandToPrinter(String command) {
        if (connectedThread != null) {
            connectedThread.write(command.getBytes());
        } else {
            notifyConnectionFailed("Not connected yet!");
        }
    }

    public void disconnect() {
        Log.d(TAG, "Disconnecting from printer");
        closeConnection();
        notifyConnectionStatus(false, "");
    }

    private void closeConnection() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing socket", e);
            } finally {
                bluetoothSocket = null;
            }
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        isConnected = false;
    }

    private void notifyConnectionStatus(boolean isConnected, String deviceName) {
        if (callback != null) {
            callback.onConnectionStatusChanged(isConnected, deviceName);
        }
    }

    private void notifyConnectionFailed(String message) {
        if (callback != null) {
            callback.onConnectionFailed(message);
        }
    }

    private void notifyDataReceived(String data) {
        if (callback != null) {
            callback.onDataReceived(data);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
        Log.d(TAG, "Service destroyed");
    }

    private class ConnectedThread extends Thread {
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "Creating ConnectedThread");
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error getting streams", e);
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            Log.d(TAG, "ConnectedThread running");
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    String data = new String(buffer, 0, bytes);
                    Log.d(TAG, "Data received: " + data);
                    notifyDataReceived(data);
                } catch (IOException e) {
                    Log.e(TAG, "Disconnected", e);
                    notifyConnectionFailed("Disconnected");
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                notifyConnectionFailed("Error while sending data");
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing socket", e);
            }
        }
    }
}