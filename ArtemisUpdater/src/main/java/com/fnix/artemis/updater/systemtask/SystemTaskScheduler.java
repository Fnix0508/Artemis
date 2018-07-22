package com.fnix.artemis.updater.systemtask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fnix.artemis.core.dto.SystemTaskType;
import com.fnix.artemis.core.dto.TaskStatus;
import com.fnix.artemis.core.model.SystemTask;
import com.fnix.artemis.core.repository.SystemTaskDao;
import com.fnix.artemis.core.service.SystemTaskService;
import com.fnix.artemis.core.utils.DateUtils;
import com.fnix.artemis.updater.UpdaterProperties;
import com.google.common.collect.Maps;

@Component
public class SystemTaskScheduler implements InitializingBean{

    private static final Logger LOG = LoggerFactory.getLogger(SystemTaskScheduler.class);

    private static Map<SystemTaskType, SystemTaskProcessor> processorMap = Maps.newConcurrentMap();

    @Autowired
    private SystemTaskDao systemTaskDao;

    @Autowired
    private List<SystemTaskProcessor> processors;

    @Autowired
    private ExecutorService systemExecutor;

    @Autowired
    private UpdaterProperties updaterProperties;

    @Autowired
    private SystemTaskService systemTaskService;

    private static Map<SystemTaskType, Future<?>> RUNNING = Maps.newConcurrentMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        processors.forEach(p -> processorMap.put(p.getType(), p));
    }

    @Scheduled(fixedDelay = 1000L)
    public void addToRunning() {
        List<SystemTask> tasks = systemTaskDao.findTop10ByStatusAndRunAtBeforeOrderByRunAt(TaskStatus.CREATED, DateUtils.now());
        RUNNING.forEach((k, v) -> {
            if (v.isDone()) {
                RUNNING.remove(k);
            }
        });
        if (RUNNING.size() > updaterProperties.getSystemThreadNum() * 2) {
            LOG.warn("系统任务已爆满！size={}", RUNNING.size());
            return;
        }
        tasks.forEach(t -> {
            if (!RUNNING.containsKey(t.getType())) {
                RUNNING.put(t.getType(), systemExecutor.submit(() -> process(t)));
            } else {
                LOG.info("已有正在进行的系统任务！type={}", t.getType());
            }
        });

    }

    private void process(SystemTask task) {
        SystemTaskProcessor processor = processorMap.get(task.getType());
        try {
            task.setStartTime(DateUtils.now());
            processor.process(task);
            task.setEndTime(DateUtils.now());
            if (task.isCycling()) {
                task.setRunAt(DateUtils.now().plusSeconds(task.getCycle()));
            } else {
                task.setStatus(TaskStatus.FINISH);
            }
            task.setErrorTimes(0);
        } catch (Exception e) {
            LOG.error("处理系统任务失败 task={}", task, e);
            task.setMessage(e.getMessage());
            task.setErrorTimes(task.getErrorTimes() + 1);
            if (task.getErrorTimes() >= 10) {
                task.setStatus(TaskStatus.ERROR);
            } else if (task.isRetry()) {
                task.setRunAt(DateUtils.now().plusSeconds(task.getRetryInterval()));
            } else if (task.isCycling()) {
                task.setRunAt(DateUtils.now().plusSeconds(task.getCycle()));
            } else {
                task.setStatus(TaskStatus.ERROR);
            }
        } finally {
            systemTaskService.updateResult(task);
        }
    }

}
