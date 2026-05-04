package com.opositores.android.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "blocks",
    foreignKeys = @ForeignKey(
        entity = OppositionEntity.class,
        parentColumns = "id",
        childColumns = "oppositionId",
        onDelete = ForeignKey.CASCADE
    )
)
public class BlockEntity {

    @PrimaryKey
    public long id;
    public long oppositionId;
    public String name;
    public double weight;
}
