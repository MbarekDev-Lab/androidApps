package com.plracticalcoding.multithreadingAndroid.UploadDatatoDB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * FIXED: Complete rewrite addressing all critical bugs:
 * - Fixed entity reference (changed from TaskEntity to InventoryItem)
 * - Fixed volatile keyword for proper double-checked locking
 * - Added proper database name
 * - Added fallback migration strategy
 * - Improved thread safety
 */
@Database(entities = {InventoryItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // FIXED: Added volatile for proper double-checked locking thread safety
    private static volatile AppDatabase INSTANCE;
    
    private static final String DATABASE_NAME = "inventory_database";

    public abstract InventoryDao inventoryDao(); // FIXED: Proper DAO reference

    /**
     * FIXED: Thread-safe singleton with proper volatile and double-checked locking
     */
    public static AppDatabase getInstance(@NonNull Context context) {
        // IMPROVED: First check without synchronization for performance
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                // FIXED: Second check inside synchronized block
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    /**
     * IMPROVED: Separate method for database building with proper configuration
     */
    @NonNull
    private static AppDatabase buildDatabase(@NonNull Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DATABASE_NAME)
                // IMPROVED: Add migration strategy
                .fallbackToDestructiveMigration() // For development - remove in production
                // IMPROVED: Enable multi-instance invalidation for testing
                // .enableMultiInstanceInvalidation()
                .build();
    }

    /**
     * IMPROVED: Method to get database instance (alternative naming)
     * This maintains compatibility with the UploadListenableWorker code
     */
    public static AppDatabase getDatabase(@NonNull Context context) {
        return getInstance(context);
    }

    /**
     * IMPROVED: Method to close database (useful for testing)
     * Call this only during app shutdown or testing
     */
    public static void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}
