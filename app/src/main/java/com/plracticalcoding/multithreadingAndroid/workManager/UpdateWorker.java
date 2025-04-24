package com.plracticalcoding.multithreadingAndroid.workManager;


import android.content.Context;

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
        try {

            YourDatabase db = YourDatabase.getInstance(getApplicationContext());
            db.taskDao().updateTaskNameById(1, "Updated Task Name");
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
