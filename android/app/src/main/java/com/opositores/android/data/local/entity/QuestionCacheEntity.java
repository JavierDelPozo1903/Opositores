package com.opositores.android.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Caché de preguntas usadas recientemente para permitir tests offline básicos */
@Entity(tableName = "question_cache")
public class QuestionCacheEntity {

    @PrimaryKey
    public long id;
    public long topicId;
    public String questionText;
    public String options;   // JSON array
    public String correctAnswer;
    public String type;
    public long cachedAt;    // epoch millis
}
