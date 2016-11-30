package FeatureManager;
import Entities.Product;
import Utils.ApplicationProperties;
import Entities.Features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Entities.RankList;
/**
 * Created by vibhor.go on 11/11/16.
 */

public class FeatureInitializer {

    public static int featuresToUse[];
    public static int featureCount=26;

    public FeatureInitializer() {
        String features=ApplicationProperties.getProperty("FEATURES_TO_USE");
        List<String> featureIds= new ArrayList<String>();
        if(features!=null&&features!="")featureIds= Arrays.asList(features.split("~"));
        if(featureIds.size()>0)
        {
            int i=0;
            featuresToUse= new int[featureIds.size()];
            for(String featureId: featureIds )featuresToUse[i++]=Integer.parseInt(featureId);
        }
        else
        {
            featuresToUse=new int[featureCount];
            for(int i=0;i<featureCount;i++)featuresToUse[i]=i;

        }
    }

    public void normalize(ArrayList< ArrayList<Double> > features)
    {
        double norm[]= new double[featuresToUse.length];
        Arrays.fill(norm,0.0);
        for(List<Double> prodFeature: features)
        {
           for(int i=0;i<featuresToUse.length;i++){
              norm[i]+= prodFeature.get(i);
           }
        }
        for(List<Double> prodFeature: features)
        {
            for(int i=0;i<featuresToUse.length;i++)
            {
                Double val=prodFeature.get(i);
                prodFeature.set(i,val/norm[i]);
            }
        }
    }

    public ArrayList<Double> getFeatureArray(Product product)
    {
       ArrayList<Double> productFeatures= new ArrayList<Double>();
       for(int i=0;i<featuresToUse.length;i++)
       {
           productFeatures.add(product.getFeatures().getFeatureList().get(featuresToUse[i]));
       }
       return productFeatures;
    }



}