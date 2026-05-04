package com.opositores.controller;

import com.opositores.dto.OppositionRequest;
import com.opositores.dto.OppositionResponse;
import com.opositores.service.OppositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oppositions")
@RequiredArgsConstructor
public class OppositionController {

    private final OppositionService oppositionService;

    @GetMapping
    public ResponseEntity<List<OppositionResponse>> getAll() {
        return ResponseEntity.ok(oppositionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OppositionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(oppositionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OppositionResponse> create(@Valid @RequestBody OppositionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oppositionService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OppositionResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody OppositionRequest req) {
        return ResponseEntity.ok(oppositionService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        oppositionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
