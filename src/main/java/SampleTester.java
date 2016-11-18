import Entities.RankList;
import NeuralNet.LambdaRank;
import NeuralNet.NDCGScorer;
import NeuralNet.RankNet;
import Utils.DataReader;

import java.util.ArrayList;

/**
 * Created by aman on 9/27/16.
 */

public class SampleTester
{


    public static void main(String args[])
    {

        ArrayList<RankList> trainSamples= DataReader.readSamples("MQ2008/Fold1/train.txt");
        System.out.println("read "+trainSamples.size()+" queries from training data");
        ArrayList<RankList> validationSamples= DataReader.readSamples("MQ2008/Fold1/vali.txt");
        System.out.println("read "+validationSamples.size()+" queries from validation data");
        ArrayList<RankList> testSamples= DataReader.readSamples("MQ2008/Fold1/train.txt");
        System.out.println("read "+testSamples.size()+" queries from test data");

        ArrayList<RankList> trainTest= new ArrayList<RankList>();
        //trainTest.add(trainSamples.get(1));
        trainTest.add(trainSamples.get(3));
        trainTest.add(trainSamples.get(5));

        RankNet rankNet= new RankNet(trainSamples,validationSamples,trainSamples.get(0).listFeatures.get(0).size(),1);
        rankNet.setMrrScorer(new NDCGScorer());
        rankNet.initialize();
        rankNet.train();
//        NDCGScorer ndcgScorer= new NDCGScorer();
//        RankList predictedRankList= rankNet.rankedProducts(trainSamples.get(3));
//        System.out.println(ndcgScorer.score(predictedRankList));
//        System.out.println(ndcgScorer.getIdealScore(predictedRankList));
    }


}