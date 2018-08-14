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
public class CurrentMatchOrder {

    public CurrentMatchOrder(Long currentMatchId, int roundIndex, Integer positionX, Integer positionY) {
        this.currentMatchId = currentMatchId;
        this.roundIndex = roundIndex;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long currentMatchId;

    private int roundIndex;

    private Integer positionX;

    private Integer positionY;

    @CreatedDate
    private LocalDateTime created;
}
