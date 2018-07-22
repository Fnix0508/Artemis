package com.fnix.artemis.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.model.NeuronWeight;

public interface NeuronWeightDao extends JpaRepository<NeuronWeight, Long> {

    List<NeuronWeight> findByToId(long fromId);

    List<NeuronWeight> findByFromId(long toId);

    NeuronWeight findByToIdAndFromId(long toId, long fromId);
}
