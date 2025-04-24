package com.plracticalcoding.multithreadingAndroid.workManager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskEntity.class}, version = 1)
public abstract class YourDatabase extends RoomDatabase {

    private static volatile YourDatabase INSTANCE;

    public abstract TaskDao taskDao();

    public static YourDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (YourDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        YourDatabase.class,
                        "your_database_name"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
