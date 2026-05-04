package com.opositores.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AIRequest {

    @NotNull
    private Long documentId;

    /** Para summary: short | medium | long */
    private String length;

    /** Para flashcards y MCQ */
    @Min(1)
    private Integer numCards;

    @Min(1)
    private Integer numQuestions;

    /** Dificultad de las preguntas MCQ: 1–5 */
    private Integer difficulty;
}
