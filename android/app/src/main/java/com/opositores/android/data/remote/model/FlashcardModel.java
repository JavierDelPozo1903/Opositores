package com.opositores.android.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class FlashcardModel {
    @SerializedName("id")
    public long id;
    @SerializedName("question")
    public String question;
    @SerializedName("answer")
    public String answer;
    @SerializedName("nextReviewDate")
    public String nextReviewDate;
    @SerializedName("intervalDays")
    public int intervalDays;
}
