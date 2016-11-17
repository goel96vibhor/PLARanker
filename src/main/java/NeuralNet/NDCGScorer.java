package NeuralNet;

import Entities.RankList;
import ciir.umass.edu.utilities.SimpleMath;

import java.util.ArrayList;
import java.util.Arrays;

import Utils.ArrayIndexComparator;

/**
 * Created by vibhor.go on 11/17/16.
 */

public class NDCGScorer implements Scorer
{
    protected static double [] discount=null;
    protected static double [] gains= null;
    protected static int ndcgRelSize =10;
    protected static int nDocuments=50;

    public NDCGScorer()
    {
        preprocess();
    }

    public NDCGScorer(int ndcgRelSize, int nDocuments)
    {
        NDCGScorer.ndcgRelSize = ndcgRelSize;
        NDCGScorer.nDocuments=nDocuments;
        preprocess();
    }

    public static void preprocess()
    {
        discount= new double[nDocuments];
        Arrays.fill(discount,0.0);
        gains = new double[10];
        for (int i = 0; i < ndcgRelSize; i++) {
            discount[i] = 1.0 / SimpleMath.logBase2(i + 2);
        }
        for (int i = 0; i < 10; i++) {
            gains[i] = (1 << i) - 1;//2^i-1
        }

    }

    public Double score(RankList rankList)
    {
        if(rankList.targetValues.size()==0)return 0.0;
        int size=rankList.targetValues.size()> ndcgRelSize ? ndcgRelSize :rankList.targetValues.size();
        Double result=0.0;
        for(int i=0;i<size;i++)
        {
           result+=gains[rankList.targetValues.get(i)]*discount[i];
        }
        Double ideal= getIdealDcg(rankList);
        if(Math.abs(ideal-0.0)<0.000001)return 0.0;
        return result/ideal;
    }

    public double getIdealDcg(RankList rankList)
    {
        ArrayIndexComparator comparator= new ArrayIndexComparator(rankList.targetValues);
        Integer [] indices= comparator.createIndexArray();
        Arrays.sort(indices, comparator);
        Double idealDcg=0.0;
        for(int i=0;i< ndcgRelSize;i++)
        {
            idealDcg+=gains[rankList.targetValues.get(indices[i])]*discount[i];
        }
        return idealDcg;
    }

    public ArrayList<ArrayList<Double>> swapChange(RankList rankList)
    {
        ArrayList<ArrayList<Double>> swapScoreDiff= new ArrayList<ArrayList<Double>>();
        Double ideal= getIdealDcg(rankList);
        for(int i=0;i<rankList.targetValues.size();i++)
        {
            ArrayList<Double> posSwapDiff= new ArrayList<Double>();
            for(int j=0;j<rankList.targetValues.size();j++)
            {
                if(Math.abs(ideal-0.0)<0.000001)posSwapDiff.add(0.0);
                else
                {
                    posSwapDiff.add((discount[i]-discount[j])*(gains[rankList.targetValues.get(i)]-gains[rankList.targetValues.get(j)]));
                }
            }
            swapScoreDiff.add(posSwapDiff);
        }
        return swapScoreDiff;
    }


}