// Create this file if it doesn't exist:
// E:/study/androidApps/app/src/main/java/com/plracticalcoding/multithreadingAndroid/UploadDatatoDB/InventoryDao.java

package com.plracticalcoding.multithreadingAndroid.UploadDatatoDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface InventoryDao {

    /**
     * Inserts one or more inventory items into the database.
     */
    @Insert
    void insert(InventoryItem... items);

    /**
     * Fetches all items that have not yet been uploaded to the server.
     * The `is_uploaded` column is used for this check.
     * @return A list of unsynced InventoryItem objects.
     */
    @Query("SELECT * FROM inventory_items WHERE is_uploaded = 0")
    List<InventoryItem> getUnsyncedItems();

    /**
     * Marks a list of items as uploaded by updating their 'is_uploaded' flag to true.
     * This method is called after a successful network sync.
     * @param itemIds A list of primary keys (id) for the items to be updated.
     */
    @Query("UPDATE inventory_items SET is_uploaded = 1 WHERE id IN (:itemIds)")
    void markItemsAsUploaded(List<Integer> itemIds);
}
