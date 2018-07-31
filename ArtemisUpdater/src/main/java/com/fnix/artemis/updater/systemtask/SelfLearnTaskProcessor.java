package com.fnix.artemis.updater.systemtask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fnix.artemis.base.brain.ArtemisBrain;
import com.fnix.artemis.base.model.Chessboard;
import com.fnix.artemis.base.model.LearningData;
import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.core.dto.FinishType;
import com.fnix.artemis.core.dto.ProgressType;
import com.fnix.artemis.core.dto.SystemTaskType;
import com.fnix.artemis.core.model.MatchHistory;
import com.fnix.artemis.core.model.NeuronWeight;
import com.fnix.artemis.core.model.NeuronWeightHistory;
import com.fnix.artemis.core.model.SystemProgress;
import com.fnix.artemis.core.model.SystemTask;
import com.fnix.artemis.core.repository.MatchHistoryDao;
import com.fnix.artemis.core.repository.NeuronWeightDao;
import com.fnix.artemis.core.repository.NeuronWeightHistoryDao;
import com.fnix.artemis.core.service.NeuronWeightService;
import com.fnix.artemis.core.service.SystemProgressService;
import com.fnix.artemis.core.utils.DateUtils;
import com.fnix.artemis.core.utils.JsonUtils;
import com.fnix.artemis.core.utils.Stream2;
import com.fnix.artemis.core.utils.ThreadUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Component
public class SelfLearnTaskProcessor implements SystemTaskProcessor {

    private LocalDateTime LEARN_START = LocalDate.of(2018, 7, 1).atStartOfDay();

    @Autowired
    private SystemProgressService systemProgressService;

    @Autowired
    private MatchHistoryDao matchHistoryDao;

    @Autowired
    private ArtemisBrain artemisBrain;

    @Autowired
    private NeuronWeightService neuronWeightService;

    @Autowired
    private NeuronWeightDao neuronWeightDao;

    @Autowired
    private ExecutorService subExecutor;

    @Override
    public SystemTaskType getType() {
        return SystemTaskType.SELF_LEARN;
    }

    @Override
    public void process(SystemTask systemTask) {
        SystemProgress progress = systemProgressService.getOrDefault(ProgressType.SELF_LEARN, LEARN_START);
        MatchHistory matchHistory = matchHistoryDao.incrementGetOne(progress.getLastId());
        if (matchHistory == null) {
            return;
        }
        learnFromHistory(matchHistory);
        progress.setLastTime(matchHistory.getLastModified());
        progress.setLastId(matchHistory.getId());
        systemProgressService.save(progress);
    }

    private void learnFromHistory (MatchHistory history) {
        if (history.getResult() == FinishType.DRAW) {
            //平局不予学习
            return;
        }
        List<Position> playTurn = JsonUtils.toObject(history.getChessmanTurn(), new TypeReference<List<Position>>() {});
        if (history.getResult() == FinishType.BLACK_WIN || history.getResult() == FinishType.WHITE_RESIGN) {
            //黑棋胜
            artemisBrain.train(build(true, playTurn, history.getChessboardRow(), history.getChessboardColumn()));
        } else if(history.getResult() == FinishType.WHITE_WIN || history.getResult() == FinishType.BLACK_RESIGN) {
            //白棋胜
            artemisBrain.train(build(false, playTurn, history.getChessboardRow(), history.getChessboardColumn()));
        }
        List<NeuronWeight> weights = artemisBrain.exportNetworkWeight();
        List<NeuronWeight> saveWeights = setValue(weights);
        neuronWeightService.save(saveWeights);
    }

    private List<LearningData> build(boolean blackWin, List<Position> playTurn, int row, int column) {
        Chessboard chessboard = new Chessboard(row, column);
        List<LearningData> result = Lists.newArrayList();
        int currLearn = blackWin ? 0 : 1;
        for (int i = 0; i < playTurn.size(); i ++) {
            Position p = playTurn.get(i);
            if (currLearn == i) {
                Chessboard currChessboard = new Chessboard(row, column);
                int[][] chessmen = copy(chessboard.getChessmen(), row, column);
                currChessboard.setChessmen(chessmen);
                result.add(new LearningData(currChessboard, p));
                currLearn += 2;
            }
            int chessmanVal = i % 2 == 0 ? 1 : -1;
            chessboard.getChessmen()[p.getX()][p.getY()] = chessmanVal;
        }
        return result;
    }

    private int[][] copy(int[][] chessmen, int row, int column) {

        int[][] result = new int[row][column];
        for(int i = 0; i < row; i ++) {
            System.arraycopy(chessmen[i], 0, result[i], 0, column);
        }
        return result;
    }

    private List<NeuronWeight> setValue(List<NeuronWeight> weights) {
        Vector<NeuronWeight> v = new Vector<>(weights.size());
        List<Future> futures = Stream2.map(weights, w -> subExecutor.submit(() -> v.add(setValue(w))));
        ThreadUtils.waitAllDone(futures);
        return Stream2.map(v.toArray(), n -> (NeuronWeight)n);
    }

    private NeuronWeight setValue(NeuronWeight weight) {
        NeuronWeight neuronWeight = neuronWeightDao.findByToIdAndFromId(weight.getToId(), weight.getFromId());
        neuronWeight.setValue(weight.getValue());
        return neuronWeight;
    }
}
