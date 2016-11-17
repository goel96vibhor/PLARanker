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

    private double titleTFIDF;
    private double attributeTFIDF;
    private double descriptionTFIDF;
    private List<Double> featureList;

    public double getTitleTFIDF() {
        return titleTFIDF;
    }

    public void setTitleTFIDF(double titleTFIDF) {
        this.titleTFIDF = titleTFIDF;
    }

    public double getAttributeTFIDF() {
        return attributeTFIDF;
    }

    public void setAttributeTFIDF(double attributeTFIDF) {
        this.attributeTFIDF = attributeTFIDF;
    }

    public double getDescriptionTFIDF() {
        return descriptionTFIDF;
    }

    public void setDescriptionTFIDF(double descriptionTFIDF) {
        this.descriptionTFIDF = descriptionTFIDF;
    }

    public List<Double> getFeatureList()
    {
        featureList= new ArrayList<Double>();
        featureList.add(titleTFIDF);
        featureList.add(attributeTFIDF);
        featureList.add(descriptionTFIDF);
        return featureList;
    }

}
