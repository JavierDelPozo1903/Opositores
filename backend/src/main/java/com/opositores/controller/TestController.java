package com.opositores.controller;

import com.opositores.dto.TestCreateRequest;
import com.opositores.dto.TestResultResponse;
import com.opositores.dto.TestSubmitRequest;
import com.opositores.model.Test;
import com.opositores.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping
    public ResponseEntity<Test> createTest(@Valid @RequestBody TestCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTest(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTest(@PathVariable Long id) {
        return ResponseEntity.ok(testService.getTest(id));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<TestResultResponse> submitTest(@PathVariable Long id,
                                                          @RequestBody TestSubmitRequest req) {
        return ResponseEntity.ok(testService.submitTest(id, req));
    }
}
