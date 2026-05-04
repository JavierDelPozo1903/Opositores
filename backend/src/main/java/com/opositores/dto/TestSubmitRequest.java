package com.opositores.dto;

import lombok.Data;

import java.util.List;

@Data
public class TestSubmitRequest {
    private List<AnswerDto> answers;

    @Data
    public static class AnswerDto {
        private Long questionId;
        /** Null si la pregunta se deja en blanco */
        private String selectedOption;
    }
}
