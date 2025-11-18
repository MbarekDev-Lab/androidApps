package com.plracticalcoding.multithreading.UploadDatatoDB;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * FIXED: Complete rewrite addressing all critical bugs:
 * - Fixed blocking .get() call on main thread
 * - Made observeWorkStatus() async using LiveData observer
 * - Added proper error handling
 * - Improved WorkManager configuration
 * - Added null safety checks
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    public static final String UNIQUE_UPLOAD_WORK_NAME = "PeriodicInventoryUpload";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application onCreate: Initializing and scheduling periodic work.");
        
        try {
            schedulePeriodicUploadWork();
            
            // IMPROVED: Observe work status asynchronously (not blocking main thread)
            observeWorkStatusAsync();
        } catch (Exception e) {
            Log.e(TAG, "Error during application initialization", e);
        }
    }

    /**
     * FIXED: Schedules the periodic inventory upload worker with proper error handling
     */
    private void schedulePeriodicUploadWork() {
        try {
            // Define constraints for the work
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    // IMPROVED: Add battery constraints for better battery life
                    .setRequiresBatteryNotLow(true)
                    // .setRequiresCharging(true) // Uncomment if upload should only happen while charging
                    .build();

            // Create the PeriodicWorkRequest
            // Minimum interval is 15 minutes (PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS)
            PeriodicWorkRequest periodicUploadRequest =
                    new PeriodicWorkRequest.Builder(
                            UploadListenableWorker.class,
                            1, TimeUnit.HOURS) // Repeat every 1 hour
                            .setConstraints(constraints)
                            .addTag(UNIQUE_UPLOAD_WORK_NAME)
                            // IMPROVED: Add initial delay if needed
                            // .setInitialDelay(5, TimeUnit.MINUTES)
                            // IMPROVED: Add backoff policy for retries
                            // .setBackoffCriteria(
                            //     BackoffPolicy.EXPONENTIAL,
                            //     PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                            //     TimeUnit.MILLISECONDS)
                            .build();

            // FIXED: Add null check for WorkManager
            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            if (workManager != null) {
                // Enqueue the unique periodic work
                workManager.enqueueUniquePeriodicWork(
                        UNIQUE_UPLOAD_WORK_NAME,
                        ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if already scheduled
                        periodicUploadRequest);

                Log.i(TAG, "Periodic upload worker '" + UNIQUE_UPLOAD_WORK_NAME + 
                        "' enqueued with KEEP policy.");
                Log.i(TAG, "Worker will run approximately every 1 hour, subject to constraints.");
            } else {
                Log.e(TAG, "WorkManager instance is null. Cannot schedule work.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error scheduling periodic upload work", e);
        }
    }

    /**
     * FIXED: Async observation of work status (not blocking main thread)
     * Uses LiveData observer instead of blocking .get() call
     */
    private void observeWorkStatusAsync() {
        try {
            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            if (workManager == null) {
                Log.w(TAG, "WorkManager is null. Cannot observe work status.");
                return;
            }

            // FIXED: Use LiveData observer instead of blocking .get()
            workManager.getWorkInfosForUniqueWorkLiveData(UNIQUE_UPLOAD_WORK_NAME)
                    .observeForever(workInfos -> {
                        if (workInfos == null || workInfos.isEmpty()) {
                            Log.w(TAG, "No WorkInfo found for: " + UNIQUE_UPLOAD_WORK_NAME);
                            return;
                        }

                        // Log status of all work instances
                        for (WorkInfo workInfo : workInfos) {
                            logWorkInfo(workInfo);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error observing work status", e);
        }
    }

    /**
     * IMPROVED: Helper method to log work info details
     */
    private void logWorkInfo(@NonNull WorkInfo workInfo) {
        Log.i(TAG, "Work Status for '" + UNIQUE_UPLOAD_WORK_NAME + "': " +
                "State=" + workInfo.getState() +
                ", ID=" + workInfo.getId() +
                ", RunAttempt=" + workInfo.getRunAttemptCount() +
                ", Tags=" + workInfo.getTags());

        // Log additional details based on state
        switch (workInfo.getState()) {
            case ENQUEUED:
                Log.d(TAG, "Work is enqueued and waiting to run");
                break;
            case RUNNING:
                Log.d(TAG, "Work is currently running");
                break;
            case SUCCEEDED:
                Log.d(TAG, "Work completed successfully");
                break;
            case FAILED:
                Log.e(TAG, "Work failed permanently");
                break;
            case BLOCKED:
                Log.w(TAG, "Work is blocked by prerequisites");
                break;
            case CANCELLED:
                Log.w(TAG, "Work was cancelled");
                break;
        }
    }

    /**
     * IMPROVED: Method to cancel all scheduled work (useful for debugging/testing)
     */
    public void cancelAllWork() {
        try {
            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            if (workManager != null) {
                workManager.cancelUniqueWork(UNIQUE_UPLOAD_WORK_NAME);
                Log.i(TAG, "Cancelled all scheduled work");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling work", e);
        }
    }

    /**
     * IMPROVED: Method to manually trigger one-time upload (useful for testing)
     */
    public void triggerImmediateUpload() {
        try {
            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            if (workManager != null) {
                // Create one-time work request
                androidx.work.OneTimeWorkRequest immediateUpload =
                        new androidx.work.OneTimeWorkRequest.Builder(UploadListenableWorker.class)
                                .addTag("IMMEDIATE_UPLOAD")
                                .build();

                workManager.enqueue(immediateUpload);
                Log.i(TAG, "Triggered immediate upload");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error triggering immediate upload", e);
        }
    }
}
