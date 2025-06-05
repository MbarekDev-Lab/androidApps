package com.plracticalcoding.multithreadingAndroid.UploadDatatoDB;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker; // Or Worker, CoroutineWorker
import androidx.work.WorkerParameters;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture; // Example for ListenableFuture
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserSpecificPeriodicWorker extends ListenableWorker {

    private static final String TAG = "UserSpecificWorker";
    public static final String KEY_USER_ID = "USER_ID"; // Key for input data

    private String userId;
    private ExecutorService backgroundExecutor;


    public UserSpecificPeriodicWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        // Retrieve the User ID passed to the worker
        userId = workerParams.getInputData().getString(KEY_USER_ID);
        backgroundExecutor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        SettableFuture<Result> future = SettableFuture.create();

        backgroundExecutor.execute(() -> {
            if (TextUtils.isEmpty(userId)) {
                Log.e(TAG, "User ID is missing. Cannot perform work. Failing permanently.");
                future.set(Result.failure()); // Or retry if appropriate, but usually failure for missing crucial data
                return;
            }

            Log.i(TAG, "Starting periodic work for User ID: " + userId);

            try {
                // --- Perform your user-specific background work here ---
                // Example: Sync data for this userId from local DB to server
                // boolean success = syncUserData(userId);
                Thread.sleep(5000); // Simulate work
                boolean success = Math.random() < 0.8; // Simulate 80% success
                // ----------------------------------------------------

                if (success) {
                    Log.i(TAG, "Periodic work for User ID: " + userId + " completed successfully.");
                    future.set(Result.success());
                } else {
                    Log.w(TAG, "Periodic work for User ID: " + userId + " failed. Retrying.");
                    future.set(Result.retry());
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Work for User ID: " + userId + " was interrupted.", e);
                Thread.currentThread().interrupt();
                future.set(Result.failure()); // Often best to fail on interruption
            } catch (Exception e) {
                Log.e(TAG, "Error during work for User ID: " + userId, e);
                future.set(Result.failure()); // Or retry based on exception type
            }
        });
        return future;
    }

    // private boolean syncUserData(String userId) { /* ... actual sync logic ... */ return true; }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.w(TAG, "Work for User ID: " + userId + " stopped. Shutting down executor.");
        if (backgroundExecutor != null && !backgroundExecutor.isShutdown()) {
            backgroundExecutor.shutdownNow();
        }
    }
}