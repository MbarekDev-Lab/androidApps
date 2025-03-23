package com.plracticalcoding.testCode.autoLogout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // Check if user is logged in
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}