package Entities;

import ProductPreprocess.IDFCalculator;
import Utils.Util;

import java.util.HashMap;

/**
 * Created by aman on 10/11/16.
 */
public class Features {

    private double titleTFIDF;
    private double attributeTFIDF;
    private double descriptionTFIDF;

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
}
