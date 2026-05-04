package com.opositores.android.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.opositores.android.data.local.entity.TopicEntity;

import java.util.List;

@Dao
public interface TopicDao {

    @Query("SELECT * FROM topics WHERE blockId = :blockId ORDER BY priority DESC")
    LiveData<List<TopicEntity>> getByBlock(long blockId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TopicEntity> topics);

    @Update
    void update(TopicEntity topic);

    @Query("DELETE FROM topics WHERE blockId = :blockId")
    void deleteByBlock(long blockId);
}
