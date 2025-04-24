package com.plracticalcoding.multithreadingAndroid.workManager;

import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.content.Context;

public class InsertWorker extends Worker {
    public InsertWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            YourDatabase db = YourDatabase.getInstance(getApplicationContext());
            db.taskDao().insert(new TaskEntity("Inserted from Worker"));
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
