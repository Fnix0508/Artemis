package com.fnix.artemis.base.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LearningData {

    public LearningData(Chessboard chessboard, Position next) {
        this.chessboard = chessboard;
        this.next = next;
    }

    private Chessboard chessboard;

    private Position next;
}
