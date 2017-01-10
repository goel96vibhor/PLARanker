package NeuralNet;

import Entities.RankList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibhor.go on 11/15/16.
 */

public class MRRScorer implements Scorer
{
    public Double score(RankList rankList)
    {
       Double score=0.0d;
       Double count=0.0d;
       for(int i=0;i<rankList.targetValues.size();i++)
       {
           if(rankList.targetValues.get(i)==1)
           {
               score+=1.0/(Double)(i+1.0);
               //break;
               count+=1.0;
           }
       }
        return score/count;
    }

    public ArrayList<ArrayList<Double>> swapChange(RankList rankList)
    {
       ArrayList<ArrayList<Double>> swapScoreDiff= new ArrayList<ArrayList<Double>>();
       for(int i=0;i<rankList.targetValues.size();i++)
       {
           ArrayList<Double> posSwapDiff= new ArrayList<Double>();
           for(int j=0;j<rankList.targetValues.size();j++)
           {
               if(rankList.targetValues.get(i)>rankList.targetValues.get(j))
                   posSwapDiff.add(-1.0/(1.0+i));
               else if(rankList.targetValues.get(j)>rankList.targetValues.get(i))
                   posSwapDiff.add(1.0/(1.0+j));
               else posSwapDiff.add(0.0d);
           }
           swapScoreDiff.add(posSwapDiff);
       }
        return swapScoreDiff;
    }

    public double getIdealScore(RankList rankList)
    {
        Double score=0.0d;
        Integer count=0;
        for(int i=0;i<rankList.targetValues.size();i++)
        {
            if(rankList.targetValues.get(i)==1)count++;
        }
        for (int i=1;i<=count;i++)score+=1.0/(Double)(i+0.0);
        score/=(Double)(count+0.0);
        return score;
    }

}