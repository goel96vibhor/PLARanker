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

    private Double title_titleTFIDF;
    private Double title_contentTFIDF;
    private Double content_titleTFIDF;
    private Double content_contentTFIDF;
    private Double title_titleBM25;
    private Double title_contentBM25;
    private Double content_titleBM25;
    private Double content_contentBM25;
    private Double title_titleLMIR_JM;
    private Double title_contentLMIR_JM;
    private Double content_titleLMIR_JM;
    private Double content_contentLMIR_JM;
    private Double title_titleLMIR_DIR;
    private Double title_contentLMIR_DIR;
    private Double content_titleLMIR_DIR;
    private Double content_contentLMIR_DIR;
    private Double title_titleLMIR_ABS;
    private Double title_contentLMIR_ABS;
    private Double content_titleLMIR_ABS;
    private Double content_contentLMIR_ABS;
    private Double netBid;
    private Double price;
    private Double discount;
    private Double sellerRating;
    private Double popularBrand;
    private Double expectedRPM;
    private List<Double> featureList;

    public void setTitle_titleTFIDF(Double title_titleTFIDF) {
        this.title_titleTFIDF = title_titleTFIDF;
    }

    public void setTitle_contentTFIDF(Double title_contentTFIDF) {
        this.title_contentTFIDF = title_contentTFIDF;
    }

    public void setContent_titleTFIDF(Double content_titleTFIDF) {
        this.content_titleTFIDF = content_titleTFIDF;
    }

    public void setContent_contentTFIDF(Double content_contentTFIDF) {
        this.content_contentTFIDF = content_contentTFIDF;
    }

    public void setTitle_titleBM25(Double title_titleBM25) {
        this.title_titleBM25 = title_titleBM25;
    }

    public void setTitle_contentBM25(Double title_contentBM25) {
        this.title_contentBM25 = title_contentBM25;
    }

    public void setContent_titleBM25(Double content_titleBM25) {
        this.content_titleBM25 = content_titleBM25;
    }

    public void setContent_contentBM25(Double content_contentBM25) {
        this.content_contentBM25 = content_contentBM25;
    }

    public void setTitle_titleLMIR_JM(Double title_titleLMIR_JM) {
        this.title_titleLMIR_JM = title_titleLMIR_JM;
    }

    public void setTitle_contentLMIR_JM(Double title_contentLMIR_JM) {
        this.title_contentLMIR_JM = title_contentLMIR_JM;
    }

    public void setContent_titleLMIR_JM(Double content_titleLMIR_JM) {
        this.content_titleLMIR_JM = content_titleLMIR_JM;
    }

    public void setContent_contentLMIR_JM(Double content_contentLMIR_JM) {
        this.content_contentLMIR_JM = content_contentLMIR_JM;
    }

    public void setTitle_titleLMIR_DIR(Double title_titleLMIR_DIR) {
        this.title_titleLMIR_DIR = title_titleLMIR_DIR;
    }

    public void setTitle_contentLMIR_DIR(Double title_contentLMIR_DIR) {
        this.title_contentLMIR_DIR = title_contentLMIR_DIR;
    }

    public void setContent_titleLMIR_DIR(Double content_titleLMIR_DIR) {
        this.content_titleLMIR_DIR = content_titleLMIR_DIR;
    }

    public void setContent_contentLMIR_DIR(Double content_contentLMIR_DIR) {
        this.content_contentLMIR_DIR = content_contentLMIR_DIR;
    }

    public void setTitle_titleLMIR_ABS(Double title_titleLMIR_ABS) {
        this.title_titleLMIR_ABS = title_titleLMIR_ABS;
    }

    public void setTitle_contentLMIR_ABS(Double title_contentLMIR_ABS) {
        this.title_contentLMIR_ABS = title_contentLMIR_ABS;
    }

    public void setContent_titleLMIR_ABS(Double content_titleLMIR_ABS) {
        this.content_titleLMIR_ABS = content_titleLMIR_ABS;
    }

    public void setContent_contentLMIR_ABS(Double content_contentLMIR_ABS) {
        this.content_contentLMIR_ABS = content_contentLMIR_ABS;
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
        featureList.add(title_titleTFIDF);
        featureList.add(title_contentTFIDF);
        featureList.add(content_titleTFIDF);
        featureList.add(content_contentTFIDF);

        featureList.add(title_titleBM25);
        featureList.add(title_contentBM25);
        featureList.add(content_titleBM25);
        featureList.add(content_contentBM25);

        featureList.add(title_titleLMIR_JM);
        featureList.add(title_contentLMIR_JM);
        featureList.add(content_titleLMIR_JM);
        featureList.add(content_contentLMIR_JM);

        featureList.add(title_titleLMIR_DIR);
        featureList.add(title_contentLMIR_DIR);
        featureList.add(content_titleLMIR_DIR);
        featureList.add(content_contentLMIR_DIR);

        featureList.add(title_titleLMIR_ABS);
        featureList.add(title_contentLMIR_ABS);
        featureList.add(content_titleLMIR_ABS);
        featureList.add(content_contentLMIR_ABS);

        featureList.add(netBid);
        featureList.add(price);
        featureList.add(discount);
        featureList.add(sellerRating);
        //featureList.add(popularBrand);
        featureList.add(expectedRPM);

        return featureList;
    }

}
