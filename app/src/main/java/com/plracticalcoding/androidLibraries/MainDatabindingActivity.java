package com.plracticalcoding.androidLibraries;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.plracticalcoding.androidLibraries.model.Info;
import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.myapplication.databinding.ActivityMainDatabindingBinding;

public class MainDatabindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainDatabindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main_databinding);

        Info infoData = new Info("Beni ", "Mbarek ", "Mr ");
        binding.setData(infoData);
    }
}