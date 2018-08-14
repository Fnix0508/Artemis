package com.fnix.artemis.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.model.User;

public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
