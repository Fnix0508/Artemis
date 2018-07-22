package com.fnix.artemis.updater.systemtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fnix.artemis.base.brain.ArtemisBrain;
import com.fnix.artemis.base.model.Chessboard;
import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.core.dto.SystemTaskType;

@Component
public class SelfPlayBrainTaskProcessor extends AbstractSelfPlayTaskProcessor {

    @Autowired
    private ArtemisBrain artemisBrain;

    @Override
    public SystemTaskType getType() {
        return SystemTaskType.SELF_PLAY_BRAIN;
    }

    @Override
    public Position judgeNextPosition(Chessboard chessboard) {
        return artemisBrain.judgeNextPosition(chessboard);
    }

}
