package com.fnix.artemis.base.brain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fnix.artemis.core.CoreProperties;
import com.fnix.artemis.core.model.ArtemisLayer;
import com.fnix.artemis.core.model.ArtemisNeuron;
import com.fnix.artemis.core.model.NeuronWeight;
import com.fnix.artemis.core.repository.ArtemisLayerDao;
import com.fnix.artemis.core.repository.ArtemisNeuronDao;
import com.fnix.artemis.core.repository.NeuronWeightDao;
import com.fnix.artemis.core.utils.Stream2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class ArtemisBrainFactoryBean {

    @Autowired
    private ArtemisLayerDao artemisLayerDao;

    @Autowired
    private ArtemisNeuronDao artemisNeuronDao;

    @Autowired
    private NeuronWeightDao neuronWeightDao;

    @Autowired
    private CoreProperties coreProperties;

    public ArtemisBrain create() {
        List<ArtemisLayer> artemisLayers =
                artemisLayerDao.findByNetworkCode(coreProperties.getNetworkCode(), new Sort(Sort.Direction.ASC, "seq"));
        List<Integer> neuronsInLayers = Stream2.map(artemisLayers, ArtemisLayer::getNum);
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(neuronsInLayers);
        artemisLayers.forEach(i -> setNeuronToNetwork(neuralNetwork, i));
        setConnect(neuralNetwork);
        return new ArtemisBrain(neuralNetwork);
    }

    private void setNeuronToNetwork(NeuralNetwork neuralNetwork, ArtemisLayer artemisLayer) {
        Layer layer = neuralNetwork.getLayerAt(artemisLayer.getSeq());
        layer.setLabel(artemisLayer.getId().toString());
        List<ArtemisNeuron> neurons = artemisNeuronDao.findByLayerId(artemisLayer.getId());
        neurons.forEach(n -> setNeuron(layer, n));
    }

    private void setNeuron(Layer layer, ArtemisNeuron artemisNeuron) {
        layer.getNeurons()[artemisNeuron.getSeq()].setLabel(artemisNeuron.getId().toString());
    }

    private void setConnect(NeuralNetwork neuralNetwork) {
        List<Neuron> allNeurons = Lists.newArrayList();
        Stream2.forEach(neuralNetwork.getLayers(), l -> allNeurons.addAll(Arrays.asList(l.getNeurons())));
        List<Neuron> localNeurons = Stream2.filter(allNeurons, n -> n.getLabel() != null);
        Map<Long, Neuron> maps = Stream2.toMap(localNeurons, i -> Long.parseLong(i.getLabel()));
        maps.keySet().forEach(i -> setTargetNeuronConnect(i, maps));

    }

    private void setTargetNeuronConnect(Long key, Map<Long, Neuron> maps) {
        Map<Long, NeuronWeight> fromMap = Stream2.toMap(neuronWeightDao.findByToId(key), NeuronWeight::getFromId);
        Neuron target = maps.get(key);
        for (Connection c : target.getInputConnections()) {
            if (StringUtils.isEmpty(c.getFromNeuron().getLabel())) {
                continue;
            }
            NeuronWeight weight = fromMap.get(Long.parseLong(c.getFromNeuron().getLabel()));
            c.getWeight().setValue(Double.parseDouble(weight.getValue()));
        }
        Map<Long, NeuronWeight> toMap = Stream2.toMap(neuronWeightDao.findByFromId(key), NeuronWeight::getToId);
        for (Connection c : target.getOutConnections()) {
            if (StringUtils.isEmpty(c.getFromNeuron().getLabel())) {
                continue;
            }
            NeuronWeight weight = toMap.get(Long.parseLong(c.getToNeuron().getLabel()));
            c.getWeight().setValue(Double.parseDouble(weight.getValue()));
        }
    }

    public static void main(String [] args) {
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(2, 2, 2);
        DataSet dataSet = new DataSet(2, 2);
        dataSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0, 1}));

        neuralNetwork.learn(dataSet);
        System.out.print(neuralNetwork);
    }

    private void init() {
        List<ArtemisLayer> layers = artemisLayerDao.findByNetworkCode(coreProperties.getNetworkCode(), new Sort(Sort.Direction.ASC, "seq"));
        if (layers.isEmpty()) {
            //build layers
            for (int i = 0; i < 3; i ++) {
                ArtemisLayer layer = new ArtemisLayer();
                layer.setNetworkCode(coreProperties.getNetworkCode());
                layer.setNum(225);
                layer.setSeq(i);
                layers.add(artemisLayerDao.save(layer));
            }
        }
        Map<Integer, List<ArtemisNeuron>> map = Maps.newHashMap();
        for (ArtemisLayer layer : layers) {
            List<ArtemisNeuron> neurons = artemisNeuronDao.findByLayerId(layer.getId());
            if (neurons.isEmpty()) {
                for (int i = 0; i < layer.getNum(); i ++) {
                    ArtemisNeuron neuron = new ArtemisNeuron();
                    neuron.setLayerId(layer.getId());
                    neuron.setSeq(i);
                    neurons.add(artemisNeuronDao.save(neuron));
                }
            }
            map.put(layer.getSeq(), neurons);
        }

        for (int i = 0; i < 2; i ++) {
            List<ArtemisNeuron> neurons = map.get(i);
            List<ArtemisNeuron> nextNeurons = map.get(i + 1);
            createWeight(neurons, nextNeurons);
        }
    }

    private void createWeight(List<ArtemisNeuron> neurons, List<ArtemisNeuron> nextNeurons) {
        for (ArtemisNeuron neuron : neurons) {
            List<NeuronWeight> weights = Lists.newArrayList();
            for (ArtemisNeuron nextNeuron : nextNeurons) {
                weights.add(new NeuronWeight(neuron.getId(), nextNeuron.getId(), "0.5"));
            }
            neuronWeightDao.saveAll(weights);
        }
    }

}
