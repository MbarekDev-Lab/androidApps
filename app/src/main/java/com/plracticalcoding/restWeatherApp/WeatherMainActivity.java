package com.plracticalcoding.restWeatherApp;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

public class WeatherMainActivity extends AppCompatActivity {
Button buttonWheatherByLocation;
Button buttonWheatherByCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        buttonWheatherByLocation = findViewById(R.id.buttonWheatherByLocation);
        buttonWheatherByCity = findViewById(R.id.buttonWheatherByCity);

        buttonWheatherByLocation.setOnClickListener(view -> {
           // startActivity(new Intent(WeatherMainActivity.this, WeatherByLocationActivity.class));
        });

        buttonWheatherByCity.setOnClickListener(view -> {
           // startActivity(new Intent(WeatherMainActivity.this, WeatherByCityActivity.class));
        });

    }



}