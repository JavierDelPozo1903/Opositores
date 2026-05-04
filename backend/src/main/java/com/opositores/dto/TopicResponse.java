package com.opositores.dto;

import com.opositores.model.Topic;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TopicResponse {
    private Long id;
    private Long blockId;
    private String officialNumber;
    private String title;
    private Integer difficulty;
    private Integer priority;
    private String status;
    private Integer reviewCount;
    private LocalDate lastReviewDate;

    public static TopicResponse from(Topic t) {
        TopicResponse dto = new TopicResponse();
        dto.setId(t.getId());
        dto.setBlockId(t.getBlock().getId());
        dto.setOfficialNumber(t.getOfficialNumber());
        dto.setTitle(t.getTitle());
        dto.setDifficulty(t.getDifficulty());
        dto.setPriority(t.getPriority());
        dto.setStatus(t.getStatus().name());
        dto.setReviewCount(t.getReviewCount());
        dto.setLastReviewDate(t.getLastReviewDate());
        return dto;
    }
}
