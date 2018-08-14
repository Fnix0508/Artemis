package com.fnix.artemis.core.dto;

import java.util.List;

import com.fnix.artemis.core.model.CurrentMatch;
import com.fnix.artemis.core.model.CurrentMatchOrder;
import com.fnix.artemis.core.model.UserCurrentMatch;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrentMatchDto {

    public CurrentMatchDto(UserCurrentMatch userCurrentMatch, CurrentMatch currentMatch) {
        this.userCurrentMatch = userCurrentMatch;
        this.currentMatch = currentMatch;
    }


    public CurrentMatchDto(boolean finished, FinishType finishType) {
        this.finished = finished;
        this.finishType = finishType;
    }

    private UserCurrentMatch userCurrentMatch;

    private CurrentMatch currentMatch;

    private List<CurrentMatchOrder> currentMatchOrders;

    private boolean finished;

    private FinishType finishType;
}
