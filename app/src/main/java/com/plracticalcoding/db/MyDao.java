package com.plracticalcoding.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao {
    @Query("SELECT * FROM my_entity_table")
    List<MyEntity> getAllEntities();
    
    @Query("SELECT * FROM my_entity_table WHERE uploaded = 0")
    List<MyEntity> getPendingData();
    
    @Update
    void update(MyEntity entity);
}
