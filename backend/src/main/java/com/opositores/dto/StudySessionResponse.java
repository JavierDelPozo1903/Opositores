package com.opositores.dto;

import com.opositores.model.StudySessionPlan;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudySessionResponse {
    private Long id;
    private LocalDate date;
    private Integer plannedMinutes;
    private String status;
    private Long topicId;
    private String topicTitle;

    public static StudySessionResponse from(StudySessionPlan s) {
        StudySessionResponse dto = new StudySessionResponse();
        dto.setId(s.getId());
        dto.setDate(s.getDate());
        dto.setPlannedMinutes(s.getPlannedMinutes());
        dto.setStatus(s.getStatus().name());
        if (s.getTopic() != null) {
            dto.setTopicId(s.getTopic().getId());
            dto.setTopicTitle(s.getTopic().getTitle());
        }
        return dto;
    }
}
