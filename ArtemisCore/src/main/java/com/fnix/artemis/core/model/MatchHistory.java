package com.fnix.artemis.core.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fnix.artemis.core.dto.FinishType;

import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class MatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long blackPlayerId;

    private Long whitePlayerId;

    private int chessboardRow;

    private int chessboardColumn;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String chessmanTurn;

    @Enumerated(EnumType.STRING)
    private FinishType result;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime lastModified;
}
