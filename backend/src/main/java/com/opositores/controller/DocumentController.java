package com.opositores.controller;

import com.opositores.dto.DocumentResponse;
import com.opositores.model.Document;
import com.opositores.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/topics/{topicId}/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getDocuments(@PathVariable Long topicId) {
        return ResponseEntity.ok(documentService.getByTopic(topicId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> uploadDocument(
            @PathVariable Long topicId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "PDF") Document.DocumentType type) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.upload(topicId, file, type));
    }
}
