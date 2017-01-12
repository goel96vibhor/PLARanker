package FeatureManager;

import Entities.TermVector;
import ProductPreprocess.IDFCalculator;

import java.util.HashMap;

/**
 * Created by vibhor.go on 10/17/16.
 */


public class LMIRCalculator
{
    private static Double lambda_short=0.1;
    private static Double lambda_long=0.7;
    private static Double mu=2000.0;
    private static Double delta=0.7;

    public static Double calculateLMIR_JM(TermVector documentTermVec, TermVector queryTermVec, int docType)
    {
        HashMap<String, Double> docTypeIdfs= IDFCalculator.getDocTypeIdfs(docType);
        Double avgDocTypelength =IDFCalculator.getavgDocTypeLength(docType);
        Double score=1.0;
        Double pTermDoc, pTermCollec,docTermCount, collTermCount;
        Double lambda;
        if(queryTermVec.getLength()<6.0)lambda=lambda_short;
        else lambda=lambda_long;
        if(documentTermVec.getLength()<0.99)return 0.0;
        for(String term:queryTermVec.getTermFreq().keySet())
        {
            if(docTypeIdfs.containsKey(term)&&docTypeIdfs.get(term)>0.00001)
                collTermCount=IDFCalculator.getDocumentCount()/docTypeIdfs.get(term);
            else
                collTermCount=IDFCalculator.getDocumentCount()*avgDocTypelength/docTypeIdfs.size();
            if(documentTermVec.getTermFreq().containsKey(term))
            {
                pTermDoc= documentTermVec.getTermFreq().get(term)/documentTermVec.getLength();
            }
            else pTermDoc=0.0;
            pTermCollec=collTermCount/(IDFCalculator.getDocumentCount()*avgDocTypelength);
            score*=Math.pow((1-lambda)*(pTermDoc)+(lambda)*pTermCollec,queryTermVec.getTermFreq().get(term));

        }
        return score;
    }

    public static Double calculateLMIR_DIR(TermVector documentTermVec, TermVector queryTermVec, int docType)
    {
        HashMap<String, Double> docTypeIdfs= IDFCalculator.getDocTypeIdfs(docType);
        Double avgDocTypelength =IDFCalculator.getavgDocTypeLength(docType);
        Double score=1.0;
        Double pTermColl, docTermCount,termScore;
        if(documentTermVec.getLength()<0.99)return 0.0;
        for(String term:queryTermVec.getTermFreq().keySet())
        {
            if(docTypeIdfs.containsKey(term)&&docTypeIdfs.get(term)>0.00001)
                pTermColl=1.0/(docTypeIdfs.get(term)*avgDocTypelength);
            else pTermColl=1.0/docTypeIdfs.size();
            if (documentTermVec.getTermFreq().containsKey(term))
                docTermCount=documentTermVec.getTermFreq().get(term);
            else docTermCount=0.0;
            termScore= (docTermCount+mu*pTermColl)/(documentTermVec.getLength()+mu);
            score*=Math.pow(termScore,queryTermVec.getTermFreq().get(term));
        }
        return score;
    }

    public static Double calculateLMIR_ABS(TermVector documentTermVec, TermVector queryTermVec, int docType)
    {
        HashMap<String, Double> docTypeIdfs= IDFCalculator.getDocTypeIdfs(docType);
        Double avgDocTypelength =IDFCalculator.getavgDocTypeLength(docType);
        Double score=1.0;
        Double pTermColl, docTermCount,termScore;
        if(documentTermVec.getLength()<0.99)return 0.0;
        for(String term:queryTermVec.getTermFreq().keySet())
        {

            if(docTypeIdfs.containsKey(term)&&docTypeIdfs.get(term)>0.00001)
                pTermColl=1.0/(docTypeIdfs.get(term)*avgDocTypelength);
            else pTermColl=1.0/docTypeIdfs.size();
            if (documentTermVec.getTermFreq().containsKey(term))
                docTermCount=documentTermVec.getTermFreq().get(term);
            else docTermCount=0.0;
            termScore=(docTermCount>delta?docTermCount-delta:0)/documentTermVec.getLength();
            termScore+=delta*documentTermVec.getTermFreq().size()*pTermColl/(documentTermVec.getLength());
            score*=Math.pow(termScore,queryTermVec.getTermFreq().get(term));

        }
        return score;
    }

}