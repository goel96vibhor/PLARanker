package NeuralNet;

import Entities.RankList;

import java.util.ArrayList;

/**
 * Created by vibhor.go on 11/17/16.
 */

public interface Scorer
{
    public Double score(RankList rankList);
    public ArrayList<ArrayList<Double>> swapChange(RankList rankList);
    public double getIdealScore(RankList rankList);
}