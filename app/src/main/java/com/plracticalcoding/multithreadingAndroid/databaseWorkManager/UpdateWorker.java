package com.plracticalcoding.multithreadingAndroid.databaseWorkManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateWorker extends Worker {

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("UpdateWorker", "Updating data in the database...");
        try {
            Thread.sleep(1500); // Simulate work
        } catch (InterruptedException e) {
            return Result.failure();
        }
        Log.d("UpdateWorker", "Update complete.");
        return Result.success();
    }
}
