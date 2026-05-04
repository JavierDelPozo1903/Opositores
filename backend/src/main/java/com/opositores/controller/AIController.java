package com.opositores.controller;

import com.opositores.dto.AIRequest;
import com.opositores.model.Flashcard;
import com.opositores.model.Question;
import com.opositores.service.AIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/summary")
    public ResponseEntity<Map<String, String>> generateSummary(@Valid @RequestBody AIRequest req) {
        String summary = aiService.generateSummary(req.getDocumentId(), req.getLength());
        return ResponseEntity.ok(Map.of("summary", summary));
    }

    @PostMapping("/flashcards")
    public ResponseEntity<List<Flashcard>> generateFlashcards(@Valid @RequestBody AIRequest req) {
        int num = req.getNumCards() != null ? req.getNumCards() : 10;
        List<Flashcard> flashcards = aiService.generateFlashcards(req.getDocumentId(), num);
        return ResponseEntity.ok(flashcards);
    }

    @PostMapping("/questions/mcq")
    public ResponseEntity<List<Question>> generateMcq(@Valid @RequestBody AIRequest req) {
        int num = req.getNumQuestions() != null ? req.getNumQuestions() : 10;
        int diff = req.getDifficulty() != null ? req.getDifficulty() : 3;
        List<Question> questions = aiService.generateMcqQuestions(req.getDocumentId(), num, diff);
        return ResponseEntity.ok(questions);
    }
}
