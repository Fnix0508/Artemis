package com.fnix.artemis.updater.systemtask;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fnix.artemis.base.match.MatchPlayService;
import com.fnix.artemis.base.model.Chessboard;
import com.fnix.artemis.base.model.MatchContext;
import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.base.model.RoundResult;
import com.fnix.artemis.core.dto.SystemTaskType;
import com.fnix.artemis.core.model.MatchHistory;
import com.fnix.artemis.core.model.SystemTask;
import com.fnix.artemis.core.repository.MatchHistoryDao;
import com.fnix.artemis.core.utils.DateUtils;
import com.fnix.artemis.core.utils.JsonUtils;
import com.google.common.collect.Lists;

public abstract class AbstractSelfPlayTaskProcessor implements SystemTaskProcessor {

    @Autowired
    private MatchPlayService matchPlayService;

    @Autowired
    private MatchHistoryDao matchHistoryDao;

    @Override
    public abstract SystemTaskType getType();

    @Override
    public void process(SystemTask systemTask) {
        MatchContext matchContext = matchPlayService.initMatchContext();
        List<Position> playOrder = Lists.newArrayList();
        LocalDateTime startTime = DateUtils.now();
        while(!matchContext.isFinish()) {
            Position position = judgeNextPosition(matchContext.getChessboard());
            RoundResult result = matchPlayService.play(matchContext.getCurrent(), position, matchContext);
            if (RoundResult.LIMIT == result) {
                throw new RuntimeException("该处限制着子 position=" + position);
            }
            if (RoundResult.NOT_TURN == result) {
                throw new RuntimeException("该回合不属于当前选手 player=" + matchContext.getCurrent());
            }
            playOrder.add(position);
            if (RoundResult.NOT_FINISH == result) {
                continue;
            }
            if (RoundResult.FINISH == result) {
                processResult(playOrder, matchContext, startTime, DateUtils.now());
            }
        }
    }

    private void processResult(List<Position> playOrder, MatchContext matchContext, LocalDateTime startTime, LocalDateTime endTime) {
        MatchHistory matchHistory = new MatchHistory();
        matchHistory.setBlackPlayerId(0L);
        matchHistory.setWhitePlayerId(0L);
        matchHistory.setChessmanTurn(JsonUtils.stringify(playOrder));
        matchHistory.setResult(matchContext.getFinishType());
        matchHistory.setStartTime(startTime);
        matchHistory.setEndTime(endTime);
        matchHistory.setChessboardColumn(matchContext.getChessboard().getColumn());
        matchHistory.setChessboardRow(matchContext.getChessboard().getRow());
        matchHistoryDao.save(matchHistory);
    }

    public abstract Position judgeNextPosition(Chessboard chessboard);
}
