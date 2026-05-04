package com.opositores.dto;

import com.opositores.model.Opposition.OppositionScope;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OppositionRequest {

    @NotBlank
    private String name;

    private OppositionScope scope;

    private LocalDate targetExamDate;

    private Double hoursPerWeek;
}
