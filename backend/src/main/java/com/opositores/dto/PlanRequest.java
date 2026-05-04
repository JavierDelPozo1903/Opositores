package com.opositores.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanRequest {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate targetExamDate;

    @NotNull
    @Min(1)
    private Double hoursPerWeek;
}
