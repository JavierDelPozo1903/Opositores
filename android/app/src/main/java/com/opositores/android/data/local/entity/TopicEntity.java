package com.opositores.android.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "topics",
    foreignKeys = @ForeignKey(
        entity = BlockEntity.class,
        parentColumns = "id",
        childColumns = "blockId",
        onDelete = ForeignKey.CASCADE
    )
)
public class TopicEntity {

    @PrimaryKey
    public long id;
    public long blockId;
    public String officialNumber;
    public String title;
    public int difficulty;
    public int priority;
    public String status;
    public int reviewCount;
}
