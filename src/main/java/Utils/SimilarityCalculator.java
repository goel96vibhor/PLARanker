package Utils;

import Entities.TermVector;
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

    public static Double calculateDocumentTFIDF(TermVector documentTermVec,TermVector queryTermVec, int docType)
    {
        HashMap<String, Double> docTypeIdfs= null;
        if(docType==0)docTypeIdfs= IDFCalculator.titleIDFs;
        else if (docType==1)docTypeIdfs=IDFCalculator.attributeIDFs;
        else docTypeIdfs=IDFCalculator.descriptionIDFs;
        Double score= 0.0;
        Double termidf;

        Integer queryTerms=0;
        for(String term:queryTermVec.getTermFreq().keySet())
        {

            queryTerms+=queryTermVec.getTermFreq().get(term);
            if(documentTermVec.getTermFreq().containsKey(term)){
                if (docTypeIdfs.containsKey(term))
                    termidf=Math.log10(docTypeIdfs.get(term));
                else termidf=0.0d;
                score+=(documentTermVec.getTermFreq().get(term)*queryTermVec.getTermFreq().get(term))*termidf;
            }
        }
        score/=(documentTermVec.getNorm()*queryTermVec.getNorm());
        return score;
    }

}