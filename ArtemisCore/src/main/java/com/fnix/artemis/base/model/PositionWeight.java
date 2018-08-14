package com.fnix.artemis.base.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionWeight {

    public PositionWeight(int index, double value) {
        this.index = index;
        this.value = value;
    }

    private int index;

    private int x;

    private int y;

    private Double value;
}
