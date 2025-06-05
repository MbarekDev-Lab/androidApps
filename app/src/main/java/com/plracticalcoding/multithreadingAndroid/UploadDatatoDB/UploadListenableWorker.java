package com.plracticalcoding.multithreadingAndroid.UploadDatatoDB;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.google.common.util.concurrent.ListenableFuture; // This is the return type for startWork()


// Assuming your networking client (e.g., Retrofit service)
// import com.yourpackage.network.ApiClient; // Your network client
// import com.yourpackage.network.ApiService; // Your Retrofit/other API service

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UploadListenableWorker extends ListenableWorker {

    private static final String TAG = "UploadListenableWrkr";

    private final com.plracticalcoding.multithreadingAndroid.UploadDatatoDB.InventoryDao inventoryDao;
    private final ExecutorService databaseExecutor;
    private final ExecutorService networkExecutor;
    // private final ApiService apiService; // Example: your Retrofit service

    // This holds the main CompletableFuture chain to allow for potential cancellation.
    private volatile CompletableFuture<Result> mainTaskFuture;


    public UploadListenableWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        SQLiteCursor AppDatabase = null;
        inventoryDao = AppDatabase.getDatabase(getApplicationContext()).inventoryDao();
        // apiService = ApiClient.getApiService(); // Initialize your API service

        // Using single thread executor for database access to ensure sequential operations if needed,
        // though Room handles its own synchronization for the most part.
        databaseExecutor = Executors.newSingleThreadExecutor();

        // Using a fixed thread pool for network operations. Adjust size based on needs.
        // Consider Executors.newCachedThreadPool() if task numbers vary greatly and can be short-lived.
        networkExecutor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2));
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Log.i(TAG, "startWork: Initiating inventory upload sequence.");

        // ResolvableFuture is the ListenableFuture we will return to WorkManager
        ResolvableFuture<Result> resolvableFuture = ResolvableFuture.create();

        // The main CompletableFuture chain
        mainTaskFuture = CompletableFuture.supplyAsync(() -> {
            // Stage 1: Fetch unsynced items from Room DB
            Log.d(TAG, "Stage 1: Fetching unsynced items from DB on thread: " + Thread.currentThread().getName());
            if (Thread.currentThread().isInterrupted()) { // Check for early interruption
                throw new InterruptedException("Task was interrupted before fetching items.");
            }
            return inventoryDao.getUnsyncedItems();
        }, databaseExecutor)
        .thenComposeAsync(itemsToUpload -> {
            // Stage 2: Upload items to server
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Task was interrupted before uploading items.");
            }
            if (itemsToUpload == null || itemsToUpload.isEmpty()) {
                Log.i(TAG, "Stage 2: No items to upload. Completing with success.");
                return CompletableFuture.completedFuture(true); // True indicates "upload phase" was successful (vacuously)
            }
            Log.i(TAG, "Stage 2: Found " + itemsToUpload.size() + " items. Starting network upload on thread: " + Thread.currentThread().getName());
            return performNetworkUpload(itemsToUpload) // This returns CompletableFuture<Boolean>
                .thenApplyAsync(uploadSuccessful -> {
                    if (uploadSuccessful) {
                        Log.i(TAG, "Stage 2.1: Upload successful. Marking " + itemsToUpload.size() + " items as uploaded in DB.");
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException("Task was interrupted before marking items.");
                        }
                        markItemsAsUploadedInDb(itemsToUpload); // Blocking I/O on databaseExecutor
                    }
                    return uploadSuccessful; // Pass through the success status
                }, databaseExecutor); // Execute DB marking on databaseExecutor
        }, networkExecutor) // Execute network upload and its dependent DB marking trigger on network/database executors respectively
        .handleAsync((uploadPhaseSuccessful, error) -> {
            // Stage 3: Handle outcome and determine final Result
            if (error != null) {
                Log.e(TAG, "Stage 3: Upload sequence failed with an exception.", error);
                if (error instanceof InterruptedException || (error.getCause() != null && error.getCause() instanceof InterruptedException) ) {
                    Log.w(TAG, "Stage 3: Task was interrupted. Likely due to cancellation.");
                    // If interrupted, it's often best not to retry immediately as the work was explicitly stopped.
                    // However, WorkManager might retry later based on its own policies if not Result.failure().
                    // For explicit cancellation, Result.failure() might be more appropriate if retries are not desired.
                    // Let's assume failure on interruption to prevent immediate retry by this specific logic.
                    return Result.failure(); // Or handle as per specific needs for interruption
                }
                // For other errors, decide on retry or failure.
                // Example: Retry for network-like issues, fail for data issues.
                // This is a generic retry, more specific error checking could be added.
                return Result.retry();
            }

            if (uploadPhaseSuccessful) {
                Log.i(TAG, "Stage 3: Upload sequence completed successfully.");
                return Result.success();
            } else {
                Log.w(TAG, "Stage 3: Upload phase reported failure, but no exception. Retrying.");
                return Result.retry(); // Or Result.failure() if this state implies non-retryable error
            }
        }, databaseExecutor); // Final handling can also be on a specific executor or default (ForkJoinPool)


        // Link the CompletableFuture's outcome to the ResolvableFuture
        mainTaskFuture.whenComplete((result, throwable) -> {
            if (resolvableFuture.isCancelled()) { // Check if WorkManager cancelled us already
                return;
            }
            if (throwable != null) {
                // This throwable is from the CompletableFuture's exceptional completion.
                // The handleAsync stage should ideally transform this into a Result.
                // If it still reaches here, it means the handleAsync itself failed or wasn't setup for all errors.
                Log.e(TAG, "Critical error in CompletableFuture chain, propagating exception to ResolvableFuture.", throwable);
                resolvableFuture.setException(throwable);
            } else {
                resolvableFuture.set(result);
            }
        });

        // Handle cancellation from WorkManager
        resolvableFuture.addListener(() -> {
            if (resolvableFuture.isCancelled()) {
                Log.w(TAG, "WorkManager cancelled the ListenableFuture. Attempting to cancel CompletableFuture chain.");
                if (mainTaskFuture != null) {
                    // CompletableFuture's cancel(true) attempts to interrupt the thread if running.
                    // This relies on tasks within the CompletableFuture checking Thread.isInterrupted().
                    mainTaskFuture.cancel(true);
                }
            }
        }, Runnable::run); // Or specify an executor like MoreExecutors.directExecutor() from Guava

        return resolvableFuture;
    }

    /**
     * Performs the actual network upload.
     * @param itemsToUpload List of items to upload.
     * @return CompletableFuture<Boolean> indicating success or failure of the upload.
     */
    private CompletableFuture<Boolean> performNetworkUpload(List<InventoryItem> itemsToUpload) {
        // This is where you'd use your actual networking client (e.g., Retrofit)
        // For Retrofit, you might adapt Call.enqueue to CompletableFuture.

        // --- SIMULATION ---
        return CompletableFuture.supplyAsync(() -> {
            Log.d(TAG, "performNetworkUpload: Simulating for " + itemsToUpload.size() + " items on thread: " + Thread.currentThread().getName());
            try {
                // Simulate network latency
                for (int i = 0; i < 3; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Network upload interrupted during delay.");
                    }
                    Thread.sleep(1000);
                }

                // Simulate potential network failure
                if (Math.random() < 0.2) { // 20% chance of failure
                    Log.e(TAG, "performNetworkUpload: Simulated network failure.");
                    throw new IOException("Simulated network I/O error");
                }

                Log.i(TAG, "performNetworkUpload: Simulated network upload successful.");
                return true; // Indicate success
            } catch (InterruptedException e) {
                Log.w(TAG, "performNetworkUpload: Interrupted during simulation.", e);
                Thread.currentThread().interrupt(); // Preserve interrupt status
                throw new RuntimeException(e); // Propagate as exceptional completion
            } catch (IOException e) {
                Log.e(TAG, "performNetworkUpload: IOException during simulation.", e);
                throw new RuntimeException(e); // Propagate as exceptional completion
            }
        }, networkExecutor);
        // --- END SIMULATION ---

        /*
        // --- EXAMPLE WITH RETROFIT (requires adapting Call to CompletableFuture) ---
        // return CompletableFuture.supplyAsync(() -> {
        //     try {
        //         Response<Void> response = apiService.uploadInventoryItems(itemsToUpload).execute(); // Synchronous call
        //         if (response.isSuccessful()) {
        //             return true;
        //         } else {
        //             Log.e(TAG, "Network upload failed with code: " + response.code());
        //             // Consider throwing a specific exception for different error codes
        //             throw new IOException("Server error: " + response.code());
        //         }
        //     } catch (IOException e) {
        //         Log.e(TAG, "Network IOException during upload.", e);
        //         throw new RuntimeException(e); // CompletableFuture will handle this as exceptional completion
        //     }
        // }, networkExecutor);
        */
    }

    private void markItemsAsUploadedInDb(List<InventoryItem> uploadedItems) {
        Log.d(TAG, "markItemsAsUploadedInDb: Marking " + uploadedItems.size() + " items on thread: " + Thread.currentThread().getName());
        try {
            List<Integer> itemIds = new ArrayList<>();
            for (InventoryItem item : uploadedItems) {
                itemIds.add(item.id);
            }
            if (!itemIds.isEmpty()) {
                inventoryDao.markItemsAsUploaded(itemIds); // This is a blocking call
            }
        } catch (Exception e) {
            Log.e(TAG, "Error marking items as uploaded in DB.", e);
            // This exception, if it occurs, will propagate up the CompletableFuture chain
            // and be caught by the .handleAsync() stage.
            throw new RuntimeException("Failed to mark items in DB", e);
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.w(TAG, "onStopped: Worker is being stopped. Cleaning up resources.");

        // The resolvableFuture.addListener for cancellation should have already
        // tried to cancel mainTaskFuture. Here, we focus on executor shutdown.

        shutdownExecutorService(databaseExecutor, "DatabaseExecutor");
        shutdownExecutorService(networkExecutor, "NetworkExecutor");
    }

    private void shutdownExecutorService(ExecutorService executor, String serviceName) {
        if (executor != null && !executor.isShutdown()) {
            Log.d(TAG, "Attempting to shutdown " + serviceName + "...");
            executor.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    Log.w(TAG, serviceName + " did not terminate in 5s. Forcing shutdown...");
                    executor.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                        Log.e(TAG, serviceName + " did not terminate even after forcing.");
                    } else {
                        Log.d(TAG, serviceName + " terminated after forcing.");
                    }
                } else {
                    Log.d(TAG, serviceName + " terminated gracefully.");
                }
            } catch (InterruptedException ie) {
                Log.w(TAG, serviceName + " shutdown interrupted. Forcing now.", ie);
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}