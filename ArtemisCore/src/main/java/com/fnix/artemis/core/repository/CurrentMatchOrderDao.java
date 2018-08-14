package com.fnix.artemis.core.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fnix.artemis.core.model.CurrentMatchOrder;

public interface CurrentMatchOrderDao extends JpaRepository<CurrentMatchOrder, Long> {

    List<CurrentMatchOrder> findByCurrentMatchId(long currentMatchId, Sort sort);

    @Query("from CurrentMatchOrder where currentMatchId = ?1 and roundIndex > ?2")
    List<CurrentMatchOrder> findByRoundIndex(long currentMatchId, int roundIndex);
}
