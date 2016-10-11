package DataIO;


import org.apache.log4j.Logger;
import Utils.Util;

import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: jigar.p
 * Date: Jul 15, 2010
 * Time: 12:45:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Database {
    private static Logger log = Logger.getLogger(Database.class.getName());

    protected Connection connection = null;
    protected boolean DB_AUTOCOMMIT = false;
    protected String DB_DRIVER = null;
    protected String DB_URL = null;
    protected String DB_USER = null;
    protected String DB_PASSWORD = null;

    public Database(String driver, String url, String user, String password, boolean autoCommit)
            throws Exception {
        this.DB_DRIVER = driver;
        this.DB_URL = url;
        this.DB_USER = user;
        this.DB_PASSWORD = password;
        this.DB_AUTOCOMMIT = autoCommit;
        log.debug("Database connecting string :: " + DB_URL);
        getConnection();
    }

    protected void getConnection()
            throws Exception {
        log.debug("Inside getConnection");
        try {
            if (Util.parseData(DB_DRIVER) == null) {
                log.fatal("DB_DRIVER not available");
                throw new Exception("DB_DRIVER not available");
            }

            if (Util.parseData(DB_URL) == null) {
                log.fatal("DB_URL not available");
                throw new Exception("DB_URL not available");
            }

            Class.forName(DB_DRIVER).newInstance();

            if (DB_USER == null && DB_PASSWORD == null) {
                connection = DriverManager.getConnection(DB_URL);
            } else {
                if (DB_USER == null) {
                    DB_USER = "";
                }
                if (DB_PASSWORD == null) {
                    DB_PASSWORD = "";
                }
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            connection.setAutoCommit(DB_AUTOCOMMIT);
            log.debug("Setting auto commit to :: " + DB_AUTOCOMMIT);
        } catch (SQLException error) {
            log.fatal("Database connection error", error);
            throw new Exception("There was an error while connection to the database. Please retry. ", error);
        } catch (Exception error) {
            log.fatal("Database connection error", error);
            throw new Exception(error);
        }
    }

    public ResultSet executeQuery(String query) throws Exception {
        log.debug("Inside executeQuery");
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
//            log.info("SQL Select Query : " + query);
            resultSet = statement.executeQuery(query);
        } catch (SQLException error) {
            log.error("Database access error", error);
            throw new Exception(error);
        }
        return resultSet;
    }

    public ResultSet executeQuery(StringBuffer query)
            throws Exception {
        return executeQuery(query.toString());
    }


    public int executeUpdate(String query)
            throws Exception {
        log.info("Inside executeUpdate");

        int result;
        try {
            log.info("SQL Non-Select Query : " + query);
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(query);
        } catch (SQLException error) {
            log.error("Database access error", error);
            throw new Exception(error);
        } catch (Exception error) {
            log.error("Database access error", error);
            throw new Exception(error);
        }

        return result;
    }

    public CallableStatement getCallableStatement(String query) throws Exception {
        log.debug("Inside getCallableStatement");
        CallableStatement callableStatement;
        try {
            callableStatement = connection.prepareCall(query);
        } catch (SQLException error) {
            log.error("Database access error", error);
            throw new Exception(error);
        }
        return callableStatement;
    }

    public PreparedStatement getPreparedStatement(String query)
            throws Exception {
        log.debug("Inside getTermVectorPreparedStatement");
        PreparedStatement preparedStatement;
        try {
            log.debug("PreparedStatemnt :: " + query);
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException error) {
            log.error("Database access error", error);
            throw new Exception(error);
        }
        return preparedStatement;
    }


    public PreparedStatement getPreparedStatement(StringBuffer query)
            throws Exception {
        return getPreparedStatement(query.toString());
    }

    public CallableStatement getCallableStatement(StringBuffer query)
            throws Exception {
        return getCallableStatement(query.toString());
    }

    public void commit()
            throws Exception {
        log.debug("Inside commit");

        try {
            connection.commit();
        } catch (SQLException error) {
            log.error("Database access error", error);
            throw new Exception(error);
        }
    }

    public void rollback()
            throws Exception {
        log.debug("Inside rollback");

        try {
            connection.rollback();
        } catch (SQLException error) {
            log.error("Database access error", error);
            throw new Exception(error);
        }
    }

    public void close()
            throws Exception {
        log.debug("Inside close with connection = " + connection);

        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException error) {
            log.error("Database access error", error);
            throw new Exception(error);
        }
    }

    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }
}
