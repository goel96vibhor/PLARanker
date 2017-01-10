package Entities;

import FeatureManager.LMIRCalculator;
import FeatureManager.SimilarityCalculator;
import org.apache.log4j.Logger;


/**
 * Created by aman on 9/23/16.
 */
public class Product {
    private long ad_id;
    private String viewId;
    private String title;
    private String publisherUrl;
    private int adPosition;
    private int categoryId;
    private double netBid;
    private double netTotalRevenue;
    private int clicked;
    private Double price;
    private Double originalPrice;
    private String description;
    private String brand;
    private String attributes;
    private Double sellerRating;
    private boolean popularBrand;
    private Double expectedRPM;
    private Features features;
    private TermVector titleTermVec;
    private TermVector attributeTermVec;
    private TermVector descriptionTermVec;
    private TermVector wholeDocTermVec;
    private static Logger logger = Logger.getLogger(Product.class.getName());



    public TermVector getTitleTermVec() {
        return titleTermVec;
    }

    public TermVector getAttributeTermVec() {
        return attributeTermVec;
    }

    public TermVector getDescriptionTermVec() {
        return descriptionTermVec;
    }

    public TermVector getWholeDocTermVec() {
        return wholeDocTermVec;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisherUrl() {
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    public int getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(int adPosition) {
        this.adPosition = adPosition;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getNetBid() {
        return netBid;
    }

    public void setNetBid(double netBid) {
        this.netBid = netBid;
    }

    public double getNetTotalRevenue() {
        return netTotalRevenue;
    }

    public void setNetTotalRevenue(double netTotalRevenue) {
        this.netTotalRevenue = netTotalRevenue;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public double getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(double sellerRating) {
        this.sellerRating = sellerRating;
    }

    public boolean isPopularBrand() {
        return popularBrand;
    }

    public void setPopularBrand(boolean popularBrand) {
        this.popularBrand = popularBrand;
    }

    public double getExpectedRPM() {
        return expectedRPM;
    }

    public void setExpectedRPM(double expectedRPM) {
        this.expectedRPM = expectedRPM;
    }

    public long getAd_id() {
        return ad_id;
    }

    public void setAd_id(long ad_id) {
        this.ad_id = ad_id;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public void calculateProductTermVecs()
    {
        titleTermVec= new TermVector(title);
//        System.out.println(title);
//        System.out.println(titleTermVec.getTermFreq().toString());
        attributeTermVec= new TermVector(attributes);
        descriptionTermVec= new TermVector(description);
        StringBuilder wholeString= new StringBuilder(title);
        wholeString.append(" ");
        wholeString.append(attributes);
        wholeString.append(" ");
        wholeString.append(description);
        wholeDocTermVec= new TermVector(wholeString.toString());
    }

    public void calculateFeatures(String query)
    {
        TermVector queryTermVec= new TermVector(query);
        calculateProductTermVecs();
        features=new Features();
        calculateTitleFeatures(queryTermVec);
        calculateAttributeFeatures(queryTermVec);
        calculateDescriptionFeatures(queryTermVec);
        calculateWholeDocFeatures(queryTermVec);
        features.setNetBid(netBid);
        features.setPrice(price);
        features.setDiscount((originalPrice-price)/price);
        features.setSellerRating(sellerRating);
        if(popularBrand)features.setPopularBrand(1.0);
        else features.setPopularBrand(0.0);
        features.setExpectedRPM(expectedRPM);

    }

    public void calculateTitleFeatures(TermVector queryTermVec)
    {
        features.setTitleTFIDF(SimilarityCalculator.calculateDocumentTFIDF(titleTermVec, queryTermVec, 0));
        features.setTitleBM25(SimilarityCalculator.calculateDocumentBM25(titleTermVec,queryTermVec,0));
        features.setTitleLMIR_JM(LMIRCalculator.calculateLMIR_JM(titleTermVec,queryTermVec,0));
        features.setTitleLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(titleTermVec,queryTermVec,0));
        features.setTitleLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(titleTermVec,queryTermVec,0));
    }

    public void calculateAttributeFeatures(TermVector queryTermVec)
    {
        features.setAttributeTFIDF(SimilarityCalculator.calculateDocumentTFIDF(attributeTermVec, queryTermVec, 1));
        features.setAttributeBM25(SimilarityCalculator.calculateDocumentBM25(attributeTermVec,queryTermVec,1));
        features.setAttributeLMIR_JM(LMIRCalculator.calculateLMIR_JM(attributeTermVec,queryTermVec,1));
        features.setAttributeLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(attributeTermVec,queryTermVec,1));
        features.setAttributeLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(attributeTermVec,queryTermVec,1));
    }

    public void calculateDescriptionFeatures(TermVector queryTermVec)
    {
        features.setDescriptionTFIDF(SimilarityCalculator.calculateDocumentTFIDF(descriptionTermVec, queryTermVec, 2));
        features.setDescriptionBM25(SimilarityCalculator.calculateDocumentBM25(descriptionTermVec,queryTermVec,2));
        features.setDescriptionLMIR_JM(LMIRCalculator.calculateLMIR_JM(descriptionTermVec,queryTermVec,2));
        features.setDescriptionLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(descriptionTermVec,queryTermVec,2));
        features.setDescriptionLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(descriptionTermVec,queryTermVec,2));
    }

    public void calculateWholeDocFeatures(TermVector queryTermVec)
    {
        features.setWholeDocTFIDF(SimilarityCalculator.calculateDocumentTFIDF(wholeDocTermVec, queryTermVec, 3));
        features.setWholeDocBM25(SimilarityCalculator.calculateDocumentBM25(wholeDocTermVec,queryTermVec,3));
        features.setWholeDocLMIR_JM(LMIRCalculator.calculateLMIR_JM(wholeDocTermVec,queryTermVec,3));
        features.setWholeDocLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(wholeDocTermVec,queryTermVec,3));
        features.setWholeDocLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(wholeDocTermVec,queryTermVec,3));
    }

}
