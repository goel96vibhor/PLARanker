package NeuralNet;

import java.util.ArrayList;

/**
 * Created by vibhor.go on 11/16/16.
 */

public class NeuralNetLoader {

    public String modelName;
    public int inputSize;
    public int outputSize;
    //public ArrayList<Integer> featuresUsed;
    public int hiddenLayers;
    public int hiddenLayerNeuronCount;

    public ArrayList<ArrayList<Double>>  layerWeights;

    public void saveModelParameters(RankNet rankNet)
    {
        this.modelName=rankNet.getName();
        this.inputSize= rankNet.getInputSize();
        this.outputSize=rankNet.getOutputSize();
        this.hiddenLayers=rankNet.layers.size()-2;
        hiddenLayerNeuronCount= rankNet.layers.get(1).neurons.size();
        layerWeights= new ArrayList<ArrayList<Double>>();

        for(int i=0;i<rankNet.layers.size()-1;i++)
        {
            layerWeights.add(rankNet.layers.get(i).getOutlinkWeights());
        }

    }

    public RankNet loadModel()
    {
        RankNet rankNet;
        if(modelName.equalsIgnoreCase("RankNet"))rankNet=new RankNet();
        else if (modelName.equalsIgnoreCase("LambdaRank"))rankNet= new LambdaRank();
        else return null;
        rankNet.setInputSize(inputSize);
        rankNet.setOutputSize(outputSize);
        rankNet.setnHiddenLayer(hiddenLayers);
        rankNet.setnHiddenNodeperLayer(hiddenLayerNeuronCount);
        rankNet.initialize();
        rankNet.setLayerWeights(layerWeights);
        rankNet.saveBestValidationModel();
        return rankNet;

    }

}
