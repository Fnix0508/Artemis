package com.fnix.artemis.core.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.fnix.artemis.core.dto.SystemTaskType;
import com.fnix.artemis.core.dto.TaskStatus;
import com.fnix.artemis.core.model.SystemTask;

public interface SystemTaskDao extends JpaRepository<SystemTask, Long> {

    List<SystemTask> findTop10ByStatusAndRunAtBeforeOrderByRunAt(TaskStatus status, LocalDateTime runAt);

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    SystemTask findAndLockById(long id);

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    SystemTask findAndLockByTypeAndTargetId(SystemTaskType type, long id);
}
