package com.fnix.artemis.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.model.UserCurrentMatch;

public interface UserCurrentMatchDao extends JpaRepository<UserCurrentMatch, Long> {

    UserCurrentMatch findByUserId(long userId);
}
