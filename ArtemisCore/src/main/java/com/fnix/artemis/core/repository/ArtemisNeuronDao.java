package com.fnix.artemis.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.model.ArtemisNeuron;

public interface ArtemisNeuronDao extends JpaRepository<ArtemisNeuron, Long> {

    List<ArtemisNeuron> findByLayerId(long layerId);
}
