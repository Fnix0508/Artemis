package com.fnix.artemis.base.model;

import java.util.Map;

import com.fnix.artemis.core.dto.FinishType;
import com.fnix.artemis.core.dto.Player;
import com.google.common.collect.ImmutableMap;

import lombok.Data;

@Data
public class MatchContext {

    private static final Map<Player, Player> NEXT_PLAYER = ImmutableMap.of(Player.BLACK, Player.WHITE, Player.WHITE, Player.BLACK);

    private Long matchId;

    private Player last;

    private Player current;

    private Player first;

    private boolean finish;

    private int roundIndex;

    private FinishType finishType;

    private Chessboard chessboard;

    private Position lastPosition;

    public int currentValue () {
        return current == Player.BLACK ? 1 : -1;
    }

    public void playAt(Position position) {
        chessboard.getChessmen()[position.getX()][position.getY()] = currentValue();
        last = current;
        current = NEXT_PLAYER.get(current);
        lastPosition = new Position(position.getX(), position.getY());
        roundIndex ++;
    }
}
