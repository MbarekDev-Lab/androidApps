package com.plracticalcoding.multithreading.UploadDatatoDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * FIXED: Created missing InventoryItem entity class
 * This entity represents an inventory item in the Room database
 */
@Entity(tableName = "inventory_items")
public class InventoryItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "item_name")
    public String itemName;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "is_uploaded")
    public boolean isUploaded; // Flag to track if item has been synced to server

    @ColumnInfo(name = "created_at")
    public long createdAt; // Timestamp

    @ColumnInfo(name = "updated_at")
    public long updatedAt; // Timestamp

    // IMPROVED: Default constructor
    public InventoryItem() {
        this.isUploaded = false;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // IMPROVED: Constructor with parameters
    public InventoryItem(String itemName, int quantity, double price, String category) {
        this();
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    // IMPROVED: Override toString for debugging
    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", isUploaded=" + isUploaded +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
