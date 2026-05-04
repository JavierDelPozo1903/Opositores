package com.opositores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "study_session_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opposition_id", nullable = false)
    private Opposition opposition;

    @Column(nullable = false)
    private LocalDate date;

    private Integer minutes;

    @Column(length = 1000)
    private String notes;
}
