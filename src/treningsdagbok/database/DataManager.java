package treningsdagbok.database;

import treningsdagbok.utils.TimeUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataManager {
    private static final Logger LOGGER = Logger.getLogger(DataManager.class.getName());

    private String url = null;
    private long startup;
    private int timeout = 0, port = 3306, queriesCount = 0;
    private Connection con = null;

    public DataManager(String host, String database) {
        this.url = generateJdbcUrl(host, database);
        outputDrivers();
    }

    public Connection getConnection() {
        return this.con;
    }

    private boolean isConnected() {
        try {
            if (this.con != null) {
                return !this.con.isClosed();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private String generateJdbcUrl(String host, String database) {
        if (host == null || database == null) {
            LOGGER.severe("Could not set H2 URL. Host: " + host + ", Database: " + database);
            throw new IllegalArgumentException("host/database cannot be null");
        }
        return "jdbc:h2:./" + database;
        /*MySQL
        return "jdbc:mysql://" + host + "/" + database
                + "?zeroDateTimeBehavior=convertToNull"
                + "&jdbcCompliantTruncation=false"
                + "&autoReconnect=true"
                + "&characterEncoding=UTF-8"
                + "&characterSetResults=UTF-8";
                */
    }

    private void outputDrivers() {
        if (LOGGER.getLevel() == Level.FINE) {
            LOGGER.fine("Checking DriverManager drivers.");
            Enumeration<Driver> driverList = DriverManager.getDrivers();
            int count = 0;
            while (driverList.hasMoreElements()) {
                Driver driverClass = driverList.nextElement();
                LOGGER.fine("Found driver #" + (count + 1) + ": " + driverClass.getClass().getName());
                count++;
            }
            LOGGER.fine("Found " + count + " drivers in DriverManager.");
        }
    }

    public void connect(String username, String password) {
        if (this.url == null) {
            // TODO: missing url, throw exception
            return;
        } else if (this.con != null && isConnected()) {
            // TODO: already conected, log debug message
            return;
        }
        long start = System.currentTimeMillis();
        try {
            LOGGER.fine("Connecting to H2 with URL '" + this.url + "'.");
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.h2.Driver");
            this.con = DriverManager.getConnection(this.url, username, password);
            this.startup = System.currentTimeMillis() / 1000;
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Could not connect to the database due to no driver could be found for 'H2'.");
            LOGGER.fine("Connection attempt took " + new TimeUtils((System.currentTimeMillis() - start) / 1000).toString() + ".");
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            LOGGER.severe("Could not connect to the database for 'H2' due to a SQL Exception.");
            LOGGER.fine("Connection attempt took " + new TimeUtils((System.currentTimeMillis() - start) / 1000).toString() + ".");
            e.printStackTrace();
            return;
        }
        LOGGER.fine("Took " + new TimeUtils((System.currentTimeMillis() - start) / 1000).toString() +
                " to establish a connection for 'H2'.");
    }
}
