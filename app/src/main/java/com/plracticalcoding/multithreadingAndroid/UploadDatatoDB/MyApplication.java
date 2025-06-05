package com.plracticalcoding.multithreadingAndroid.UploadDatatoDB;

import android.app.Application;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture; // For observing work

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    public static final String UNIQUE_UPLOAD_WORK_NAME = "PeriodicInventoryUpload"; // Made public for potential external reference

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application onCreate: Initializing and scheduling periodic work.");
        schedulePeriodicUploadWork();

        // Optional: Log the status of the work for debugging after scheduling
        // This is for demonstration; you might not need it in production this verbosely.
        // observeWorkStatus();
    }

    /**
     * Schedules the periodic inventory upload worker.
     * This method is designed to be called once during application setup.
     */
    private void schedulePeriodicUploadWork() {
        // --- Define Constraints for the work ---
        // Work will only run if these conditions are met.
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Essential for upload tasks
                // .setRequiresCharging(true) // Consider for battery saving
                // .setRequiresBatteryNotLow(true) // Consider for battery saving
                .build();

        // --- Create the PeriodicWorkRequest ---
        // Define the worker, repeat interval, and constraints.
        // Repeat interval must be at least PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS (15 minutes).
        PeriodicWorkRequest periodicUploadRequest =
                new PeriodicWorkRequest.Builder(
                        UploadListenableWorker.class,         // Your worker class
                        1, TimeUnit.HOURS)                    // Example: Repeat every 1 HOUR
                        // For more frequent, but respecting system optimizations:
                        // PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .addTag(UNIQUE_UPLOAD_WORK_NAME)      // Tag for easier querying (optional but good)
                        // .setInitialDelay(5, TimeUnit.MINUTES) // Optional: If you want to delay the first run
                        // .setBackoffCriteria( // Optional: Customize how retries are handled
                        //        BackoffPolicy.EXPONENTIAL,
                        //        PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                        //        TimeUnit.MILLISECONDS)
                        .build();

        // --- Enqueue the Unique Periodic Work ---
        // This ensures that if this work is already scheduled with the same unique name,
        // it won't be scheduled again if using ExistingPeriodicWorkPolicy.KEEP.
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                UNIQUE_UPLOAD_WORK_NAME,             // A unique name to identify this periodic work
                ExistingPeriodicWorkPolicy.KEEP,     // Policy for existing work:
                                                     // KEEP: If work with this name exists, do nothing.
                                                     // REPLACE: If work exists, cancel it and schedule the new one.
                periodicUploadRequest);

        Log.i(TAG, "Periodic upload worker '" + UNIQUE_UPLOAD_WORK_NAME + "' enqueued with KEEP policy.");
        Log.i(TAG, "It will attempt to run approximately every " +
                periodicUploadRequest.getWorkSpec().getIntervalDuration() / (60 * 1000) + " minutes, " +
                "subject to constraints and system optimizations.");
    }

    /**
     * Optional: Helper method to observe and log the status of the enqueued work.
     * This is useful for debugging to confirm the work is scheduled.
     */
    @SuppressWarnings("unused") // Suppress warnings if not always called
    private void observeWorkStatus() {
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        ListenableFuture<List<WorkInfo>> workInfosFuture = workManager.getWorkInfosForUniqueWork(UNIQUE_UPLOAD_WORK_NAME);

        try {
            // This is a blocking call, so do it carefully or use LiveData for UI observation.
            // For Application.onCreate(), a quick synchronous check like this is usually fine.
            List<WorkInfo> workInfos = workInfosFuture.get(); // Blocks until the future is complete
            if (workInfos == null || workInfos.isEmpty()) {
                Log.w(TAG, "No WorkInfo found for unique work: " + UNIQUE_UPLOAD_WORK_NAME + ". It might not be enqueued yet or was cancelled.");
            } else {
                for (WorkInfo workInfo : workInfos) {
                    Log.i(TAG, "Status for '" + UNIQUE_UPLOAD_WORK_NAME + "': " +
                            "State=" + workInfo.getState() +
                            ", ID=" + workInfo.getId() +
                            ", Tags=" + workInfo.getTags());
                    // For periodic work, the state will likely be ENQUEUED initially.
                    // It transitions to RUNNING when executing, and then back to ENQUEUED for the next period.
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Failed to get WorkInfo for " + UNIQUE_UPLOAD_WORK_NAME, e);
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }
}