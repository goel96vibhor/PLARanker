package Utils;

import Entities.RankList;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibhor.go on 11/11/16.
 */

public class DataReader{

    private static Logger logger = Logger.getLogger(DataReader.class.getName());
    private static int maxfeatureCount=0;

     public static ArrayList<RankList> readSamples(String inputFile)
     {
         ArrayList<RankList> samples=null;
         ArrayList<Integer> targetValue= new ArrayList<Integer>();
         ArrayList<ArrayList<Double>> featureList= new ArrayList<ArrayList<Double>>();

         int countEntries=0;
         try {
             samples= new ArrayList<RankList>();
             String content = "";
             BufferedReader in = new BufferedReader(new FileReader(inputFile));

             String lastID = "";
             while((content = in.readLine()) != null)
             {

                 content = content.trim();
                 if(content.length() == 0)
                     continue;
                 int idx = content.indexOf("#");
                 if(idx != -1)
                 {

                     content = content.substring(0, idx).trim();//remove the comment part at the end of the line
                 }
                 String [] pairs=content.split("\\s+");

                 if(!(lastID.equalsIgnoreCase("") ||lastID.equalsIgnoreCase(getValue(pairs[1]))))
                 {
                     countEntries++;
                     samples.add(new RankList(targetValue,featureList,lastID));
                     targetValue= new ArrayList<Integer>();
                     featureList= new ArrayList<ArrayList<Double>>();


                     logger.info("read qid "+lastID);
                 }
                 lastID=getValue(pairs[1]);
                 targetValue.add(Integer.parseInt(pairs[0]));

                 featureList.add(parseContent(pairs));

             }
             logger.info("read qid "+lastID);
             samples.add(new RankList(targetValue,featureList,lastID));
         }
         catch (Exception ex)
         {
             logger.error("error in reading samples from file: "+inputFile);
             ex.printStackTrace();
         }
         finally {
             return samples;
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
         if(maxfeatureCount<pairs.length-2)maxfeatureCount=pairs.length-2;
         ArrayList<Double> features= new ArrayList<Double>(maxfeatureCount);
         for(int i=2;i<pairs.length;i++)
         {
             features.add(Integer.parseInt(getKey(pairs[i])) - 1, Double.parseDouble(getValue(pairs[i])));
         }
         return features;

     }
}