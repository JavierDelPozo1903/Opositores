package com.opositores.repository;

import com.opositores.model.StudySessionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudySessionPlanRepository extends JpaRepository<StudySessionPlan, Long> {

    List<StudySessionPlan> findByOppositionId(Long oppositionId);

    List<StudySessionPlan> findByOppositionIdAndDateBetweenOrderByDateAsc(
            Long oppositionId, LocalDate from, LocalDate to);

    void deleteByOppositionId(Long oppositionId);
}
