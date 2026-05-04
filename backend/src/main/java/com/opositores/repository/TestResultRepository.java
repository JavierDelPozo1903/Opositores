package com.opositores.repository;

import com.opositores.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    Optional<TestResult> findByTestId(Long testId);

    List<TestResult> findByUserIdOrderByEndTimeDesc(Long userId);

    @Query("SELECT tr FROM TestResult tr WHERE tr.test.opposition.id = :oppositionId AND tr.user.id = :userId ORDER BY tr.endTime ASC")
    List<TestResult> findByOppositionIdAndUserIdOrderByEndTime(
            @Param("oppositionId") Long oppositionId, @Param("userId") Long userId);

    @Query("SELECT AVG(tr.score) FROM TestResult tr WHERE tr.user.id = :userId")
    Double findAverageScoreByUserId(@Param("userId") Long userId);
}
