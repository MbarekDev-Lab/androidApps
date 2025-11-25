package com.plracticalcoding.restWeatherApp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.plracticalcoding.myapplication.R;


public class WeatheActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    LinearLayout linearLayoutSearch;
    LinearLayout linearLayoutWeatherData;
    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weathe);
        toolbar = findViewById(R.id.toolbar);
        linearLayoutSearch = findViewById(R.id.linearLayoutSearch);
        linearLayoutWeatherData = findViewById(R.id.linearLayoutWeatherData);
        search = findViewById(R.id.search);

        toolbar.setNavigationOnClickListener(v -> finish());

        search.setOnClickListener(view -> {
            //startActivity(new Intent(WeatheActivity.this, WeatherByLocationActivity.class));
        });


    }
}