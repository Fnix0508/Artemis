package com.fnix.artemis.base.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Chessboard {

    public Chessboard(int row, int column, int[][] chessmen) {
        this.row = row;
        this.column = column;
        this.chessmen = chessmen;
    }

    public Chessboard(int row, int column) {
        this.row = row;
        this.column = column;
        this.chessmen = new int[row][column];
    }

    private int row;

    private int column;

    private int[][] chessmen;

}
