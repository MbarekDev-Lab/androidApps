package com.plracticalcoding.multithreadingAndroid.workManager;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(TaskEntity task);

    @Query("UPDATE tasks SET name = :name WHERE id = :id")
    void updateTaskNameById(int id, String name);

    @Query("SELECT * FROM tasks")
    List<TaskEntity> getAllTasks();
}
