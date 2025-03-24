package com.plracticalcoding.testCode.autoLogout;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final long INACTIVITY_TIMEOUT = 30 * 60 * 1000; // 30 minutes in milliseconds
    private final Handler inactivityHandler = new Handler(Looper.getMainLooper());
    private final Runnable logoutRunnable = this::logoutUser;

    @Override
    protected void onResume() {
        super.onResume();
        resetInactivityTimer();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        resetInactivityTimer(); // Reset the timer on user interaction
    }

    private void resetInactivityTimer() {
        inactivityHandler.removeCallbacks(logoutRunnable);
        inactivityHandler.postDelayed(logoutRunnable, INACTIVITY_TIMEOUT);
    }

    private void logoutUser() {
        // Clear session or token
        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().clear().apply();

        // Navigate to login screen
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        inactivityHandler.removeCallbacks(logoutRunnable);
    }
}
