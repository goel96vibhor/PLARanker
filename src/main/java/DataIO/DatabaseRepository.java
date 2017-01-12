package DataIO;

import Entities.Product;
import Entities.View;
import Utils.ApplicationProperties;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aman on 10/11/16.
 */
public class DatabaseRepository {
    private static Logger logger = Logger.getLogger(DatabaseRepository.class.getName());
    private static DatabaseRepository dataBaseRepository = null;

    public static HashMap<Long, Product> getProductDetails() {
        return productDetails;
    }

    private static HashMap<Long, Product> productDetails;

    static {
        try {
            logger.info("Creating Database Repository");
            dataBaseRepository = new DatabaseRepository();
        } catch (Exception ex) {
            logger.error("ERROR: Creating Database Repository: " + ex.getMessage() + ": " + ex);
        }
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ApplicationProperties.getProperty("PRODUCT_DETAIL_MAP")));
            parseProductJson((HashMap<Long,String>) inputStream.readObject());
            inputStream.close();
        } catch (Exception e) {
            logger.info("ERROR : Reading Product Detail Map : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void parseProductJson(HashMap<Long, String> productJsonMap) {
        productDetails = new HashMap<>();
        for(Long adId : productJsonMap.keySet()) {
            Product product = new Product();
            JSONObject productJson = new JSONObject(productJsonMap.get(adId));
            product.setAd_id(productJson.getLong("ad_id"));
            product.setTitle(productJson.getString("title"));
            product.setPrice(productJson.getDouble("price"));
            product.setOriginalPrice(productJson.getDouble("original_price"));
            product.setDescription(productJson.getString("description"));
            product.setBrand(productJson.getString("brand"));
            product.setAttributes(productJson.getString("attribute_text"));
            product.setSellerRating(productJson.getDouble("seller_rating"));
            product.setExpectedRPM(productJson.getDouble("expected_rpm"));
            productDetails.put(adId, product);
        }
    }

    public static DatabaseRepository getInstance() {return dataBaseRepository;}

    public static List<Long> getUrlKeys() {return null;}

    public static List<Product> getProductsforUrlKey() {return null;}

    public static ResultSetConnectionPair getProductsForVertical(String vertical, int providerId) {
        ResultSetConnectionPair resultSetConnectionPair = null;
        try {
            resultSetConnectionPair = DatabaseManager.getProductsForVertical(vertical, providerId);
        } catch (Exception ex) {
            logger.error("ERROR while getting ResultSetConnection Pair for Vertical :  " + vertical + " : " + ex.getMessage());
        }
        return resultSetConnectionPair;
    }

    private static List<Product> getProductViews(ResultSet resultSet) {
        List<Product> products = new ArrayList<>();
        try {
            while(resultSet.next()) {
                Product product = new Product();
                product.setAd_id(resultSet.getLong("ad_id"));
                product.setAdPosition(resultSet.getInt("ad_position"));
                product.setCategoryId(resultSet.getInt("category_id"));
                product.setPublisherUrl(resultSet.getString("publisher_url"));
                product.setNetBid(resultSet.getDouble("net_bid"));
                product.setViewId(resultSet.getString("view_id"));
                product.setNetTotalRevenue(resultSet.getDouble("net_total_revenue"));
                product.setNetTotalRevenue(resultSet.getInt("click_status_present"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private static void fillProductDetails(Product product) {
        Product productInfo = productDetails.get(product.getAd_id());
        product.setTitle(productInfo.getTitle());
        product.setPrice(productInfo.getPrice());
        product.setOriginalPrice(productInfo.getOriginalPrice());
        product.setDescription(productInfo.getDescription());
        product.setBrand(productInfo.getBrand());
        product.setAttributes(productInfo.getBrand());
        product.setSellerRating(productInfo.getSellerRating());
        product.setExpectedRPM(productInfo.getExpectedRPM());
    }

    private static List<View> segregateViews(List<Product> products) {
        HashMap<String, View> pageViewMap = new HashMap<>();
        for(Product product : products) {
            if(!productDetails.containsKey(product.getAd_id()))
                continue;
            fillProductDetails(product);
            if(product.getPublisherUrl().contains("contextual.media.net"))
                continue;
            if (!pageViewMap.containsKey(product.getViewId()))
                pageViewMap.put(product.getViewId(), new View(product.getViewId(), new ArrayList<Product>(), product.getPublisherUrl()));
            pageViewMap.get(product.getViewId()).getAds().add(product);
        }
        return new ArrayList<>(pageViewMap.values());
    }

    public static List<View> getPageViews() {
        ResultSetConnectionPair resultSetConnectionPair = null;
        List<View> pageViews = new ArrayList<>();
        try {
            resultSetConnectionPair = DatabaseManager.getPageViews();
            ResultSet resultSet = resultSetConnectionPair.getResultSet();
            List<Product> products = getProductViews(resultSet);
            pageViews = segregateViews(products);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resultSetConnectionPair.closeBoth();
        }
        return pageViews;
    }
}