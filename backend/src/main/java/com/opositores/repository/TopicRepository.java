package com.opositores.repository;

import com.opositores.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByBlockId(Long blockId);

    List<Topic> findByBlockIdIn(List<Long> blockIds);

    @Query("SELECT t FROM Topic t WHERE t.block.opposition.id = :oppositionId ORDER BY t.priority DESC, t.difficulty DESC")
    List<Topic> findByOppositionIdOrderByPriorityDesc(@Param("oppositionId") Long oppositionId);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.block.opposition.id = :oppositionId AND t.status IN ('REVIEWED', 'MASTERED')")
    long countReviewedOrMasteredByOppositionId(@Param("oppositionId") Long oppositionId);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.block.opposition.id = :oppositionId")
    long countByOppositionId(@Param("oppositionId") Long oppositionId);
}
