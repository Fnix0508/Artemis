package com.fnix.artemis.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fnix.artemis.core.model.NeuronWeight;
import com.fnix.artemis.core.repository.NeuronWeightDao;
import com.fnix.artemis.core.utils.Stream2;
import com.google.common.collect.Lists;

@Service
public class NeuronWeightService {

    @Autowired
    private NeuronWeightDao neuronWeightDao;

    @Transactional
    public void save(List<NeuronWeight> weightList) {
        for (List<NeuronWeight> list: Lists.partition(weightList, 225)) {
            neuronWeightDao.saveAll(list);
        }
    }
}
