package com.plracticalcoding.multithreadingAndroid.databaseWorkManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class InsertWorker extends Worker {

    public InsertWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("InsertWorker", "Inserting data into the database...");
        try {
            Thread.sleep(2000); // Simulate work
        } catch (InterruptedException e) {
            return Result.failure();
        }
        Log.d("InsertWorker", "Insert complete.");
        return Result.success();
    }
}
