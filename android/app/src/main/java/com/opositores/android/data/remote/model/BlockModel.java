package com.opositores.android.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class BlockModel {

    @SerializedName("id")
    public long id;
    @SerializedName("oppositionId")
    public long oppositionId;
    @SerializedName("name")
    public String name;
    @SerializedName("weight")
    public Double weight;
}
