package NeuralNet;

import Entities.RankList;
import Utils.FeatureManager;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibhor.go on 11/14/16.
 */

public class RankNet{
    private int inputSize;
    private int outputSize=1;
    protected List<Layer> layers;
    private int nHiddenLayer=1;
    private int nHiddenNodeperLayer=10;
    private static int nIterations=100;
    private List<RankList> trainSamples;
    private List<RankList> validationSamples;
    protected double bestScoreonValidation=0.0;
    private ArrayList<ArrayList<Double>> bestValidationModel= new ArrayList<ArrayList<Double>>();
    protected static Scorer mrrScorer= new MRRScorer();
    public RankNet(List<RankList> trainSamples, List<RankList> validationSamples, int inputSize, int outputSize)
    {
        this.inputSize=inputSize;
        this.outputSize=outputSize;
        this.trainSamples= trainSamples;
        this.validationSamples=validationSamples;
        this.layers= new ArrayList<Layer>();
    }

    public RankNet(){}

    public void initialize()
    {
        layers.add(new Layer(inputSize+1));
        for(int i=0;i<nHiddenLayer;i++)layers.add(new Layer(nHiddenNodeperLayer));
        layers.add(new Layer(outputSize));
        for(int i=1;i<layers.size();i++)layers.get(i).connectLayer(layers.get(i-1));
        Layer inputLayer= layers.get(0);
        //connect bias to all layers
        for(int i=2;i<layers.size();i++){
            layers.get(i).connectNeuron(inputLayer.neurons.get(inputLayer.neurons.size()-1));

        }
        for(int i=0;i<layers.size()-1;i++)bestValidationModel.add(new ArrayList<Double>());

    }

    public void setInput(RankList rl)
    {
        List<Neuron> inputLayerNeurons= layers.get(0).neurons;
        for(List<Double> sampleFeatures: rl.listFeatures)
        {
            for(int i=0;i<sampleFeatures.size();i++)
            {
                inputLayerNeurons.get(i).outputs.add(sampleFeatures.get(i)) ;
            }
            inputLayerNeurons.get(inputLayerNeurons.size()-1).setOutput(1.0f);
        }

    }

    public void clearInputs()
    {
        for(Layer layer: layers)
        {
            for(Neuron neuron: layer.neurons)
            {
                neuron.outputs.clear();
            }
        }
    }

    public ArrayList<ArrayList<Integer>> batchFeedForward(RankList rankList,int [] sortIndex)
    {
        for(int i=0;i<rankList.listFeatures.size();i++)
        {
            for(int j=1;j<layers.size();j++)
            {
                layers.get(j).computeOutput(i);
            }
        }
        ArrayList<ArrayList<Integer> > pairmap= new ArrayList<ArrayList<Integer> >();
        for(int i=0;i<rankList.listFeatures.size();i++)
        {
            int k=0;
            ArrayList<Integer> pairmap_i=new ArrayList<Integer>();
            for(int j=0;j<rankList.listFeatures.size();j++)
            {
                if((rankList.targetValues.get(i)>rankList.targetValues.get(j))&&sortIndex[i]>sortIndex[j]){
                    pairmap_i.add(j);
                }
            }
            pairmap.add(pairmap_i);
        }
        return pairmap;
    }

    public ArrayList<ArrayList<Double> > computePairWeight(ArrayList<ArrayList<Integer> > pairmap)
    {
        return null;
    }

    public void batchBackPropagate(ArrayList<ArrayList<Integer> > pairMap, ArrayList<ArrayList<Double> >pairWeight)
    {
        for(int i=0;i<pairMap.size();i++)
        {
            Layer outputLayer= layers.get(layers.size()-1);
            for(Neuron neuron: outputLayer.neurons){
                neuron.computeOuterDeltas(pairMap, pairWeight, i);
            }
            for(int j=layers.size()-2;j>0;j--)
            {
                layers.get(j).computeDelta(pairMap,pairWeight,i);
            }
            for(int j=layers.size()-1;j>0;j--)
            {
                layers.get(j).updateWeight(pairMap,i);
            }
        }
    }

    public RankList reorder(RankList rankList, int [] sortIndex)
    {

        return new RankList(rankList, sortIndex);
    }

    public void trainIteration()
    {
        for(RankList rl:trainSamples)
        {
            ArrayList<Double> scoresOnModel=eval(rl);
            int [] sortIndex=getSortedIndices(scoresOnModel, false);
            RankList rankList= reorder(rl,sortIndex);
            clearInputs();
            setInput(rankList);
            ArrayList<ArrayList<Integer> > pairmap= batchFeedForward(rankList,sortIndex);
            ArrayList<ArrayList<Double> > pairWeight = computePairWeight(pairmap);
            batchBackPropagate(pairmap,pairWeight);

        }
    }

    public void train()
    {
        Double score;
        for(int i=0;i<nIterations;i++)
        {
            trainIteration();
            if(validationSamples!=null)
            {
                score=0.0;
                for(RankList rl:validationSamples)
                {
                    score+=mrrScorer.score(rankedProducts(rl));
                }
                score/=validationSamples.size();
                if(score>bestScoreonValidation)
                {
                    bestScoreonValidation=score;
                    saveBestValidationModel();
                }
            }


        }
        if (validationSamples!=null)loadBestValidationModel();

        score=0.0;
        for (RankList rankList:trainSamples)
        {
            score+=mrrScorer.score(rankedProducts(rankList));
        }
        score/=trainSamples.size();

        if (validationSamples!=null)
        {
            score=0.0;
            for (RankList rankList:validationSamples)
            {
                score+=mrrScorer.score(rankedProducts(rankList));
            }
            score/=validationSamples.size();
        }

    }

    public void saveBestValidationModel()
    {
        for(int i=0;i<layers.size()-1;i++)
        {
            bestValidationModel.get(i).clear();
            bestValidationModel.set(i,layers.get(i).getOutlinkWeights());
        }
    }

    public void loadBestValidationModel()
    {
        for (int i=0;i<layers.size()-1;i++)
        {
            layers.get(i).setOutlinkWeights(bestValidationModel.get(i));
        }
    }

    public void setLayerWeights(ArrayList<ArrayList<Double>> layerWeights)
    {
        for (int i=0;i<layers.size()-1;i++)
        {
            layers.get(i).setOutlinkWeights(layerWeights.get(i));
        }
    }


    public int[] getSortedIndices(ArrayList<Double> scores, boolean asc)
    {
        int[] sortedIndex = new int[scores.size()];
        for(int i=0;i<sortedIndex.length;i++)sortedIndex[i]=i;
        int swapIndex, temp;
        for(int i=0;i<sortedIndex.length;i++)
        {
            swapIndex=i;
            for(int j=i+1;j<sortedIndex.length;j++)
            {

                if((scores.get(sortedIndex[j])<scores.get(sortedIndex[swapIndex])) && asc==true)
                    swapIndex=j;
                else if((scores.get(sortedIndex[j])>scores.get(sortedIndex[swapIndex])) && asc==false)
                    swapIndex=j;
            }
            temp=sortedIndex[swapIndex];
            sortedIndex[swapIndex]=sortedIndex[i];
            sortedIndex[i]=temp;
        }

        return sortedIndex;
    }

    public ArrayList<Double> eval(RankList rankList)
    {
        clearInputs();
        setInput(rankList);
        for(int i=1;i<layers.size();i++)
        {
            for(int j=0;i<rankList.listFeatures.size();j++)
            {
                layers.get(i).computeOutput(j);
            }
        }
        return layers.get(layers.size()-1).neurons.get(0).outputs;
    }

    public RankList rankedProducts(RankList rl)
    {
        ArrayList<Double> scoresOnModel=eval(rl);
        int [] sortIndex=getSortedIndices(scoresOnModel, false);
        return reorder(rl,sortIndex);
    }

    public String getName()
    {
        return "RankNet";
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    public void setnHiddenLayer(int number)
    {
        this.nHiddenLayer= number;
    }

    public int getnHiddenNodeperLayer() {
        return nHiddenNodeperLayer;
    }

    public void setnHiddenNodeperLayer(int nHiddenNodeperLayer) {
        this.nHiddenNodeperLayer = nHiddenNodeperLayer;
    }

    public void setMrrScorer(Scorer scorer)
    {
        mrrScorer=scorer;
    }
}