package Entities;

import ProductPreprocess.IDFCalculator;
import Utils.Util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aman on 10/11/16.
 */
public class Features{

    private Double titleTFIDF;
    private Double attributeTFIDF;
    private Double descriptionTFIDF;
    private Double wholeDocTFIDF;
    private Double titleBM25;
    private Double attributeBM25;
    private Double descriptionBM25;
    private Double wholeDocBM25;
    private Double titleLMIR_JM;
    private Double attributeLMIR_JM;
    private Double descriptionLMIR_JM;
    private Double wholeDocLMIR_JM;
    private Double titleLMIR_DIR;
    private Double attributeLMIR_DIR;
    private Double descriptionLMIR_DIR;
    private Double wholeDocLMIR_DIR;
    private Double titleLMIR_ABS;
    private Double attributeLMIR_ABS;
    private Double descriptionLMIR_ABS;
    private Double wholeDocLMIR_ABS;
    private Double netBid;
    private Double price;
    private Double discount;
    private Double sellerRating;
    private Double popularBrand;
    private Double expectedRPM;
    private List<Double> featureList;

    public void setTitleTFIDF(Double titleTFIDF) {
        this.titleTFIDF = titleTFIDF;
    }

    public void setAttributeTFIDF(Double attributeTFIDF) {
        this.attributeTFIDF = attributeTFIDF;
    }

    public void setDescriptionTFIDF(Double descriptionTFIDF) {
        this.descriptionTFIDF = descriptionTFIDF;
    }

    public void setWholeDocTFIDF(Double wholeDocTFIDF) {
        this.wholeDocTFIDF = wholeDocTFIDF;
    }

    public void setTitleBM25(Double titleBM25) {
        this.titleBM25 = titleBM25;
    }

    public void setAttributeBM25(Double attributeBM25) {
        this.attributeBM25 = attributeBM25;
    }

    public void setDescriptionBM25(Double descriptionBM25) {
        this.descriptionBM25 = descriptionBM25;
    }

    public void setWholeDocBM25(Double wholeDocBM25) {
        this.wholeDocBM25 = wholeDocBM25;
    }

    public void setTitleLMIR_JM(Double titleLMIR_JM) {
        this.titleLMIR_JM = titleLMIR_JM;
    }

    public void setAttributeLMIR_JM(Double attributeLMIR_JM) {
        this.attributeLMIR_JM = attributeLMIR_JM;
    }

    public void setDescriptionLMIR_JM(Double descriptionLMIR_JM) {
        this.descriptionLMIR_JM = descriptionLMIR_JM;
    }

    public void setWholeDocLMIR_JM(Double wholeDocLMIR_JM) {
        this.wholeDocLMIR_JM = wholeDocLMIR_JM;
    }

    public void setTitleLMIR_DIR(Double titleLMIR_DIR) {
        this.titleLMIR_DIR = titleLMIR_DIR;
    }

    public void setAttributeLMIR_DIR(Double attributeLMIR_DIR) {
        this.attributeLMIR_DIR = attributeLMIR_DIR;
    }

    public void setDescriptionLMIR_DIR(Double descriptionLMIR_DIR) {
        this.descriptionLMIR_DIR = descriptionLMIR_DIR;
    }

    public void setWholeDocLMIR_DIR(Double wholeDocLMIR_DIR) {
        this.wholeDocLMIR_DIR = wholeDocLMIR_DIR;
    }

    public void setTitleLMIR_ABS(Double titleLMIR_ABS) {
        this.titleLMIR_ABS = titleLMIR_ABS;
    }

    public void setAttributeLMIR_ABS(Double attributeLMIR_ABS) {
        this.attributeLMIR_ABS = attributeLMIR_ABS;
    }

    public void setDescriptionLMIR_ABS(Double descriptionLMIR_ABS) {
        this.descriptionLMIR_ABS = descriptionLMIR_ABS;
    }

    public void setWholeDocLMIR_ABS(Double wholeDocLMIR_ABS) {
        this.wholeDocLMIR_ABS = wholeDocLMIR_ABS;
    }

    public void setNetBid(Double netBid) {
        this.netBid = netBid;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setSellerRating(Double sellerRating) {
        this.sellerRating = sellerRating;
    }

    public void setPopularBrand(Double popularBrand) {
        this.popularBrand = popularBrand;
    }

    public void setExpectedRPM(Double expectedRPM) {
        this.expectedRPM = expectedRPM;
    }

    public List<Double> getFeatureList()
    {
        featureList= new ArrayList<Double>();
        featureList.add(titleTFIDF);
        featureList.add(attributeTFIDF);
        featureList.add(descriptionTFIDF);
        featureList.add(wholeDocTFIDF);

        featureList.add(titleBM25);
        featureList.add(attributeBM25);
        featureList.add(descriptionBM25);
        featureList.add(wholeDocBM25);

        featureList.add(titleLMIR_JM);
        featureList.add(attributeLMIR_JM);
        featureList.add(descriptionLMIR_JM);
        featureList.add(wholeDocLMIR_JM);

        featureList.add(titleLMIR_DIR);
        featureList.add(attributeLMIR_DIR);
        featureList.add(descriptionLMIR_DIR);
        featureList.add(wholeDocLMIR_DIR);

        featureList.add(titleLMIR_ABS);
        featureList.add(attributeLMIR_ABS);
        featureList.add(descriptionLMIR_ABS);
        featureList.add(wholeDocLMIR_ABS);

        featureList.add(netBid);
        featureList.add(price);
        featureList.add(discount);
        featureList.add(sellerRating);
        //featureList.add(popularBrand);
        featureList.add(expectedRPM);

        return featureList;
    }

}
