package com.opositores.repository;

import com.opositores.model.Opposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OppositionRepository extends JpaRepository<Opposition, Long> {

    List<Opposition> findByUserId(Long userId);

    Optional<Opposition> findByIdAndUserId(Long id, Long userId);
}
