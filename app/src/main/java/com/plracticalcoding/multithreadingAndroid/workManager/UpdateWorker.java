package com.plracticalcoding.multithreadingAndroid.workManager;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.plracticalcoding.multithreadingAndroid.UploadDatatoDB.AppDatabase;

public class UpdateWorker extends Worker {
    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            db.taskDao().updateTaskNameById(1, "Updated Task Name");
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
