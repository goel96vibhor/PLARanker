package Utils;

import Entities.RankList;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibhor.go on 11/11/16.
 */

public class DataReader{

    private static Logger logger = Logger.getLogger(DataReader.class.getName());
    private static int maxfeatureCount=0;

     public static void readSamples(String inputFile)
     {
         List<RankList> samples = new ArrayList<RankList>();
         ArrayList<Integer> targetValue= new ArrayList<Integer>();
         ArrayList<ArrayList<Double>> featureList= new ArrayList<ArrayList<Double>>();
         //float [] features;
         int countEntries=0;
         try {
             String content = "";
             BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "ASCII"));

             String lastID = "";
             while((content = in.readLine()) != null)
             {
                 content = content.trim();
                 if(content.length() == 0)
                     continue;
                 String [] pairs=content.split("\\s+");
                 //lastID=getValue(pairs[1]);
                 if(lastID.compareTo("")!=0 && lastID.compareTo(getValue(pairs[1]))!=0)
                 {
                     samples.add(new RankList(targetValue,featureList));
                     targetValue= new ArrayList<Integer>();
                     featureList= new ArrayList<ArrayList<Double>>();
                     countEntries++;
                     if(countEntries%100==0)logger.info("reading qid "+getValue(pairs[1]));
                 }
                 targetValue.add(Integer.parseInt(pairs[0]));

                 featureList.add(parseContent(pairs));

             }
             samples.add(new RankList(targetValue,featureList));
         }
         catch (Exception ex)
         {
             logger.error("error in reading samples from file: "+inputFile);
         }
     }



     public static String getValue(String pair)
     {
         return pair.substring(pair.lastIndexOf(":")+1);
     }

     protected static String getKey(String pair)
     {
         return pair.substring(0, pair.indexOf(":"));
     }

     public static ArrayList<Double> parseContent(String [] pairs)
     {
         if(maxfeatureCount<pairs.length-2)maxfeatureCount=pairs.length;
         //float [] features= new float[maxfeatureCount];
         ArrayList<Double> features= new ArrayList<Double>(maxfeatureCount);
         for(int i=2;i<pairs.length;i++)
         {
             features.set(Integer.parseInt(getKey(pairs[i]))-1,Double.parseDouble(getValue(pairs[i])));
         }
         return features;

     }
}