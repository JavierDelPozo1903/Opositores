package com.opositores.android.data.local.dao;

import androidx.room.*;

import com.opositores.android.data.local.entity.QuestionCacheEntity;

import java.util.List;

@Dao
public interface QuestionCacheDao {

    @Query("SELECT * FROM question_cache WHERE topicId IN (:topicIds) LIMIT :limit")
    List<QuestionCacheEntity> getByTopics(List<Long> topicIds, int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuestionCacheEntity> questions);

    /** Limpia preguntas antiguas (más de 7 días) */
    @Query("DELETE FROM question_cache WHERE cachedAt < :cutoff")
    void deleteOlderThan(long cutoff);
}
