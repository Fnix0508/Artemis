package com.fnix.artemis.base.brain;

import java.util.Arrays;
import java.util.List;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.IterativeLearning;
import org.springframework.util.StringUtils;

import com.fnix.artemis.base.model.Chessboard;
import com.fnix.artemis.base.model.LearningData;
import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.base.model.PositionWeight;
import com.fnix.artemis.core.model.NeuronWeight;
import com.fnix.artemis.core.utils.Stream2;
import com.google.common.collect.Lists;

public class ArtemisBrain {

    ArtemisBrain(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    private NeuralNetwork neuralNetwork;

    public NeuralNetwork getNeuralNetwork() {
        return this.neuralNetwork;
    }

    public Position judgeNextPosition(Chessboard chessboard) {
        double[] inputs = getInputs(chessboard);
        neuralNetwork.setInput(inputs);
        neuralNetwork.calculate();
        double[] outputs = neuralNetwork.getOutput();
        return determinePosition(outputs, chessboard);
    }

    public List<NeuronWeight> exportNetworkWeight() {
        List<NeuronWeight> weights = Lists.newArrayList();
        Stream2.forEach(neuralNetwork.getLayers(), l -> weights.addAll(getOutputWeightsFromLayer(l)));
        return weights;
    }

    public void train(List<LearningData> learningData) {
        int row = learningData.get(0).getChessboard().getRow();
        int column = learningData.get(0).getChessboard().getColumn();
        DataSet dataSet = new DataSet(row * column, row * column);
        learningData.forEach(i ->
                dataSet.addRow(new DataSetRow(getInputs(i.getChessboard()), buildOutput(i.getNext(), row, column))));
        IterativeLearning learningRule = (IterativeLearning)neuralNetwork.getLearningRule();
        learningRule.setMaxIterations(1);
        neuralNetwork.learn(dataSet);
    }

    private double[] buildOutput(Position position, int row, int column) {
        double[] outputs = new double[row * column];
        outputs[position.getX() * row + position.getY()] = 1;
        return outputs;
    }

    private double[] getInputs(Chessboard chessboard) {
        double[] inputs = new double[chessboard.getRow() * chessboard.getColumn()];
        int[][] chessmen = chessboard.getChessmen();
        for(int i = 0; i < chessboard.getRow(); i ++) {
            for (int j = 0; j < chessboard.getColumn(); j ++) {
                int idx = i * chessboard.getColumn() + j;
                inputs[idx] = chessmen[i][j];
            }
        }
        return inputs;
    }

    private Position determinePosition(double[] weights, Chessboard chessboard) {
        Arrays.sort(weights);
        List<PositionWeight> pws = Lists.newArrayList();
        for (int i = 0; i < weights.length; i ++) {
            pws.add(new PositionWeight(i, weights[i]));
        }
        pws = Stream2.sort(pws, (t1, t2) -> t2.getValue().compareTo(t1.getValue()));
        for (PositionWeight w : pws) {
            int row = w.getIndex() / chessboard.getColumn();
            int column = w.getIndex() % chessboard.getRow();
            if (chessboard.getChessmen()[row][column] == 0) {
                return new Position(row, column);
            }
        }
        throw new RuntimeException("未找到合适的落子位置");
    }

    private List<NeuronWeight> getOutputWeightsFromLayer(Layer layer) {
        List<NeuronWeight> layerWeights = Lists.newArrayList();
        for (Neuron neuron : layer.getNeurons()) {
            if (StringUtils.isEmpty(neuron.getLabel())) {
                continue;
            }
            Connection[] connections = neuron.getOutConnections();
            List<NeuronWeight> weights = Stream2.map(connections, this::fromConnection);
            layerWeights.addAll(weights);
        }
        return layerWeights;
    }

    private NeuronWeight fromConnection(Connection connection) {
        return new NeuronWeight(Long.parseLong(connection.getFromNeuron().getLabel()),
                Long.parseLong(connection.getToNeuron().getLabel()),
                String.valueOf(connection.getWeight().getValue()));
    }
}
