package com.opositores.controller;

import com.opositores.dto.PlanRequest;
import com.opositores.dto.StudySessionResponse;
import com.opositores.model.StudySessionPlan;
import com.opositores.service.PlanningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PlanningController {

    private final PlanningService planningService;

    @PostMapping("/oppositions/{id}/plan")
    public ResponseEntity<List<StudySessionResponse>> generatePlan(
            @PathVariable Long id,
            @Valid @RequestBody PlanRequest req) {
        return ResponseEntity.ok(planningService.generatePlan(id, req));
    }

    @GetMapping("/oppositions/{id}/plan")
    public ResponseEntity<List<StudySessionResponse>> getPlan(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(planningService.getPlan(id, from, to));
    }

    @PatchMapping("/study-sessions/{sessionId}")
    public ResponseEntity<StudySessionResponse> updateSessionStatus(
            @PathVariable Long sessionId,
            @RequestBody Map<String, String> body) {
        StudySessionPlan.SessionStatus status =
                StudySessionPlan.SessionStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(planningService.updateStatus(sessionId, status));
    }
}
