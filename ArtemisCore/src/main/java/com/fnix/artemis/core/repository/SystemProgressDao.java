package com.fnix.artemis.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.dto.ProgressType;
import com.fnix.artemis.core.model.SystemProgress;

public interface SystemProgressDao extends JpaRepository<SystemProgress, Long> {

    Optional<SystemProgress> findByType(ProgressType type);
}
