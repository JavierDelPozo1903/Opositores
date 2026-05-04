package com.opositores.repository;

import com.opositores.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    List<Block> findByOppositionId(Long oppositionId);

    Optional<Block> findByIdAndOppositionId(Long id, Long oppositionId);
}
