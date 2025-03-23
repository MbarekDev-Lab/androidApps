package com.plracticalcoding.testCode.autoLogout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);

        // Initialize UI elements
//        usernameInput = findViewById(R.id.usernameInput);
//        passwordInput = findViewById(R.id.passwordInput);
//        loginButton = findViewById(R.id.loginButton);

        // Check if user is already logged in
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            navigateToMain();
        }

        loginButton.setOnClickListener(view -> authenticateUser());
    }

    private void authenticateUser() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Simulated authentication (Replace with real authentication)
        if (username.equals("admin") && password.equals("password")) {
            saveLoginState();
            navigateToMain();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoginState() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    private void logoutUser() {
        // Clear login state
        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().clear().apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }




}

