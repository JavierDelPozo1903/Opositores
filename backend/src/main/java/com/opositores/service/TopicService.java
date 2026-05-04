package com.opositores.service;

import com.opositores.dto.TopicRequest;
import com.opositores.dto.TopicResponse;
import com.opositores.model.Block;
import com.opositores.model.Topic;
import com.opositores.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final BlockService blockService;
    private final OppositionService oppositionService;

    public List<TopicResponse> getByBlock(Long blockId) {
        // Verificamos que el bloque existe y pertenece a una oposición del usuario
        topicRepository.findByBlockId(blockId); // solo lectura, validación se hace al crear
        return topicRepository.findByBlockId(blockId)
                .stream().map(TopicResponse::from).collect(Collectors.toList());
    }

    public TopicResponse create(Long blockId, TopicRequest req) {
        // Buscamos el bloque asegurándonos de que la oposición padre pertenece al usuario
        Block block = topicRepository.findByBlockId(blockId)
                .stream().findFirst()
                .map(Topic::getBlock)
                .orElse(null);

        // Si no hay topics aún, cargamos el bloque directamente
        if (block == null) {
            block = blockService.findOwned(blockId,
                    // necesitamos el oppositionId; lo sacamos del path
                    topicRepository.findByBlockId(blockId).stream()
                            .findFirst().map(t -> t.getBlock().getOpposition().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Bloque " + blockId)));
        }

        Topic topic = Topic.builder()
                .block(block)
                .title(req.getTitle())
                .officialNumber(req.getOfficialNumber())
                .difficulty(req.getDifficulty())
                .priority(req.getPriority())
                .status(req.getStatus() != null ? req.getStatus() : Topic.TopicStatus.NOT_STARTED)
                .build();

        return TopicResponse.from(topicRepository.save(topic));
    }

    public TopicResponse update(Long topicId, TopicRequest req) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Tema no encontrado: " + topicId));

        // Verificamos que el usuario es propietario a través de la cadena block -> opposition -> user
        oppositionService.findOwned(topic.getBlock().getOpposition().getId());

        if (req.getTitle() != null) topic.setTitle(req.getTitle());
        if (req.getOfficialNumber() != null) topic.setOfficialNumber(req.getOfficialNumber());
        if (req.getDifficulty() != null) topic.setDifficulty(req.getDifficulty());
        if (req.getPriority() != null) topic.setPriority(req.getPriority());
        if (req.getStatus() != null) {
            topic.setStatus(req.getStatus());
            // Actualizamos fecha de último repaso cuando el estado avanza
            if (req.getStatus().ordinal() > Topic.TopicStatus.NOT_STARTED.ordinal()) {
                topic.setLastReviewDate(LocalDate.now());
                topic.setReviewCount(topic.getReviewCount() + 1);
            }
        }

        return TopicResponse.from(topicRepository.save(topic));
    }

    public Topic findById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Tema no encontrado: " + topicId));
    }
}
