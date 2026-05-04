package com.opositores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "topics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;

    /** Número oficial del tema (p.ej. "Tema 1", "T-15") */
    private String officialNumber;

    @Column(nullable = false)
    private String title;

    /** Dificultad percibida: 1 (fácil) - 5 (muy difícil) */
    @Builder.Default
    private Integer difficulty = 3;

    /** Prioridad en el estudio: 1 (baja) - 5 (alta) */
    @Builder.Default
    private Integer priority = 3;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TopicStatus status = TopicStatus.NOT_STARTED;

    @Builder.Default
    private Integer reviewCount = 0;

    private LocalDate lastReviewDate;

    public enum TopicStatus {
        NOT_STARTED, STUDYING, REVIEWED, MASTERED
    }
}
