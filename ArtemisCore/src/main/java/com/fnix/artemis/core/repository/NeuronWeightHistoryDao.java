package com.fnix.artemis.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.model.NeuronWeightHistory;

public interface NeuronWeightHistoryDao extends JpaRepository<NeuronWeightHistory, Long> {
}
