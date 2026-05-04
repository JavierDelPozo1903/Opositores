package com.opositores.android.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.opositores.android.data.local.dao.BlockDao;
import com.opositores.android.data.local.dao.OppositionDao;
import com.opositores.android.data.local.dao.QuestionCacheDao;
import com.opositores.android.data.local.dao.TopicDao;
import com.opositores.android.data.local.entity.BlockEntity;
import com.opositores.android.data.local.entity.OppositionEntity;
import com.opositores.android.data.local.entity.QuestionCacheEntity;
import com.opositores.android.data.local.entity.TopicEntity;

@Database(
    entities = {OppositionEntity.class, BlockEntity.class, TopicEntity.class, QuestionCacheEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract OppositionDao oppositionDao();
    public abstract BlockDao blockDao();
    public abstract TopicDao topicDao();
    public abstract QuestionCacheDao questionCacheDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "opositores.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
