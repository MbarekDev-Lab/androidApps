package com.plracticalcoding.bluetoothService;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plracticalcoding.myapplication.R;

public class MyFragment extends Fragment {
    private BluetoothPermissionHelper bluetoothPermissionHelper;
    private Button connectButton;
    private Button scanButton;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeBluetoothPermissionHelper();
    }

    private void initializeBluetoothPermissionHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionHelper = new BluetoothPermissionHelper(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUI(view);
        setupClickListeners();
    }

    private void initializeUI(View view) {
        connectButton = view.findViewById(R.id.connectButton);
        scanButton = view.findViewById(R.id.scanButton);
    }

    private void setupClickListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            connectButton.setOnClickListener(v -> bluetoothPermissionHelper.requestBluetoothConnectPermission());
            scanButton.setOnClickListener(v -> bluetoothPermissionHelper.requestBluetoothScanPermission());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}