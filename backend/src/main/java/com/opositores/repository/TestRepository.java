package com.opositores.repository;

import com.opositores.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Test> findByUserIdAndOppositionIdOrderByCreatedAtDesc(Long userId, Long oppositionId);

    Optional<Test> findByIdAndUserId(Long id, Long userId);
}
