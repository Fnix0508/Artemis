package com.fnix.artemis.core.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fnix.artemis.core.model.ArtemisLayer;

public interface ArtemisLayerDao extends JpaRepository<ArtemisLayer, Long> {

    List<ArtemisLayer> findByNetworkCode(String networkCode, Sort sort);
}
