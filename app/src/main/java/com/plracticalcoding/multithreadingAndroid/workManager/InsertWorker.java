package com.plracticalcoding.multithreadingAndroid.workManager;

import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.content.Context;

import com.plracticalcoding.multithreadingAndroid.UploadDatatoDB.AppDatabase;

public class InsertWorker extends Worker {
    public InsertWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            db.taskDao().insert(new TaskEntity("Inserted from Worker"));
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
