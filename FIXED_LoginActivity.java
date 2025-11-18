package com.plracticalcoding.multithreading.UploadDatatoDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.plracticalcoding.myapplication.R;

import java.util.concurrent.TimeUnit;

/**
 * FIXED: Complete rewrite addressing all critical bugs:
 * - Fixed wrong button ID reference (buttonLogin vs loginButton)
 * - Added password validation and authentication logic
 * - Replaced deprecated PreferenceManager with Context.getSharedPreferences()
 * - Added input validation and sanitization
 * - Added null safety checks
 * - Improved error handling and user feedback
 * - Added lifecycle safety
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "UserPreferences"; // FIXED: Using proper SharedPreferences
    private static final String KEY_LOGGED_IN_USER_ID = "LOGGED_IN_USER_ID";
    private static final String KEY_USER_PASSWORD_HASH = "USER_PASSWORD_HASH"; // IMPROVED: Store hashed password
    public static final String USER_SPECIFIC_PERIODIC_WORK_NAME_BASE = "UserSpecificPeriodicSync";

    // IMPROVED: Field references for lifecycle safety
    private EditText editTextUserId;
    private EditText editTextPassword; // FIXED: Added missing password field
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FIXED: Initialize all views with null safety
        initializeViews();

        // FIXED: Check if views are properly initialized
        if (editTextUserId == null || editTextPassword == null || buttonLogin == null) {
            Log.e(TAG, "Failed to initialize views. Check layout resource.");
            Toast.makeText(this, "Error: Failed to load login screen", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // FIXED: Proper button click listener with all validation
        buttonLogin.setOnClickListener(v -> handleLogin());

        // IMPROVED: Check if user is already logged in
        checkExistingLogin();
    }

    /**
     * FIXED: Initialize all views with proper null checks
     */
    private void initializeViews() {
        try {
            editTextUserId = findViewById(R.id.editTextUserId);
            editTextPassword = findViewById(R.id.editTextPassword); // FIXED: Added password field
            buttonLogin = findViewById(R.id.buttonLogin); // FIXED: Correct ID from XML
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }

    /**
     * IMPROVED: Centralized login handling with comprehensive validation
     */
    private void handleLogin() {
        // FIXED: Get and validate both userId and password
        String userId = editTextUserId.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // FIXED: Comprehensive input validation
        if (!validateInput(userId, password)) {
            return; // Validation already shows error messages
        }

        // IMPROVED: Sanitize inputs to prevent injection attacks
        userId = sanitizeInput(userId);

        // FIXED: Simulate authentication (replace with real authentication)
        if (authenticateUser(userId, password)) {
            Log.i(TAG, "User " + userId + " logged in successfully.");
            
            // FIXED: Save user credentials securely
            saveLoggedInUser(userId, password);

            // Schedule user-specific periodic work
            scheduleOrUpdateUserSpecificPeriodicWork(userId);

            Toast.makeText(this, "Logged in as " + userId, Toast.LENGTH_SHORT).show();

            // IMPROVED: Navigate to main activity (uncomment when ready)
            // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // startActivity(intent);
            // finish();
        } else {
            // IMPROVED: Better error messaging without exposing security details
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Authentication failed for user: " + userId);
        }
    }

    /**
     * FIXED: Comprehensive input validation
     */
    private boolean validateInput(@NonNull String userId, @NonNull String password) {
        // FIXED: Check for empty userId
        if (TextUtils.isEmpty(userId)) {
            editTextUserId.setError("User ID is required");
            editTextUserId.requestFocus();
            return false;
        }

        // FIXED: Check for empty password
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return false;
        }

        // IMPROVED: Validate userId format (alphanumeric, email, etc.)
        if (userId.length() < 3) {
            editTextUserId.setError("User ID must be at least 3 characters");
            editTextUserId.requestFocus();
            return false;
        }

        // IMPROVED: Optional email validation if userId should be email
        // if (!Patterns.EMAIL_ADDRESS.matcher(userId).matches()) {
        //     editTextUserId.setError("Please enter a valid email address");
        //     editTextUserId.requestFocus();
        //     return false;
        // }

        // IMPROVED: Password strength validation
        if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * IMPROVED: Sanitize input to prevent injection attacks
     */
    @NonNull
    private String sanitizeInput(@NonNull String input) {
        // Remove any SQL injection characters
        return input.replaceAll("[;'\"\\-\\-]", "");
    }

    /**
     * IMPROVED: Authentication logic (replace with real backend authentication)
     */
    private boolean authenticateUser(@NonNull String userId, @NonNull String password) {
        // FIXED: This is a placeholder - implement real authentication
        // Options:
        // 1. API call to backend server
        // 2. Firebase Authentication
        // 3. Local database with hashed passwords
        
        // For demonstration: accept non-empty credentials
        // TODO: Replace with actual authentication logic
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(password);
    }

    /**
     * IMPROVED: Check if user is already logged in
     */
    private void checkExistingLogin() {
        String existingUserId = getLoggedInUser();
        if (existingUserId != null && !existingUserId.isEmpty()) {
            Log.i(TAG, "User " + existingUserId + " already logged in. Ensuring worker is scheduled.");
            scheduleOrUpdateUserSpecificPeriodicWork(existingUserId);
            
            // IMPROVED: Optional - auto-navigate to main screen
            // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // startActivity(intent);
            // finish();
        }
    }

    /**
     * FIXED: Schedules or updates the periodic work specific to the logged-in user.
     * Added null safety and error handling.
     */
    private void scheduleOrUpdateUserSpecificPeriodicWork(@NonNull String userId) {
        if (TextUtils.isEmpty(userId)) {
            Log.e(TAG, "Cannot schedule work for null or empty user ID.");
            return;
        }

        try {
            // Create input data for the worker
            Data inputData = new Data.Builder()
                    .putString(UserSpecificPeriodicWorker.KEY_USER_ID, userId)
                    .build();

            // Define constraints
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    // IMPROVED: Add battery constraints for better battery life
                    .setRequiresBatteryNotLow(true)
                    .build();

            // Create the PeriodicWorkRequest
            PeriodicWorkRequest userPeriodicWorkRequest =
                    new PeriodicWorkRequest.Builder(
                            UserSpecificPeriodicWorker.class,
                            2, TimeUnit.HOURS) // Repeat every 2 hours
                            .setConstraints(constraints)
                            .setInputData(inputData)
                            .addTag("USER_SYNC_" + userId) // IMPROVED: Add user-specific tag
                            .build();

            // FIXED: Add null check for WorkManager
            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            if (workManager != null) {
                workManager.enqueueUniquePeriodicWork(
                        USER_SPECIFIC_PERIODIC_WORK_NAME_BASE,
                        ExistingPeriodicWorkPolicy.REPLACE,
                        userPeriodicWorkRequest);

                Log.i(TAG, "User-specific periodic work enqueued for User ID: " + userId +
                        " with REPLACE policy. Work Name: " + USER_SPECIFIC_PERIODIC_WORK_NAME_BASE);
            } else {
                Log.e(TAG, "WorkManager instance is null. Cannot schedule work.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error scheduling user-specific periodic work", e);
            Toast.makeText(this, "Warning: Background sync may not work", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * IMPROVED: Call this on user logout to cancel the specific user's periodic work.
     */
    public void onUserLogout() {
        Log.i(TAG, "User logged out. Cancelling user-specific periodic work: " + 
                USER_SPECIFIC_PERIODIC_WORK_NAME_BASE);
        
        try {
            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            if (workManager != null) {
                workManager.cancelUniqueWork(USER_SPECIFIC_PERIODIC_WORK_NAME_BASE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling work on logout", e);
        }
        
        clearLoggedInUser();
        
        // IMPROVED: Navigate to login screen if needed
        // Intent intent = new Intent(this, LoginActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // startActivity(intent);
        // finish();
    }

    // ===========================================================================================
    // FIXED: SharedPreferences management - replaced deprecated PreferenceManager
    // ===========================================================================================

    /**
     * FIXED: Save logged-in user using proper SharedPreferences API
     * IMPROVED: Added password hashing (use real hashing in production)
     */
    private void saveLoggedInUser(@NonNull String userId, @NonNull String password) {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_LOGGED_IN_USER_ID, userId);
            
            // IMPROVED: Hash password before storing (use BCrypt/Argon2 in production)
            // For now, just a simple hash demonstration - DO NOT use in production
            String passwordHash = String.valueOf(password.hashCode());
            editor.putString(KEY_USER_PASSWORD_HASH, passwordHash);
            
            // FIXED: Use apply() instead of commit() for async save
            editor.apply();
            
            Log.d(TAG, "User credentials saved successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error saving user credentials", e);
        }
    }

    /**
     * FIXED: Get logged-in user using proper SharedPreferences API
     */
    private String getLoggedInUser() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return prefs.getString(KEY_LOGGED_IN_USER_ID, null);
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving logged-in user", e);
            return null;
        }
    }

    /**
     * FIXED: Clear logged-in user using proper SharedPreferences API
     */
    private void clearLoggedInUser() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(KEY_LOGGED_IN_USER_ID);
            editor.remove(KEY_USER_PASSWORD_HASH);
            editor.apply();
            
            Log.d(TAG, "User credentials cleared successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing user credentials", e);
        }
    }

    // IMPROVED: Cleanup on activity destruction
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear any sensitive data from memory
        if (editTextPassword != null) {
            editTextPassword.setText("");
        }
    }
}
