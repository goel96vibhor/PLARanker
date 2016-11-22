package DataIO;

import Entities.Product;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 10/11/16.
 */
public class DatabaseRepository {
    private static Logger logger = Logger.getLogger(DatabaseRepository.class.getName());
    private static DatabaseRepository dataBaseRepository = null;

    static {
        try {
            logger.info("Creating Database Repository");
            dataBaseRepository = new DatabaseRepository();
        } catch (Exception ex) {
            logger.error("ERROR: Creating Database Repository: " + ex.getMessage() + ": " + ex);
        }
    }

    public static DatabaseRepository getInstance() {return dataBaseRepository;}

    public static List<Long> getUrlKeys() {return null;}

    public static List<Product> getProductsforUrlKey() {return null;}

    public static ResultSetConnectionPair getProductsForVertical(String vertical) {
        ResultSetConnectionPair resultSetConnectionPair = null;
        try {
            resultSetConnectionPair = DatabaseManager.getProductsForVertical(vertical);
        } catch (Exception ex) {
            logger.error("ERROR while getting ResultSetConnection Pair for Vertical :  " + vertical + " : " + ex.getMessage());
        }
        return resultSetConnectionPair;
    }

}