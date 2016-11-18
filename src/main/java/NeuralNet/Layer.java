package NeuralNet;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by vibhor.go on 11/14/16.
 */
public class Layer {

    public List<Neuron> neurons;

    public Layer(int numofNeurons)
    {
        neurons= new ArrayList<Neuron>();
        for(int i=0;i<numofNeurons;i++)
            neurons.add(new Neuron());
    }

    public void connectLayer(Layer inputLayer)
    {
       for(Neuron inputNeuron: inputLayer.neurons)
       {
           for (Neuron outputNeuron: neurons)
           {
               Synapse synapse= new Synapse(inputNeuron, outputNeuron);

           }
       }
    }

    public void connectNeuron(Neuron biasNeuron)
    {
        for(Neuron outputNeuron: neurons)
        {
            Synapse synapse= new Synapse(biasNeuron, outputNeuron);

        }
    }

    public void computeOutput(int index)
    {
        for(Neuron neuron:neurons)
        {
            neuron.computeOutput(index);
        }
    }

    public void computeDelta(ArrayList<ArrayList<Integer> > pairmap, ArrayList<ArrayList<Double> > pairweight, int current)
    {
        for(Neuron neuron:neurons)
        {
            neuron.computeInnerDeltas(pairmap,pairweight,current);
        }
    }

    public void updateWeight(ArrayList<ArrayList<Integer> > pairmap, int current)
    {
        for(Neuron neuron:neurons)
        {
            neuron.updateWeight(pairmap,current);
        }
    }

    public ArrayList<Double> getOutlinkWeights()
    {
        ArrayList<Double> layerWeights= new ArrayList<Double>();
        for(Neuron neuron:neurons)
        {
            for(Synapse synapse: neuron.outlinks)
            {
                layerWeights.add(synapse.getWeight());
            }
        }
        return layerWeights;
    }

    public void setOutlinkWeights(ArrayList<Double> layerWeights)
    {
        int i=0;
        for(Neuron neuron:neurons)
        {
            for(Synapse synapse: neuron.outlinks)
            {
                synapse.setWeight(layerWeights.get(i++));
            }
        }
    }

    public void setInput(int ranklistSize)
    {
        for(Neuron neuron:neurons)
        {
            for(int i=0;i<ranklistSize;i++)
            {
                neuron.outputs.add(0.0d);
            }
        }
    }

}