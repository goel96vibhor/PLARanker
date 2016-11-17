package NeuralNet;

import Entities.RankList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibhor.go on 11/15/16.
 */

public class LambdaRank extends RankNet
{
    public LambdaRank(){}

    public LambdaRank(List<RankList> trainSamples, List<RankList> validationSamples, int inputSize, int outputSize)
    {
        super(trainSamples,validationSamples,inputSize,outputSize);
    }

    public RankList reorder(RankList rl)
    {
        return rl;
    }

    public ArrayList<ArrayList<Double> > computePairWeight(ArrayList<ArrayList<Integer> > pairMap, RankList rankList)
    {
        ArrayList<Integer> pairmap_i;
        ArrayList<Double> posSwapDiff;
        ArrayList<ArrayList<Double>> pairweight= new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> swapScoreDiff = mrrScorer.swapChange(rankList);
        for(int i=0;i<pairMap.size();i++)
        {
            pairmap_i=pairMap.get(i);
            posSwapDiff= swapScoreDiff.get(i);
            ArrayList<Double> pairweight_i= new ArrayList<Double>();
            for(int k=0;k<pairmap_i.size();k++)
            {
                int j=pairmap_i.get(k);
                int sign= (rankList.targetValues.get(i)>rankList.targetValues.get(j)?1:-1);
                pairweight_i.add(posSwapDiff.get(j)*sign);
            }
            pairweight.add(pairweight_i);
        }
        return pairweight;
    }

    public String getName()
    {
        return "LambdaRank";
    }

}
