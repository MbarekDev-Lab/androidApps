package com.plracticalcoding.multithreadingAndroid.UploadDatatoDB;// Inside your LoginActivity or ViewModel where login success is handled
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.yourpackage.R; // Your R file
import com.yourpackage.work.UserSpecificPeriodicWorker; // Your worker

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    // Define a base unique name. We might append user ID or use REPLACE policy.
    public static final String USER_SPECIFIC_PERIODIC_WORK_NAME_BASE = "UserSpecificPeriodicSync";

    private EditText editTextUserId; // Or however you get the user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Your login layout

        editTextUserId = findViewById(R.id.editTextUserId); // Example
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String userId = editTextUserId.getText().toString().trim();
            if (!userId.isEmpty()) {
                // Simulate successful login
                Log.i(TAG, "User " + userId + " logged in successfully.");
                saveLoggedInUser(userId); // Save user state (e.g., SharedPreferences)

                // Schedule or update the periodic work for this user
                scheduleOrUpdateUserSpecificPeriodicWork(userId);

                // Proceed to MainActivity or next screen
                // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                // startActivity(intent);
                // finish();
                Toast.makeText(LoginActivity.this, "Logged in as " + userId, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(LoginActivity.this, "Please enter User ID", Toast.LENGTH_SHORT).show();
            }
        });

        // Optional: Check if a user is already logged in on activity start
        // String currentUserId = getLoggedInUser();
        // if (currentUserId != null) {
        //     Log.i(TAG, "User " + currentUserId + " already logged in. Ensuring worker is scheduled.");
        //     scheduleOrUpdateUserSpecificPeriodicWork(currentUserId);
        // }
    }

    /**
     * Schedules or updates the periodic work specific to the logged-in user.
     * This method should be called AFTER a user successfully logs in.
     */
    private void scheduleOrUpdateUserSpecificPeriodicWork(String userId) {
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "Cannot schedule work for null or empty user ID.");
            return;
        }

        // --- Create Input Data for the Worker ---
        Data inputData = new Data.Builder()
                .putString(UserSpecificPeriodicWorker.KEY_USER_ID, userId)
                .build();

        // --- Define Constraints ---
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // --- Create the PeriodicWorkRequest ---
        PeriodicWorkRequest userPeriodicWorkRequest =
                new PeriodicWorkRequest.Builder(UserSpecificPeriodicWorker.class,
                        2, TimeUnit.HOURS) // Example: Repeat every 2 hours
                        .setConstraints(constraints)
                        .setInputData(inputData) // Pass user ID to the worker
                        // Optional: Add a tag that includes the user ID for more specific observation
                        // .addTag("USER_SYNC_" + userId)
                        .build();

        // --- Enqueue with REPLACE policy ---
        // Using REPLACE ensures that if another user was previously logged in and had
        // a similar worker scheduled, that old worker is cancelled and replaced by
        // this new one for the current user.
        // The unique name should be consistent for this type of user-specific work.
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                USER_SPECIFIC_PERIODIC_WORK_NAME_BASE, // A consistent unique name for this *type* of work
                ExistingPeriodicWorkPolicy.REPLACE,    // REPLACE ensures the old user's work is stopped
                userPeriodicWorkRequest);

        Log.i(TAG, "User-specific periodic work enqueued for User ID: " + userId +
                   " with REPLACE policy. Work Name: " + USER_SPECIFIC_PERIODIC_WORK_NAME_BASE);
    }

    /**
     * Optional: Call this on user logout to cancel the specific user's periodic work.
     */
    public void onUserLogout() {
        Log.i(TAG, "User logged out. Cancelling user-specific periodic work: " + USER_SPECIFIC_PERIODIC_WORK_NAME_BASE);
        WorkManager.getInstance(getApplicationContext()).cancelUniqueWork(USER_SPECIFIC_PERIODIC_WORK_NAME_BASE);
        clearLoggedInUser(); // Clear user state
        // Navigate to login screen
    }

    // --- Helper methods for managing logged-in user state (example using SharedPreferences) ---
    private void saveLoggedInUser(String userId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("LOGGED_IN_USER_ID", userId).apply();
    }

    private String getLoggedInUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("LOGGED_IN_USER_ID", null);
    }

    private void clearLoggedInUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("LOGGED_IN_USER_ID").apply();
    }
}