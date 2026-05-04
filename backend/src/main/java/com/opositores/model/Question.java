package com.opositores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private QuestionSource source = QuestionSource.AI;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private QuestionType type = QuestionType.MCQ;

    @Column(nullable = false, length = 2000)
    private String questionText;

    @Column(nullable = false, length = 1000)
    private String correctAnswer;

    /**
     * Opciones en formato JSON: ["opción A","opción B","opción C","opción D"]
     * Solo aplica a MCQ y TRUE_FALSE.
     */
    @Column(length = 2000)
    private String options;

    public enum QuestionSource {
        USER, AI
    }

    public enum QuestionType {
        MCQ, OPEN, TRUE_FALSE
    }
}
