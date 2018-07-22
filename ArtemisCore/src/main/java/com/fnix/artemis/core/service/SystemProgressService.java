package com.fnix.artemis.core.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fnix.artemis.core.dto.ProgressType;
import com.fnix.artemis.core.model.SystemProgress;
import com.fnix.artemis.core.repository.SystemProgressDao;

@Service
public class SystemProgressService {

    @Autowired
    private SystemProgressDao systemProgressDao;

    public SystemProgress getOrDefault(ProgressType type, LocalDateTime lastModified) {
        return systemProgressDao.findByType(type).orElse(new SystemProgress(type, lastModified, 0L));
    }

    public SystemProgress save(SystemProgress progress) {
        return systemProgressDao.save(progress);
    }
}
