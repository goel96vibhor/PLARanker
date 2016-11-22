package DataIO;

import Entities.Product;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 10/20/16.
 */
public class ResultSetParser {
    Logger logger = Logger.getLogger(ResultSetParser.class.getName());

    public static List<Product> getProductForSubCategory(ResultSet resultSet) {
        List<Product> products = new ArrayList<Product>();
        try {
            while(resultSet.next()) {
                Product product = new Product();
                product.setAd_id(resultSet.getInt("ad_id"));
                product.setTitle(resultSet.getString("OFFER_TITLE"));
                product.setDescription(resultSet.getString("description"));
                product.setAttributes(resultSet.getString("attribute"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static Product getNextProduct(ResultSet resultSet) {
        Product product = new Product();
        try {
            product.setAd_id(resultSet.getInt("ad_id"));
            product.setTitle(resultSet.getString("OFFER_TITLE"));
            product.setDescription(resultSet.getString("description"));
            product.setAttributes(resultSet.getString("attribute"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
}
