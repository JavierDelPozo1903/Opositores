package com.opositores.android.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.opositores.android.data.local.entity.BlockEntity;

import java.util.List;

@Dao
public interface BlockDao {

    @Query("SELECT * FROM blocks WHERE oppositionId = :oppositionId")
    LiveData<List<BlockEntity>> getByOpposition(long oppositionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BlockEntity> blocks);

    @Query("DELETE FROM blocks WHERE oppositionId = :oppositionId")
    void deleteByOpposition(long oppositionId);
}
