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
    protected String viewid=null;

    public RankList(){}

    public RankList(List<Product> ads, String viewid)
    {
        this.viewid=viewid;
        this.ads=ads;
        for(Product product:ads)listFeatures.add(featureManager.getFeatureArray(product));
        for(Product product:ads)targetValues.add(product.getClicked());
        featureManager.normalize(listFeatures);

    }

    public RankList(RankList rankList, int[] sortedInd)
    {

        this.viewid=rankList.getViewid();
        for(int i=0;i<rankList.listFeatures.size();i++)
        {
            if(rankList.ads.size()!=0)this.ads.add(rankList.ads.get(sortedInd[i]));
            this.listFeatures.add(rankList.listFeatures.get(sortedInd[i]));
            this.targetValues.add(rankList.targetValues.get(sortedInd[i]));
        }

    }

    public RankList(ArrayList<Integer> targetValues,ArrayList<ArrayList<Double>> listFeatures, String viewid)
    {
        this.listFeatures=listFeatures;
        this.targetValues=targetValues;
        this.viewid=viewid;
    }

    public String getViewid() {
        return viewid;
    }

    public void setViewid(String viewid) {
        this.viewid = viewid;
    }

}