package com.plracticalcoding.appHomeArea;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.plracticalcoding.calculator.CalculatorActivity;
import com.plracticalcoding.firbase.FirbaseMainActivity;
import com.plracticalcoding.fragments.fragment_ope.activities.OpeFragmentActivity;
import com.plracticalcoding.multithreading.workManager.MultithreadingAndroidActivity;
import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.myapplication.databinding.ActivityMainAppAreaBinding;
import com.plracticalcoding.quizGame.number_guessing.GassingNumberSplash;
import com.plracticalcoding.quizGame.mathGame.GameActivity;
import com.plracticalcoding.restAPIRetrofit.MainRestApiActivity;
import com.plracticalcoding.mp3player.Mp3PLayerMainActivity;

public class MainAppArea extends AppCompatActivity {

    private ActivityMainAppAreaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_app_area);

        binding.firbaseactivity.setOnClickListener(v -> {
            Toast.makeText(this, "firbaseactivity APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FirbaseMainActivity.class));
        });

        binding.areaMultithreadingActivity.setOnClickListener(v -> {
            Toast.makeText(this, "MULTITHREADING APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MultithreadingAndroidActivity.class));
        });

        binding.quizGame.setOnClickListener(v -> {
            Toast.makeText(this, "quiz_game APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GameActivity.class));
        });

        binding.calculator.setOnClickListener(v -> {
            Toast.makeText(this, "CalculatorActivity APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CalculatorActivity.class));
        });

        binding.gussingNum.setOnClickListener(v -> {
            Toast.makeText(this, "Gussing_num APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GassingNumberSplash.class));
        });

        binding.opeFragmentActivity.setOnClickListener(v -> {
            Toast.makeText(this, "ope Fragment Activity APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, OpeFragmentActivity.class));
        });

        binding.mainRestApiActivity.setOnClickListener(view -> {
            startActivity(new Intent(MainAppArea.this, MainRestApiActivity.class));
        });

        binding.weatheractivitybtnid.setOnClickListener(view -> {
            startActivity(new Intent(MainAppArea.this, com.plracticalcoding.restWeatherApp.view.MainWeatherActivity.class));
        });

        binding.mainDatabindingActivityactivitybtnid.setOnClickListener(view -> {
            startActivity(new Intent(MainAppArea.this, com.plracticalcoding.androidLibraries.MainDatabindingActivity.class));
        });

        binding.mainFlagQuizActivity.setOnClickListener(view -> {
            startActivity(new Intent(MainAppArea.this, com.plracticalcoding.FlagQuizApp.resources.view.MainFlagQuizActivity.class));
        });

        binding.mp3PLayerMainActivity.setOnClickListener(view -> {
            startActivity(new Intent(MainAppArea.this, Mp3PLayerMainActivity.class));
        });
    }
}
