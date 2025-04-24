package com.plracticalcoding.multithreadingAndroid.workManager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.Worker;
import android.content.Context;

import java.util.List;

public class FetchWorker extends Worker {
    public FetchWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            YourDatabase db = YourDatabase.getInstance(getApplicationContext());
            List<TaskEntity> tasks = db.taskDao().getAllTasks();

            Log.d("FetchWorker", "Fetched " + tasks.size() + " tasks.");

            for (TaskEntity task : tasks) {
                Log.d("FetchWorker", "Task: " + task.getName());
            }

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
