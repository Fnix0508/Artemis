package com.fnix.artemis.core.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fnix.artemis.core.model.MatchHistory;

public interface MatchHistoryDao extends JpaRepository<MatchHistory, Long> {

    @Query("from MatchHistory where lastModified > ?1 or (lastModified = ?1 and id > ?3) and lastModified < ?2 order by lastModified, id ")
    List<MatchHistory> incrementGet(LocalDateTime start, LocalDateTime end, Long lastId, Pageable page);

    @Query("from MatchHistory where id > ?1 order by id")
    List<MatchHistory> incrementGet(long lastId, Pageable pageable);

    default MatchHistory incrementGetOne(long lastId) {
        List<MatchHistory> list = incrementGet(lastId, PageRequest.of(0, 1));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
