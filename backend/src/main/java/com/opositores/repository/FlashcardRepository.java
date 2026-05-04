package com.opositores.repository;

import com.opositores.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByTopicId(Long topicId);

    /** Flashcards pendientes de repasar hoy o antes */
    List<Flashcard> findByTopicIdAndNextReviewDateLessThanEqual(Long topicId, LocalDate date);
}
