package com.opositores.dto;

import com.opositores.model.Topic.TopicStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TopicRequest {

    @NotBlank
    private String title;

    private String officialNumber;

    @Min(1) @Max(5)
    private Integer difficulty = 3;

    @Min(1) @Max(5)
    private Integer priority = 3;

    private TopicStatus status;
}
