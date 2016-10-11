package DataIO;

import Utils.ApplicationProperties;
import org.apache.log4j.Logger;

/**
 * Created by aman on 10/11/16.
 */
public class DatabaseManager {
    private static Logger logger = Logger.getLogger(Database.class.getName());

    public static Database getStatsDb() {
        try {
            return new Database(ApplicationProperties.getProperty("DB_MSSQL_DRIVER"),
                    ApplicationProperties.getProperty("STATS_DB"),
                    ApplicationProperties.getProperty("DB_USER"),
                    ApplicationProperties.getProperty("DB_PASSWORD"),
                    Boolean.parseBoolean(ApplicationProperties.getProperty("DB_AUTO_COMMIT")));
        } catch (Exception ex) {
            logger.error("ERROR: Getting database connection " + ApplicationProperties.getProperty("STATS_DB") + ": " + ex.getMessage() + ": " + ex);
        }
        return null;
    }
}
