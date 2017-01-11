package DataIO;

import Utils.ApplicationProperties;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

/**
 * Created by aman on 10/11/16.
 */
public class DatabaseManager {
    private static Logger logger = Logger.getLogger(Database.class.getName());

    public static Database getAdMaster() {
        try {
            return new Database(ApplicationProperties.getProperty("DB_MSSQL_DRIVER"),
                    ApplicationProperties.getProperty("AD_MASTER"),
                    ApplicationProperties.getProperty("DB_USER"),
                    ApplicationProperties.getProperty("DB_PASSWORD"),
                    Boolean.parseBoolean(ApplicationProperties.getProperty("DB_AUTO_COMMIT")));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("ERROR: Getting database connection " + ApplicationProperties.getProperty("STATS_DB") + ": " + ex.getMessage() + ": " + ex);
        }
        return null;
    }

    public static Database getStatsDb() {
        try {
            return new Database(ApplicationProperties.getProperty("DB_MSSQL_DRIVER"),
                    ApplicationProperties.getProperty("STATS_DB"),
                    ApplicationProperties.getProperty("DB_USER"),
                    ApplicationProperties.getProperty("DB_PASSWORD"),
                    Boolean.parseBoolean(ApplicationProperties.getProperty("DB_AUTO_COMMIT")));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("ERROR: Getting database connection " + ApplicationProperties.getProperty("STATS_DB") + ": " + ex.getMessage() + ": " + ex);
        }
        return null;
    }

    public static ResultSetConnectionPair getProductsForVertical(String vertical, int providerId) {
        try {
            Database database = getAdMaster();
            CallableStatement callableStatement = database.getCallableStatement("{call GET_AD_MASTER_LEARNING (?,?)}");
            callableStatement.setString(1, vertical);
            callableStatement.setInt(2, providerId);
//            callableStatement.setFetchSize(100000);
            return new ResultSetConnectionPair(callableStatement.executeQuery(), database);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("ERROR: Getting active test buckets: " + ex.getMessage() + ": " + ex);
        }
        return null;
    }

    public static ResultSetConnectionPair getPageViews() {
        try {
            Database database = getStatsDb();
            PreparedStatement preparedStatement = database.getPreparedStatement("select * from ebay_learning_stats(nolock)");
            return new ResultSetConnectionPair(preparedStatement.executeQuery(), database);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
