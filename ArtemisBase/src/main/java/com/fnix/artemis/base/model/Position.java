package com.fnix.artemis.base.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Position {

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int x;

    private int y;
}
