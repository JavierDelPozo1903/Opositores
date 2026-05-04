package com.opositores.dto;

import com.opositores.model.Test.TestMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TestCreateRequest {

    @NotNull
    private Long oppositionId;

    /** IDs de temas a incluir (tiene prioridad sobre blockIds si se especifica) */
    private List<Long> topicIds;

    /** IDs de bloques a incluir (se incluirán todos sus temas) */
    private List<Long> blockIds;

    @NotNull
    @Min(1)
    private Integer numQuestions;

    private TestMode mode = TestMode.PRACTICE;

    private Integer timeLimitMinutes;
}
