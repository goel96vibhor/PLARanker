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
    private TermVector wholeDocTermVec;
    private static Logger logger = Logger.getLogger(Product.class.getName());



    public TermVector getTitleTermVec() {
        return titleTermVec;
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
        StringBuilder wholeString= new StringBuilder(title);
        wholeString.append(" ");
        wholeString.append(attributes);
        wholeString.append(" ");
        wholeString.append(description);
        wholeDocTermVec= new TermVector(wholeString.toString());
    }

    public void calculateFeatures(URLBean urlBean)
    {
        calculateProductTermVecs();
        features=new Features();
        calculateTitle_titleFeatures(urlBean.getTitleTermVec());
        calculateTitle_contentFeatures(urlBean.getContenTermVec());
        calculateContent_titleFeatures(urlBean.getTitleTermVec());
        calculateContent_contentFeatures(urlBean.getContenTermVec());
        features.setNetBid(netBid);
        features.setPrice(price);
        features.setDiscount((originalPrice-price)/price);
        features.setSellerRating(sellerRating);
        if(popularBrand)features.setPopularBrand(1.0);
        else features.setPopularBrand(0.0);
        features.setExpectedRPM(expectedRPM);

    }

    public void calculateTitle_titleFeatures(TermVector urlTitleTermVec)
    {
        features.setTitle_titleTFIDF(SimilarityCalculator.calculateDocumentTFIDF(titleTermVec, urlTitleTermVec, 0));
        features.setTitle_titleBM25(SimilarityCalculator.calculateDocumentBM25(titleTermVec,urlTitleTermVec,0));
        features.setTitle_titleLMIR_JM(LMIRCalculator.calculateLMIR_JM(titleTermVec,urlTitleTermVec,0));
        features.setTitle_titleLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(titleTermVec,urlTitleTermVec,0));
        features.setTitle_titleLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(titleTermVec,urlTitleTermVec,0));
    }

    public void calculateTitle_contentFeatures(TermVector urlContentTermVec)
    {
        features.setTitle_contentTFIDF(SimilarityCalculator.calculateDocumentTFIDF(titleTermVec, urlContentTermVec, 1));
        features.setTitle_contentBM25(SimilarityCalculator.calculateDocumentBM25(titleTermVec,urlContentTermVec,1));
        features.setTitle_contentLMIR_JM(LMIRCalculator.calculateLMIR_JM(titleTermVec,urlContentTermVec,1));
        features.setTitle_contentLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(titleTermVec,urlContentTermVec,1));
        features.setTitle_contentLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(titleTermVec,urlContentTermVec,1));
    }

    public void calculateContent_titleFeatures(TermVector urlTitleTermVe)
    {
        features.setContent_titleTFIDF(SimilarityCalculator.calculateDocumentTFIDF(wholeDocTermVec, urlTitleTermVe, 1));
        features.setContent_titleBM25(SimilarityCalculator.calculateDocumentBM25(wholeDocTermVec,urlTitleTermVe,1));
        features.setContent_titleLMIR_JM(LMIRCalculator.calculateLMIR_JM(wholeDocTermVec,urlTitleTermVe,1));
        features.setContent_titleLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(wholeDocTermVec,urlTitleTermVe,1));
        features.setContent_titleLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(wholeDocTermVec,urlTitleTermVe,1));
    }

    public void calculateContent_contentFeatures(TermVector urlContentTermVec)
    {
        features.setContent_contentTFIDF(SimilarityCalculator.calculateDocumentTFIDF(wholeDocTermVec, urlContentTermVec, 1));
        features.setContent_contentBM25(SimilarityCalculator.calculateDocumentBM25(wholeDocTermVec,urlContentTermVec,1));
        features.setContent_contentLMIR_JM(LMIRCalculator.calculateLMIR_JM(wholeDocTermVec,urlContentTermVec,1));
        features.setContent_contentLMIR_DIR(LMIRCalculator.calculateLMIR_DIR(wholeDocTermVec,urlContentTermVec,1));
        features.setContent_contentLMIR_ABS(LMIRCalculator.calculateLMIR_ABS(wholeDocTermVec,urlContentTermVec,1));
    }

}
