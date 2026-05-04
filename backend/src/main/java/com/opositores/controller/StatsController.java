package com.opositores.controller;

import com.opositores.dto.OppositionStatsResponse;
import com.opositores.dto.StatsOverviewResponse;
import com.opositores.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/overview")
    public ResponseEntity<StatsOverviewResponse> getOverview() {
        return ResponseEntity.ok(statsService.getOverview());
    }

    @GetMapping("/opposition/{id}")
    public ResponseEntity<OppositionStatsResponse> getOppositionStats(@PathVariable Long id) {
        return ResponseEntity.ok(statsService.getOppositionStats(id));
    }
}
