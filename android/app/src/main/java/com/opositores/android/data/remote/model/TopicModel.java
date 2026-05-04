package com.opositores.android.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class TopicModel {

    @SerializedName("id")
    public long id;
    @SerializedName("blockId")
    public long blockId;
    @SerializedName("officialNumber")
    public String officialNumber;
    @SerializedName("title")
    public String title;
    @SerializedName("difficulty")
    public int difficulty;
    @SerializedName("priority")
    public int priority;
    @SerializedName("status")
    public String status;
    @SerializedName("reviewCount")
    public int reviewCount;

    public static class UpdateRequest {
        @SerializedName("status")
        public String status;
        @SerializedName("difficulty")
        public Integer difficulty;
        @SerializedName("priority")
        public Integer priority;
    }
}
