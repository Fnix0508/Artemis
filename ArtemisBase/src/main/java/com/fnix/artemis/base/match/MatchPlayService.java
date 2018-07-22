package com.fnix.artemis.base.match;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.fnix.artemis.base.model.Chessboard;
import com.fnix.artemis.core.dto.FinishType;
import com.fnix.artemis.base.model.MatchContext;
import com.fnix.artemis.core.dto.Player;
import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.base.model.RoundResult;
import com.fnix.artemis.core.utils.Stream2;

/**  y_____
 * x|
 *  |
 *  |
 */
@Service
public class MatchPlayService {

    public MatchContext initMatchContext() {
        MatchContext context = new MatchContext();
        context.setChessboard(new Chessboard(15, 15));
        context.setCurrent(Player.BLACK);
        context.setFirst(Player.BLACK);
        return context;
    }

    public RoundResult play(Player player, Position position, MatchContext context) {
        if (context.getCurrent() != player) {
            return RoundResult.NOT_TURN;
        }
        int x = position.getX();
        int y = position.getY();
        int[][] chessmen = context.getChessboard().getChessmen();
        if (chessmen[x][y] != 0) {
            return RoundResult.LIMIT;
        }
        context.playAt(position);
        checkMatch(context);
        if (context.isFinish()) {
            return RoundResult.FINISH;
        }
        return RoundResult.NOT_FINISH;
    }

    private void checkMatch(MatchContext matchContext) {
        Position last = matchContext.getLastPosition();
        int row = matchContext.getChessboard().getRow();
        int column = matchContext.getChessboard().getColumn();
        int[][] chessmen = matchContext.getChessboard().getChessmen();
        int fromX = Math.max(0, last.getX() - 5);
        int toX = Math.min(row, last.getX() + 5);
        int[] checkArray = Arrays.copyOfRange(chessmen[last.getX()], fromX, toX);
        if (isFinal(checkArray)) {
            matchContext.setFinish(true);
            matchContext.setFinishType(matchContext.getLast() == Player.BLACK ? FinishType.BLACK_WIN : FinishType.WHITE_WIN);
            return;
        }
        checkArray = pickColumn(chessmen, last.getX(), last.getY(), row);
        if (isFinal(checkArray)) {
            matchContext.setFinish(true);
            matchContext.setFinishType(matchContext.getLast() == Player.BLACK ? FinishType.BLACK_WIN : FinishType.WHITE_WIN);
            return;
        }
        checkArray = pickDiagonalLeft(chessmen, last.getX(), last.getY(), row, column);
        if (isFinal(checkArray)) {
            matchContext.setFinish(true);
            matchContext.setFinishType(matchContext.getLast() == Player.BLACK ? FinishType.BLACK_WIN : FinishType.WHITE_WIN);
            return;
        }
        checkArray = pickDiagonalRight(chessmen, last.getX(), last.getY(), row, column);
        if (isFinal(checkArray)) {
            matchContext.setFinish(true);
            matchContext.setFinishType(matchContext.getLast() == Player.BLACK ? FinishType.BLACK_WIN : FinishType.WHITE_WIN);
            return;
        }
        if (matchContext.getRoundIndex() == row * column) {
            matchContext.setFinish(true);
            matchContext.setFinishType(FinishType.DRAW);
        }
    }

    private boolean isFinal(int[] array) {
        if (array.length < 5) {
            return false;
        }
        for (int i = 0; i + 5 <= array.length; i ++) {
            int[] cal = Arrays.copyOfRange(array, i, i + 5);
            int sum = Stream2.sum(cal);
            if (sum == -5 || sum == 5) {
                return true;
            }
        }
        return false;
    }

    private int[] pickColumn(int[][] array, int x, int y, int row) {
        int fromX = Math.max(0, x - 5);
        int toX = Math.min(row, x + 5);
        int[] picked = new int[toX - fromX];
        for (int i = fromX, j = 0; i < toX; i ++, j ++) {
            picked[j] = array[i][y];
        }
        return picked;
    }

    /**
     * \
     */
    private int[] pickDiagonalLeft(int[][] array, int x, int y, int row, int column) {
        int fromX, fromY, toX, toY;
        if (x < y) {
            fromX = Math.max(0, x - 5);
            fromY = y - (x - fromX);
        } else {
            fromY = Math.max(0, y - 5);
            fromX = x - (y - fromY);
        }
        if (row - x < column - y) {
            toX = Math.min(row, x + 5);
            toY = y + (toX - x);
        } else  {
            toY = Math.min(column, y + 5);
            toX = x + (toY - y);
        }
        if (toX - fromX < 5) {
            return new int[]{1};
        }
        int[] picked = new int[toX - fromX];
        for (int fx = fromX, fy = fromY, j = 0; fx < toX && fy < toY; fx ++, fy ++, j ++ ) {
            picked[j] = array[fx][fy];
        }
        return picked;
    }

    /**
     * /
     */
    private int[] pickDiagonalRight(int[][] array, int x, int y, int row, int column) {
        int fromX, fromY, toX, toY;
        if (row - x < y) {
            fromX = Math.min(row, x + 5);
            fromY = y - (x - fromX);
        } else {
            fromY = Math.max(0, y - 5);
            fromX = x + (y - fromY);
        }
        if (x < column - y) {
            toX = Math.max(0, x - 5);
            toY = y + (toX - x);
        } else  {
            toY = Math.min(column, y + 5);
            toX = x - (toY - y);
        }
        if (toX - fromX < 5) {
            return new int[]{1};
        }
        int[] picked = new int[toX - fromX];
        for (int fx = fromX, fy = fromY, j = 0; fx < toX && fy < toY; fx ++, fy ++, j ++ ) {
            picked[j] = array[fx][fy];
        }
        return picked;
    }

}
