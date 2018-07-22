package com.fnix.artemis.core.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class NeuronWeightHistory {

    public NeuronWeightHistory(LocalDateTime snapshotTime, String snapshot) {
        this.snapshotTime = snapshotTime;
        this.snapshot = snapshot;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime snapshotTime;

    private String snapshot;

    @CreatedDate
    private LocalDateTime created;
}
