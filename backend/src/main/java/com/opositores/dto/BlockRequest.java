package com.opositores.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlockRequest {

    @NotBlank
    private String name;

    private Double weight = 1.0;
}
