package com.opositores.android.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.opositores.android.data.local.entity.OppositionEntity;

import java.util.List;

@Dao
public interface OppositionDao {

    @Query("SELECT * FROM oppositions ORDER BY createdAt DESC")
    LiveData<List<OppositionEntity>> getAll();

    @Query("SELECT * FROM oppositions WHERE id = :id")
    OppositionEntity getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<OppositionEntity> oppositions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OppositionEntity opposition);

    @Delete
    void delete(OppositionEntity opposition);

    @Query("DELETE FROM oppositions")
    void deleteAll();
}
