package Entities;

/**
 * Created by aman on 9/23/16.
 */
public class Product {
    private long ad_id;
    private long urlKey;
    private String title;
    private String publisherUrl;
    private int adPosition;
    private int categoryId;
    private double netBid;
    private double netTotalRevenue;
    private double clicked;
    private double price;
    private double originalPrice;
    private String description;
    private String merchant_name;
    private String manufacturer;

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

    private String attributes;
    private double sellerRating;
    private boolean popularBrand;
    private double expectedRPM;
    private Features features;




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

    public double getClicked() {
        return clicked;
    }

    public void setClicked(double clicked) {
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

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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

    public long getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(long urlKey) {
        this.urlKey = urlKey;
    }
}
