package com.plracticalcoding.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_entity_table") // The tableName is optional, but recommended.
public class MyEntity {

    public String data;
    public boolean uploaded = false;


    @PrimaryKey(autoGenerate = true)
    private int id;

    // You can add other columns here as member variables.
    // For example:
    // private String name;

    // --- Getters and setters for your fields ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // public String getName() {
    //     return name;
    // }
    //
    // public void setName(String name) {
    //     this.name = name;
    // }
}
