package com.plracticalcoding.multithreading.worker;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;
import com.plracticalcoding.db.AppDatabase;
import com.plracticalcoding.db.MyEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UploadWorker extends ListenableWorker {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final AppDatabase db;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        db = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        @SuppressLint("RestrictedApi") SettableFuture<Result> future = SettableFuture.create();

        CompletableFuture.supplyAsync(this::fetchPendingData, executor)
            .thenApplyAsync(this::uploadDataToServer, executor)
            .thenAccept(success -> future.set(success ? Result.success() : Result.retry()))
            .exceptionally(ex -> {
                ex.printStackTrace();
                future.set(Result.retry());
                return null;
            });

        return future;
    }

    private List<MyEntity> fetchPendingData() {
        return db.myDao().getPendingData();
    }

    private boolean uploadDataToServer(List<MyEntity> dataList) {
        for (MyEntity data : dataList) {
            if (sendToServer(data)) {
                data.uploaded = true;
                db.myDao().update(data);
            } else {
                return false; // fail fast
            }
        }
        return true;
    }

    private boolean sendToServer(MyEntity data) {
        try (Socket socket = new Socket("192.168.1.100", 1433);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(data.toString());
            String response = in.readLine();
            return "OK".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


private void userInActivity(){
    PeriodicWorkRequest uploadRequest = new PeriodicWorkRequest.Builder(UploadWorker.class
            , 15, TimeUnit.MINUTES)
                    .setConstraints(new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    .build();

    WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
            "UploadWorkerTask",
            ExistingPeriodicWorkPolicy.KEEP,
            uploadRequest
    );

}
}

