package com.opositores.service;

import com.opositores.dto.BlockRequest;
import com.opositores.dto.BlockResponse;
import com.opositores.model.Block;
import com.opositores.model.Opposition;
import com.opositores.repository.BlockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final OppositionService oppositionService;

    public List<BlockResponse> getByOpposition(Long oppositionId) {
        oppositionService.findOwned(oppositionId); // verifica propiedad
        return blockRepository.findByOppositionId(oppositionId)
                .stream().map(BlockResponse::from).collect(Collectors.toList());
    }

    public BlockResponse create(Long oppositionId, BlockRequest req) {
        Opposition opposition = oppositionService.findOwned(oppositionId);
        Block block = Block.builder()
                .opposition(opposition)
                .name(req.getName())
                .weight(req.getWeight() != null ? req.getWeight() : 1.0)
                .build();
        return BlockResponse.from(blockRepository.save(block));
    }

    public Block findOwned(Long blockId, Long oppositionId) {
        return blockRepository.findByIdAndOppositionId(blockId, oppositionId)
                .orElseThrow(() -> new EntityNotFoundException("Bloque no encontrado: " + blockId));
    }
}
