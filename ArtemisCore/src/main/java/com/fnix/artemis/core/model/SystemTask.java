package com.fnix.artemis.core.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fnix.artemis.core.dto.SystemTaskType;
import com.fnix.artemis.core.dto.TaskStatus;

import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class SystemTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SystemTaskType type;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime runAt;

    private boolean cycling;

    private int cycle;

    private boolean retry;

    private int retryInterval;

    private int errorTimes;

    private String message;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime lastModified;

    @Version
    private Long version;
}
