package com.opositores.controller;

import com.opositores.dto.TopicRequest;
import com.opositores.dto.TopicResponse;
import com.opositores.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/blocks/{blockId}/topics")
    public ResponseEntity<List<TopicResponse>> getTopics(@PathVariable Long blockId) {
        return ResponseEntity.ok(topicService.getByBlock(blockId));
    }

    @PostMapping("/blocks/{blockId}/topics")
    public ResponseEntity<TopicResponse> createTopic(@PathVariable Long blockId,
                                                      @Valid @RequestBody TopicRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.create(blockId, req));
    }

    @PutMapping("/topics/{topicId}")
    public ResponseEntity<TopicResponse> updateTopic(@PathVariable Long topicId,
                                                      @RequestBody TopicRequest req) {
        return ResponseEntity.ok(topicService.update(topicId, req));
    }
}
