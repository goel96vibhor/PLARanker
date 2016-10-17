package Utils;

import ProductPreprocess.IDFCalculator;
import ProductPreprocess.WordRetrieval;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by vibhor.go on 10/17/16.
 */

public class TFIDFCalculator{

    public static HashMap<String, Integer> getTermVecfromString(String text)
    {
        List<String> processedTerms= WordRetrieval.retrieveProcessedWords(text);
        HashMap<String,Integer> termVec= new HashMap<String, Integer>();
        Set<String> allWords= IDFCalculator.titleIDFs.keySet();
        for(String word:allWords)termVec.put(word,0);
        for(String term: processedTerms){
            termVec.put(term,termVec.get(term)+1);
        }
        return termVec;
    }

    public static Double calculateDocumentTFIDF(HashMap<String, Integer> documentTermVec,HashMap<String,Integer> queryTermVec)
    {

        Double score= 0.0;
        Double termidf;
        Integer documentTerms=0;
        Integer queryTerms=0;
        for(String term:documentTermVec.keySet())
        {
            documentTerms+=documentTermVec.get(term);
            queryTerms+=queryTermVec.get(term);
            termidf=Math.log10(((double)IDFCalculator.productCount)/((double)IDFCalculator.titleIDFs.get(term)));
            score+=((double)documentTermVec.get(term)*queryTermVec.get(term))*termidf;
        }
        score/=((double)documentTerms*queryTerms);
        return score;
    }

}