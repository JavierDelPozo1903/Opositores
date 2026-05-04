package com.opositores.dto;

import com.opositores.model.Document;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    private Long id;
    private Long topicId;
    private String name;
    private String fileUrl;
    private String type;
    private String language;
    private LocalDateTime createdAt;

    public static DocumentResponse from(Document d) {
        DocumentResponse dto = new DocumentResponse();
        dto.setId(d.getId());
        dto.setTopicId(d.getTopic().getId());
        dto.setName(d.getName());
        dto.setFileUrl(d.getFileUrl());
        dto.setType(d.getType().name());
        dto.setLanguage(d.getLanguage());
        dto.setCreatedAt(d.getCreatedAt());
        return dto;
    }
}
