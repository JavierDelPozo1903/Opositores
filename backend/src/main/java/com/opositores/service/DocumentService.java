package com.opositores.service;

import com.opositores.dto.DocumentResponse;
import com.opositores.model.Document;
import com.opositores.model.Topic;
import com.opositores.model.User;
import com.opositores.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TopicService topicService;
    private final UserService userService;
    private final OppositionService oppositionService;

    @Value("${storage.local-path:/tmp/opositores/documents}")
    private String storagePath;

    public DocumentResponse upload(Long topicId, MultipartFile file, Document.DocumentType type) throws IOException {
        Topic topic = topicService.findById(topicId);
        oppositionService.findOwned(topic.getBlock().getOpposition().getId()); // verifica propiedad
        User user = userService.getCurrentUser();

        String fileUrl = storeFile(file);

        Document doc = Document.builder()
                .topic(topic)
                .user(user)
                .name(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .type(type)
                .build();

        return DocumentResponse.from(documentRepository.save(doc));
    }

    public List<DocumentResponse> getByTopic(Long topicId) {
        Topic topic = topicService.findById(topicId);
        oppositionService.findOwned(topic.getBlock().getOpposition().getId());
        return documentRepository.findByTopicId(topicId)
                .stream().map(DocumentResponse::from).collect(Collectors.toList());
    }

    public Document findById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado: " + documentId));
    }

    /**
     * Stub: extrae el contenido textual del documento.
     * En producción aquí iría la lógica de extracción de PDF, OCR, etc.
     */
    public String getDocumentText(Document document) {
        // TODO: implementar extracción real con Apache PDFBox / Tika
        return "[Contenido del documento: " + document.getName() + "]";
    }

    private String storeFile(MultipartFile file) throws IOException {
        Path dir = Paths.get(storagePath);
        Files.createDirectories(dir);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dest = dir.resolve(filename);
        file.transferTo(dest);
        return storagePath + "/" + filename;
    }
}
