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
    public static int featureCount=25;

    public FeatureInitializer() {
        String features=ApplicationProperties.getProperty("FEATURES_TO_USE");
        List<String> featureIds= new ArrayList<String>();
        features =features.replaceAll("\\s","");
        if(features!=null&&features!=""&&features.matches(".*[0-9].*"))featureIds= Arrays.asList(features.split("~"));
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
        double count[]= new double[featuresToUse.length];
        Arrays.fill(count,0.0);
        for(List<Double> prodFeature: features)
        {

           for(int i=0;i<featuresToUse.length;i++){
               if(prodFeature.get(i).compareTo(0.0d)!=0)count[i]++;
              norm[i]+= prodFeature.get(i);
           }
        }

//        for(int i=0;i<featuresToUse.length;i++)
//        {
//            if (count[i]!=0d)
//            norm[i]=norm[i]*features.size()/count[i];
//        }
        for(List<Double> prodFeature: features)
        {
            for(int i=0;i<featuresToUse.length;i++)
            {
                Double val=prodFeature.get(i);
//                if (count[i]==0d)
//                {
//                    prodFeature.set(i,0.0d);
//                }
//                else
                if(norm[i]!=0d)
                {
//                    if(val.compareTo(0.0d)==0)prodFeature.set(i,norm[i]/features.size());
                    prodFeature.set(i,val/norm[i]);
                }
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