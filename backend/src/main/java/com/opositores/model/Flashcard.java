package com.opositores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "flashcards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false, length = 2000)
    private String question;

    @Column(nullable = false, length = 4000)
    private String answer;

    private LocalDate nextReviewDate;

    /** Días hasta la próxima revisión (algoritmo SM-2 simplificado) */
    @Builder.Default
    private Integer intervalDays = 1;

    /** Factor de facilidad para SM-2, valor inicial 2.5 */
    @Builder.Default
    private Double easeFactor = 2.5;
}
