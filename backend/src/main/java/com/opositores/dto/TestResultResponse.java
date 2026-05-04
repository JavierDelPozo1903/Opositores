package com.opositores.dto;

import com.opositores.model.TestResult;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestResultResponse {
    private Long id;
    private Long testId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double score;
    private Integer correctCount;
    private Integer incorrectCount;
    private Integer blankCount;

    public static TestResultResponse from(TestResult r) {
        TestResultResponse dto = new TestResultResponse();
        dto.setId(r.getId());
        dto.setTestId(r.getTest().getId());
        dto.setStartTime(r.getStartTime());
        dto.setEndTime(r.getEndTime());
        dto.setScore(r.getScore());
        dto.setCorrectCount(r.getCorrectCount());
        dto.setIncorrectCount(r.getIncorrectCount());
        dto.setBlankCount(r.getBlankCount());
        return dto;
    }
}
