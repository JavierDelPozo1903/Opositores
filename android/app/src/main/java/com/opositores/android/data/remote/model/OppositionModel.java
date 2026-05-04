package com.opositores.android.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class OppositionModel {

    @SerializedName("id")
    public long id;
    @SerializedName("name")
    public String name;
    @SerializedName("scope")
    public String scope;
    @SerializedName("targetExamDate")
    public String targetExamDate;
    @SerializedName("hoursPerWeek")
    public Double hoursPerWeek;
    @SerializedName("createdAt")
    public String createdAt;

    public static class CreateRequest {
        @SerializedName("name")
        public String name;
        @SerializedName("scope")
        public String scope;
        @SerializedName("targetExamDate")
        public String targetExamDate;
        @SerializedName("hoursPerWeek")
        public Double hoursPerWeek;

        public CreateRequest(String name, String scope, String targetExamDate, Double hoursPerWeek) {
            this.name = name;
            this.scope = scope;
            this.targetExamDate = targetExamDate;
            this.hoursPerWeek = hoursPerWeek;
        }
    }
}
