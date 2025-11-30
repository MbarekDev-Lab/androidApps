package com.plracticalcoding.appHomeArea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.calculator.CalculatorActivity;
import com.plracticalcoding.firbase.FirbaseMainActivity;
import com.plracticalcoding.fragments.fragment_ope.activities.OpeFragmentActivity;
import com.plracticalcoding.multithreading.workManager.MultithreadingAndroidActivity;
import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.quizGame.number_guessing.GassingNumberSplash;
import com.plracticalcoding.quizGame.mathGame.GameActivity;
import com.plracticalcoding.restAPIRetrofit.MainRestApiActivity;

public class MainAppArea extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        setContentView(R.layout.activity_main_app_area);


        Button multithreadingActivity = findViewById(R.id.areaMultithreadingActivity);
        Button firbaseactivity = findViewById(R.id.firbaseactivity);
        Button quiz_game = findViewById(R.id.quiz_game);
        Button calculator = findViewById(R.id.calculator);
        Button gussing_num = findViewById(R.id.gussing_num);
        Button opeFragmentActivity = findViewById(R.id.opeFragmentActivity);
        Button chatappbtnid = findViewById(R.id.chatappbtnid);
        Button mainRestApiActivity = findViewById(R.id.mainRestApiActivity);
        Button weatheractivitybtnid = findViewById(R.id.weatheractivitybtnid);
        Button mainDatabindingActivityactivitybtnid = findViewById(R.id.mainDatabindingActivityactivitybtnid);
        Button mainFlagQuizActivity = findViewById(R.id.mainFlagQuizActivity);


        firbaseactivity.setOnClickListener(v -> {
            Toast.makeText(this, "firbaseactivity APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FirbaseMainActivity.class));
        });

        multithreadingActivity.setOnClickListener(v -> {
            Toast.makeText(this, "MULTITHREADING APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MultithreadingAndroidActivity.class));
        });


        quiz_game.setOnClickListener(v -> {
            Toast.makeText(this, "quiz_game APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GameActivity.class));
        });


        calculator.setOnClickListener(v -> {
            Toast.makeText(this, "CalculatorActivity APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CalculatorActivity.class));
        });

        gussing_num.setOnClickListener(v -> {
            Toast.makeText(this, "Gussing_num APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GassingNumberSplash.class));
        });

        opeFragmentActivity.setOnClickListener(v -> {
            Toast.makeText(this, "ope Fragment Activity APP", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, OpeFragmentActivity.class));
        });

        mainRestApiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainAppArea.this, MainRestApiActivity.class));
            }
        });
        weatheractivitybtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainAppArea.this, com.plracticalcoding.restWeatherApp.view.MainWeatherActivity.class));
            }
        });
        mainDatabindingActivityactivitybtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainAppArea.this, com.plracticalcoding.androidLibraries.MainDatabindingActivity.class));
            }
        });

        mainFlagQuizActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainAppArea.this, com.plracticalcoding.FlagQuizApp.resources.view.MainFlagQuizActivity.class));
            }
        });


    }
}