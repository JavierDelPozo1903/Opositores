package com.opositores.android.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanModels {

    public static class StudySessionDto {
        @SerializedName("id")
        public long id;
        @SerializedName("date")
        public String date;
        @SerializedName("plannedMinutes")
        public Integer plannedMinutes;
        @SerializedName("status")
        public String status;
        @SerializedName("topicId")
        public Long topicId;
        @SerializedName("topicTitle")
        public String topicTitle;
    }

    public static class GeneratePlanRequest {
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("hoursPerWeek")
        public double hoursPerWeek;

        public GeneratePlanRequest(String startDate, String endDate, double hoursPerWeek) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.hoursPerWeek = hoursPerWeek;
        }
    }
}
