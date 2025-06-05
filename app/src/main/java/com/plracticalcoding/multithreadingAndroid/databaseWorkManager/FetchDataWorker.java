package com.plracticalcoding.multithreadingAndroid.databaseWorkManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import androidx.work.ListenableWorker.Result;

import com.plracticalcoding.db.AppDatabase;
import com.plracticalcoding.db.MyDao;
import com.plracticalcoding.db.MyEntity;

public class FetchDataWorker extends Worker {
    private static final String TAG = "FetchDataWorker";

    public FetchDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: Starting to fetch data from database");
        try {
            AppDatabase db = Room.databaseBuilder(
                    getApplicationContext(),
                    AppDatabase.class, "my-database"
            ).build();
            MyDao myDao = db.myDao();

            // Fetch all entities from the database:
            List<MyEntity> entities = myDao.getAllEntities();
            Log.d(TAG, "doWork: Fetched " + entities.size() + " entities from the database");

            // Create a String Builder to store the results:
            StringBuilder dataStringBuilder = new StringBuilder();

            // Process data and add to the String Builder:
            for (MyEntity entity : entities) {
                dataStringBuilder.append("ID: ").append(entity.id).append(", Data: ").append(entity.data).append("\n");
            }

            // Create output data:
            Data outputData = new Data.Builder()
                    .putString("db_data", dataStringBuilder.toString())
                    .build();

            db.close();//Close database
            Log.d(TAG, "doWork: Fetching data from database complete");
            // Return success with the output data:
            return Result.success(outputData);
        } catch (Exception e) {
            Log.e(TAG, "doWork: Error fetching data from database", e);
            // Return failure
            return Result.failure();
        }
    }
}