package com.plracticalcoding.multithreadingAndroid.workManager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public TaskEntity(String name) { this.name = name; }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
}
