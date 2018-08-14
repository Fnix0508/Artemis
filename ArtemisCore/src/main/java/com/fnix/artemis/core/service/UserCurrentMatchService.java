package com.fnix.artemis.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fnix.artemis.base.brain.ArtemisBrain;
import com.fnix.artemis.base.match.MatchPlayService;
import com.fnix.artemis.base.model.MatchContext;
import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.base.model.RoundResult;
import com.fnix.artemis.core.dto.CurrentMatchDto;
import com.fnix.artemis.core.dto.Player;
import com.fnix.artemis.core.model.CurrentMatch;
import com.fnix.artemis.core.model.CurrentMatchOrder;
import com.fnix.artemis.core.model.UserCurrentMatch;
import com.fnix.artemis.core.repository.CurrentMatchDao;
import com.fnix.artemis.core.repository.CurrentMatchOrderDao;
import com.fnix.artemis.core.repository.UserCurrentMatchDao;
import com.fnix.artemis.core.utils.ExceptionUtils;

@Service
public class UserCurrentMatchService {

    @Autowired
    private UserCurrentMatchDao userCurrentMatchDao;

    @Autowired
    private CurrentMatchDao currentMatchDao;

    @Autowired
    private CurrentMatchOrderDao currentMatchOrderDao;

    @Autowired
    private MatchPlayService matchPlayService;

    @Autowired
    private ArtemisBrain artemisBrain;

    public CurrentMatchDto getCurrentMatch(long userId) {
        UserCurrentMatch match = userCurrentMatchDao.findByUserId(userId);
        if (match == null) {
            return new CurrentMatchDto();
        }
        CurrentMatch currentMatch = currentMatchDao.findById(match.getCurrentMatchId()).orElse(null);
        return new CurrentMatchDto(match, currentMatch);
    }

    @Transactional
    public CurrentMatchDto create(long userId) {
        CurrentMatch currentMatch = new CurrentMatch(userId, 0L);
        currentMatch = currentMatchDao.save(currentMatch);
        UserCurrentMatch userCurrentMatch = new UserCurrentMatch(userId, Player.BLACK, currentMatch.getId());
        userCurrentMatchDao.save(userCurrentMatch);
        return new CurrentMatchDto(userCurrentMatch, currentMatch);
    }

    public List<CurrentMatchOrder> getMatchOrder(long userId, long currentMatchId, int roundIndex) {
        return currentMatchOrderDao.findByRoundIndex(currentMatchId, roundIndex);
    }

    @Transactional
    public CurrentMatchDto play(long userId, Position position) {
        UserCurrentMatch userCurrentMatch = userCurrentMatchDao.findByUserId(userId);
        if (userCurrentMatch == null) {
            throw ExceptionUtils.matchAlreadyFinishedException();
        }
        CurrentMatch currentMatch = currentMatchDao.findById(userCurrentMatch.getCurrentMatchId()).orElse(new CurrentMatch());
        if (currentMatch.isFinished()) {
            throw ExceptionUtils.matchAlreadyFinishedException();
        }
        List<CurrentMatchOrder> currentMatchOrders =
                currentMatchOrderDao.findByCurrentMatchId(currentMatch.getId(), new Sort(Sort.Direction.ASC, "roundIndex"));
        MatchContext matchContext = matchPlayService.initMatchContext();
        for (CurrentMatchOrder order : currentMatchOrders) {
            Player player = order.getRoundIndex() % 2 == 0 ? Player.WHITE : Player.BLACK;
            matchPlayService.play(player, new Position(order.getPositionX(), order.getPositionY()), matchContext);
        }

        RoundResult roundResult = matchPlayService.play(userCurrentMatch.getPlayer(), position, matchContext);
        if (roundResult == RoundResult.NOT_TURN || roundResult == RoundResult.LIMIT) {
            throw ExceptionUtils.matchNotTurnException();
        }
        currentMatchOrderDao.save(new CurrentMatchOrder(currentMatch.getId(), currentMatch.getRoundIndex() + 1, position.getX(), position.getY()));
        currentMatch.setRoundIndex(currentMatch.getRoundIndex() + 1);
        if (roundResult == RoundResult.NOT_FINISH) {
            currentMatchDao.save(currentMatch);
        } else {
            currentMatch.setFinished(true);
            currentMatch.setFinishType(matchContext.getFinishType());
            currentMatchDao.save(currentMatch);
            return new CurrentMatchDto(true, matchContext.getFinishType());
        }

        Position p = artemisBrain.judgeNextPosition(matchContext.getChessboard());
        RoundResult roundBrain = matchPlayService.play(Player.WHITE, p, matchContext);
        if (roundBrain == RoundResult.NOT_TURN || roundBrain == RoundResult.LIMIT) {
            throw ExceptionUtils.matchNotTurnException();
        }
        currentMatchOrderDao.save(new CurrentMatchOrder(currentMatch.getId(), currentMatch.getRoundIndex() + 1, p.getX(), p.getY()));
        currentMatch.setRoundIndex(currentMatch.getRoundIndex() + 1);
        if (roundBrain == RoundResult.NOT_FINISH) {
            currentMatchDao.save(currentMatch);
        } else {
            currentMatch.setFinished(true);
            currentMatch.setFinishType(matchContext.getFinishType());
            currentMatchDao.save(currentMatch);
        }
        return new CurrentMatchDto(true, matchContext.getFinishType());
    }



}
