package Entities;

import ProductPreprocess.WordRetrieval;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vibhor.go on 11/23/16.
*/

public class TermVector
{
    private HashMap<String, Double> termFreq=null;
    private Double norm;
    private Double length;

    public TermVector(String text)
    {
        List<String> processedTerms= WordRetrieval.retrieveProcessedWords(text);
        termFreq= new HashMap<String, Double>();
        for(String term: processedTerms){
            if(!termFreq.containsKey(term)){
                termFreq.put(term,1.0);
            }
            else termFreq.put(term,termFreq.get(term)+1.0);
        }
        length=processedTerms.size()+0.0;
        norm=0.0;
        for (String term:termFreq.keySet())
        {
            termFreq.put(term, termFreq.get(term)/length);
            norm+=Math.pow(termFreq.get(term),2);
        }
        norm=Math.sqrt(norm);

    }

    public HashMap<String, Double> getTermFreq() {
        return termFreq;
    }

    public void setTermFreq(HashMap<String, Double> termFreq) {
        this.termFreq = termFreq;
    }

    public Double getNorm() {
        return norm;
    }

    public void setNorm(Double norm) {
        this.norm = norm;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}