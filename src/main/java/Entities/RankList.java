package Entities;

import Utils.FeatureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibhor.go on 11/11/16.
 */

public class RankList {

    public List<Product> ads=new ArrayList<Product>();
    public ArrayList<ArrayList<Double>> listFeatures= new ArrayList<ArrayList<Double>>();
    public ArrayList<Integer> targetValues=new ArrayList<Integer>();
    private static FeatureManager featureManager= new FeatureManager();

    public RankList(){}

    public RankList(List<Product> ads)
    {
        this.ads=ads;
        for(Product product:ads)listFeatures.add(featureManager.getFeatureArray(product));
        for(Product product:ads)targetValues.add(product.getClicked());
        featureManager.normalize(listFeatures);
    }

    public RankList(RankList rankList, int[] sortedInd)
    {
        RankList rl= new RankList();
        for(int i=0;i<rankList.listFeatures.size();i++)
        {
            if(ads!=null)rl.ads.add(rankList.ads.get(sortedInd[i]));
            rl.listFeatures.add(rankList.listFeatures.get(sortedInd[i]));
            rl.targetValues.add(rankList.targetValues.get(sortedInd[i]));
        }

    }

    public RankList(ArrayList<Integer> targetValues,ArrayList<ArrayList<Double>> listFeatures)
    {
        this.listFeatures=listFeatures;
        this.targetValues=targetValues;
    }

}