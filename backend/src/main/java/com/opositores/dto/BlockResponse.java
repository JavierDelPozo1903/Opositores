package com.opositores.dto;

import com.opositores.model.Block;
import lombok.Data;

@Data
public class BlockResponse {
    private Long id;
    private Long oppositionId;
    private String name;
    private Double weight;

    public static BlockResponse from(Block b) {
        BlockResponse dto = new BlockResponse();
        dto.setId(b.getId());
        dto.setOppositionId(b.getOpposition().getId());
        dto.setName(b.getName());
        dto.setWeight(b.getWeight());
        return dto;
    }
}
