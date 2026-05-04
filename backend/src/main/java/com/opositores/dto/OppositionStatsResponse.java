package com.opositores.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OppositionStatsResponse {
    private Long oppositionId;
    private String oppositionName;
    private long totalTopics;
    private long reviewedTopics;
    private double progressPercent;
    /** Histórico de notas: lista de {fecha, nota} */
    private List<ScoreEntry> scoreHistory;

    @Data
    @Builder
    public static class ScoreEntry {
        private String date;
        private Double score;
    }
}
