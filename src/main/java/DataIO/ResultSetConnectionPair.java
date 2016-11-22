package DataIO;

import org.apache.log4j.Logger;
import java.sql.ResultSet;

/**
 * Created by aman on 10/20/16.
 */
public class ResultSetConnectionPair {
    Logger logger = Logger.getLogger(ResultSetConnectionPair.class.getName());
    private ResultSet resultSet;
    private Database database;

    public ResultSetConnectionPair(ResultSet resultSet, Database database) {
        this.resultSet = resultSet;
        this.database = database;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void closeBoth() {
        try {
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error closing result set", e);
        }
        try {
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error closing database", e);
        }
    }
}
