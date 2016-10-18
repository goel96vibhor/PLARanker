package Utils;

import ProductPreprocess.IDFCalculator;
import ProductPreprocess.WordRetrieval;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vibhor.go on 10/17/16.
 */

public class SimilarityCalculator {

    private static Logger logger = Logger.getLogger(SimilarityCalculator.class.getName());

    public static HashMap<String, Integer> getTermVecfromString(String text)
    {
        List<String> processedTerms= WordRetrieval.retrieveProcessedWords(text);
        HashMap<String,Integer> termVec= new HashMap<String, Integer>();
        for(String term: processedTerms){
            if(termVec.containsKey(term))termVec.put(term,1);
            else termVec.put(term,termVec.get(term)+1);
        }
        return termVec;
    }

    public static Integer getTermVecTermCount(HashMap<String, Integer> termVec)
    {
        int termCount=0;
        for(String term: termVec.keySet())
        {
            termCount+=termVec.get(term);
        }
        return termCount;
    }

    public static Double calculateDocumentTFIDF(HashMap<String, Integer> documentTermVec,HashMap<String,Integer> queryTermVec, int documentTerms)
    {

        Double score= 0.0;
        Double termidf;

        Integer queryTerms=0;
        for(String term:queryTermVec.keySet())
        {

            queryTerms+=queryTermVec.get(term);
            if(documentTermVec.containsKey(term)){
                termidf=Math.log10(((double)IDFCalculator.productCount)/((double)IDFCalculator.titleIDFs.get(term)));
                score+=((double)documentTermVec.get(term)*queryTermVec.get(term))*termidf;
            }
        }
        score/=((double)documentTerms*queryTerms);
        return score;
    }

}