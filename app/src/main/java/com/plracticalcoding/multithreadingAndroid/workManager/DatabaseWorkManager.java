package com.plracticalcoding.multithreadingAndroid.workManager;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class DatabaseWorkManager {
    private final Context context;

    public DatabaseWorkManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void startSequentialDatabaseWork(LifecycleOwner lifecycleOwner) {

        WorkRequest insertWork = new OneTimeWorkRequest.Builder(InsertWorker.class).build();
        WorkRequest updateWork = new OneTimeWorkRequest.Builder(UpdateWorker.class).build();
        WorkRequest fetchWork = new OneTimeWorkRequest.Builder(FetchWorker.class).build();

        WorkManager.getInstance(context)
                .beginWith((OneTimeWorkRequest) insertWork)    // Insert first
                .then((OneTimeWorkRequest) updateWork)         // Update second
                .then((OneTimeWorkRequest) fetchWork)          // Fetch third
                .enqueue();

        WorkManager.getInstance(context)
                .getWorkInfoByIdLiveData(fetchWork.getId())
                .observe(lifecycleOwner, workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        Log.d("DatabaseWorkManager", "All tasks finished!");
                        // Safe to update UI here.
                    }
                });
    }
}
