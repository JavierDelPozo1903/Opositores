package com.opositores.controller;

import com.opositores.dto.BlockRequest;
import com.opositores.dto.BlockResponse;
import com.opositores.service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oppositions/{oppositionId}/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @GetMapping
    public ResponseEntity<List<BlockResponse>> getBlocks(@PathVariable Long oppositionId) {
        return ResponseEntity.ok(blockService.getByOpposition(oppositionId));
    }

    @PostMapping
    public ResponseEntity<BlockResponse> createBlock(@PathVariable Long oppositionId,
                                                      @Valid @RequestBody BlockRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blockService.create(oppositionId, req));
    }
}
