package com.opositores.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsOverviewResponse {
    private int totalOppositions;
    private long totalTopics;
    private long studiedTopics;
    private long totalTests;
    private Double averageScore;
}
