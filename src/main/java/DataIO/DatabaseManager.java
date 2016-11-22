package DataIO;

import Utils.ApplicationProperties;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;

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

    public static ResultSetConnectionPair getProductsForVertical(String vertical) {
        try {
            Database database = getAdMaster();
            CallableStatement callableStatement = database.getCallableStatement("{call (?)}");
            callableStatement.setString(1, vertical);
//            callableStatement.setFetchSize(100000);
            return new ResultSetConnectionPair(callableStatement.executeQuery(), database);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("ERROR: Getting active test buckets: " + ex.getMessage() + ": " + ex);
        }
        return null;
    }
}
