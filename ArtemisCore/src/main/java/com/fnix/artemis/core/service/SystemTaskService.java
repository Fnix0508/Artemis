package com.fnix.artemis.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fnix.artemis.core.dto.SystemTaskType;
import com.fnix.artemis.core.model.SystemTask;
import com.fnix.artemis.core.repository.SystemTaskDao;

@Service
public class SystemTaskService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemTaskService.class);

    @Autowired
    private SystemTaskDao systemTaskDao;

    public void submit(SystemTask systemTask) {
        SystemTask task = systemTaskDao.findAndLockByTypeAndTargetId(systemTask.getType(), systemTask.getTargetId());
        if (task == null) {
            task = systemTask;
        } else {
            task.setRunAt(systemTask.getRunAt());
        }
        systemTaskDao.save(task);
    }

    /**
     * 后台任务调用，其他地方请勿调用
     * @param systemTask
     */
    @Transactional
    public void updateResult(SystemTask systemTask) {
        SystemTask task = systemTaskDao.findAndLockById(systemTask.getId());
        if (task == null) {
            LOG.warn("系统任务已被删除，本次结果不予保存，result={}", systemTask);
            return;
        }
        if (!task.getVersion().equals(systemTask.getVersion())) {
            LOG.warn("系统任务已有更改，本次结果不予保存，result={}", systemTask);
            return;
        }
        systemTaskDao.save(systemTask);
    }

}
