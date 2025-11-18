package com.plracticalcoding.multithreading.UploadDatatoDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    public boolean isUploaded;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public InventoryItem() {
        this.isUploaded = false;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    @Ignore
    public InventoryItem(String itemName, int quantity, double price, String category) {
        this();
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

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
