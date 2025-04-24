package com.plracticalcoding.multithreadingAndroid.databaseWorkManager;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.List;

public class DatabaseWorkManager {
    private final Context context;

    public DatabaseWorkManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void startSequentialDatabaseWork(LifecycleOwner lifecycleOwner) {

        WorkRequest insertWork = new OneTimeWorkRequest.Builder(InsertWorker.class).build();
        WorkRequest updateWork = new OneTimeWorkRequest.Builder(UpdateWorker.class).build();
        WorkRequest fetchWork = new OneTimeWorkRequest.Builder(FetchWorker.class).build();

        WorkContinuation continuation = WorkManager.getInstance(context)
                .beginWith((OneTimeWorkRequest) insertWork)
                .then((OneTimeWorkRequest) updateWork)
                .then((OneTimeWorkRequest) fetchWork);

        continuation.enqueue();

        continuation.getWorkInfosLiveData().observe(lifecycleOwner, workInfos -> {
            if (workInfos != null && !workInfos.isEmpty()) {
                WorkInfo lastWork = workInfos.get(workInfos.size() - 1);
                if (lastWork.getState().isFinished()) {
                    Log.d("DatabaseWorkManager", "All tasks finished!");
                }
            }
        });
    }
}
