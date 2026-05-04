package com.opositores.repository;

import com.opositores.model.AIRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRequestRepository extends JpaRepository<AIRequest, Long> {

    List<AIRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
}
