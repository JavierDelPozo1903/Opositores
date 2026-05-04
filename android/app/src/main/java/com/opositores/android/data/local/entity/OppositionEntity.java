package com.opositores.android.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "oppositions")
public class OppositionEntity {

    @PrimaryKey
    public long id;
    public String name;
    public String scope;
    public String targetExamDate;
    public double hoursPerWeek;
    public String createdAt;
}
