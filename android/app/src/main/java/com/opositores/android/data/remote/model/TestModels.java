package com.opositores.android.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestModels {

    public static class CreateRequest {
        @SerializedName("oppositionId")
        public long oppositionId;
        @SerializedName("topicIds")
        public List<Long> topicIds;
        @SerializedName("blockIds")
        public List<Long> blockIds;
        @SerializedName("numQuestions")
        public int numQuestions;
        @SerializedName("mode")
        public String mode;
        @SerializedName("timeLimitMinutes")
        public Integer timeLimitMinutes;
    }

    public static class QuestionDto {
        @SerializedName("id")
        public long id;
        @SerializedName("questionText")
        public String questionText;
        @SerializedName("options")
        public String options; // JSON string con array de opciones
        @SerializedName("type")
        public String type;
    }

    public static class TestDto {
        @SerializedName("id")
        public long id;
        @SerializedName("mode")
        public String mode;
        @SerializedName("numQuestions")
        public int numQuestions;
        @SerializedName("timeLimitMinutes")
        public Integer timeLimitMinutes;
        @SerializedName("testQuestions")
        public List<TestQuestionDto> testQuestions;
    }

    public static class TestQuestionDto {
        @SerializedName("id")
        public long id;
        @SerializedName("orderIndex")
        public int orderIndex;
        @SerializedName("question")
        public QuestionDto question;
    }

    public static class SubmitRequest {
        @SerializedName("answers")
        public List<AnswerDto> answers;

        public SubmitRequest(List<AnswerDto> answers) {
            this.answers = answers;
        }
    }

    public static class AnswerDto {
        @SerializedName("questionId")
        public long questionId;
        @SerializedName("selectedOption")
        public String selectedOption;

        public AnswerDto(long questionId, String selectedOption) {
            this.questionId = questionId;
            this.selectedOption = selectedOption;
        }
    }

    public static class ResultDto {
        @SerializedName("id")
        public long id;
        @SerializedName("testId")
        public long testId;
        @SerializedName("score")
        public double score;
        @SerializedName("correctCount")
        public int correctCount;
        @SerializedName("incorrectCount")
        public int incorrectCount;
        @SerializedName("blankCount")
        public int blankCount;
    }
}
