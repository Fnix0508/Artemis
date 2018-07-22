package com.fnix.artemis.updater.systemtask;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.fnix.artemis.base.model.Chessboard;
import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.core.dto.SystemTaskType;
import com.google.common.collect.Lists;

@Component
public class SelfPlayRandomTaskProcessor extends AbstractSelfPlayTaskProcessor {
    @Override
    public SystemTaskType getType() {
        return SystemTaskType.SELF_PLAY_RANDOM;
    }

    @Override
    public Position judgeNextPosition(Chessboard chessboard) {
        List<Position> positions = findValidPositions(chessboard);
        int i = new Random().nextInt(positions.size());
        return positions.get(i);
    }

    private List<Position> findValidPositions(Chessboard chessboard) {
        List<Position> positions = Lists.newArrayList();
         for (int i = 0; i < chessboard.getRow(); i ++) {
             for (int j = 0; j < chessboard.getColumn(); j ++) {
                 if (chessboard.getChessmen()[i][j] == 0) {
                     positions.add(new Position(i, j));
                 }
             }
         }
         return positions;
    }
}
