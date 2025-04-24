package com.plracticalcoding.multithreadingAndroid.databaseWorkManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FetchWorker extends Worker {

    public FetchWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("FetchWorker", "Fetching data from the database...");
        try {
            Thread.sleep(1000); // Simulate work
        } catch (InterruptedException e) {
            return Result.failure();
        }
        Log.d("FetchWorker", "Fetch complete.");
        return Result.success();
    }
}
