package com.opositores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "study_session_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opposition_id", nullable = false)
    private Opposition opposition;

    @Column(nullable = false)
    private LocalDate date;

    private Integer plannedMinutes;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SessionStatus status = SessionStatus.PENDING;

    /** Tema sugerido para esta sesión (puede ser null si es sesión libre) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public enum SessionStatus {
        PENDING, COMPLETED, SKIPPED
    }
}
