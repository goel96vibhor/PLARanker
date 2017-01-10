package FeatureManager;

import Entities.TermVector;
import ProductPreprocess.IDFCalculator;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Created by vibhor.go on 10/17/16.
 */

public class SimilarityCalculator {

    private static Logger logger = Logger.getLogger(SimilarityCalculator.class.getName());
    private static Double k1=2.5;
    private static Double k3=0.0;
    private static Double b=0.8;

    public static Double calculateDocumentTFIDF(TermVector documentTermVec,TermVector queryTermVec, int docType)
    {
        HashMap<String, Double> docTypeIdfs= IDFCalculator.getDocTypeIdfs(docType);
        Double score= 0.0;
        Double termIdf;
        Double termDocTypeCount;
        Integer queryTerms=0;
        Double termScore=0.0;
//        if(docType==2) System.out.println("querynorm docnorm querylength doclength: "+queryTermVec.getNorm()+" "
//                +documentTermVec.getNorm()+" "+queryTermVec.getLength()+" "+documentTermVec.getLength());
        if(documentTermVec.getLength()<0.99)return 0.0;
        for(String term:queryTermVec.getTermFreq().keySet())
        {

            //queryTerms+=queryTermVec.getTermFreq().get(term);
            if(documentTermVec.getTermFreq().containsKey(term)){
                if(docTypeIdfs.containsKey(term))
                {
                    //if(docType==3)System.out.print(term);
                    termDocTypeCount=IDFCalculator.productCount/docTypeIdfs.get(term);
                    //if (docTypeIdfs.get(term)==0)System.out.print("yes");
                    //if(docType==3)System.out.print(docTypeIdfs.get(term));
                    termIdf=(IDFCalculator.productCount)/(termDocTypeCount);
                    //if(docType==3)System.out.println(" "+termIdf);
                    termIdf=Math.log10(termIdf);
                }
                else termIdf=0.0d;
                termScore=(documentTermVec.getTermFreq().get(term));
                termScore*=(queryTermVec.getTermFreq().get(term));
                termScore*=termIdf;
                //if(docType==3)System.out.print(term +" "+termScore+" ");
                score+=termScore;
            }
        }
        //if(docType==3)System.out.println("     "+ score);
        score/=(documentTermVec.getNorm()*queryTermVec.getNorm());
        return score;
    }

    public static Double calculateDocumentBM25(TermVector documentTermVec, TermVector queryTermVec, int docType)
    {
        HashMap<String, Double> docTypeIdfs=IDFCalculator.getDocTypeIdfs(docType);
        Double avgDocTypelength =IDFCalculator.getavgDocTypeLength(docType);
        Double score=0.0;
        Double termIdf=0.0;
        Double termScore=0.0;
        Double termDocTypeCount=0.0;
        if(documentTermVec.getLength()<0.99)return 0.0;
        for(String term:queryTermVec.getTermFreq().keySet())
        {
            if(documentTermVec.getTermFreq().containsKey(term)){
                if(docTypeIdfs.containsKey(term))
                {
                    termDocTypeCount=IDFCalculator.productCount/docTypeIdfs.get(term);
                    termIdf=(IDFCalculator.productCount)/(termDocTypeCount);
                    termIdf=Math.log10(termIdf);
                }
                else termIdf=0.0;
                termScore=(termIdf*documentTermVec.getTermFreq().get(term)*(k1+1.0));
                termScore/=(documentTermVec.getTermFreq().get(term)+k1*(1-b+b*(documentTermVec.getLength()/avgDocTypelength)));
                termScore*=((k3+1.0)*queryTermVec.getTermFreq().get(term))/(k3+queryTermVec.getTermFreq().get(term));
                score+=termScore;

            }
        }
        return score;

    }


}