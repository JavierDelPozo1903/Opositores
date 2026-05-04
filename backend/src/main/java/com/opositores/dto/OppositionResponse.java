package com.opositores.dto;

import com.opositores.model.Opposition;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OppositionResponse {
    private Long id;
    private String name;
    private String scope;
    private LocalDate targetExamDate;
    private Double hoursPerWeek;
    private LocalDateTime createdAt;

    public static OppositionResponse from(Opposition o) {
        OppositionResponse dto = new OppositionResponse();
        dto.setId(o.getId());
        dto.setName(o.getName());
        dto.setScope(o.getScope() != null ? o.getScope().name() : null);
        dto.setTargetExamDate(o.getTargetExamDate());
        dto.setHoursPerWeek(o.getHoursPerWeek());
        dto.setCreatedAt(o.getCreatedAt());
        return dto;
    }
}
