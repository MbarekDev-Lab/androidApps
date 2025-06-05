package com.plracticalcoding.multithreadingAndroid.UploadDatatoDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.plracticalcoding.multithreadingAndroid.workManager.TaskDao;
import com.plracticalcoding.multithreadingAndroid.workManager.TaskEntity;

@Database(entities = {TaskEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract TaskDao taskDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "your_database_name"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
