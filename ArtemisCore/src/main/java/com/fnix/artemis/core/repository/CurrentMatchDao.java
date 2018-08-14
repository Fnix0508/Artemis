package com.fnix.artemis.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.model.CurrentMatch;

public interface CurrentMatchDao extends JpaRepository<CurrentMatch, Long> {

}
