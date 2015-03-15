package util;

import exceptions.DBConnectionException;
import exceptions.DBDriverException;
import exceptions.DBException;
import exceptions.MissingPropertyException;
import com.mysql.jdbc.ConnectionImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by ivar on 20.02.2015.
 */
public class DB {

    private HashMap<String, String> properties;
    private Class driverClass;
    private Connection connection;
    private int timeout;

    public void configure(Config config) throws DBException, MissingPropertyException {
        properties = config.getProperties(new String[]{
                "db_url", "db_database", "db_driver", "db_user", "db_pswd", "db_timeout"});
        try {
            driverClass = Class.forName(properties.get("db_driver"));
            driverClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new DBDriverException(e);
        } catch (InstantiationException e) {
            throw new DBDriverException(e);
        } catch (IllegalAccessException e) {
            throw new DBDriverException(e);
        }
        timeout = Integer.parseInt(properties.get("db_timeout"));
    }

    public void connect() throws DBConnectionException {
        try {
            driverClass.newInstance();
        } catch (InstantiationException e) {
            throw new DBConnectionException(e);
        } catch (IllegalAccessException e) {
            throw new DBConnectionException(e);
        }
        try {
            connection = DriverManager.getConnection(
                    properties.get("db_url") + properties.get("db_database"),
                    properties.get("db_user"), properties.get("db_pswd"));
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        }
    }

    private void ensureConnection() throws DBConnectionException {
        try {
            if(!connection.isValid(timeout)) connect();
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        }
    }

    public ResultSet query(String sql) throws SQLException, DBConnectionException {
        ResultSet result;
        ensureConnection();
        result = connection.createStatement().executeQuery(sql);
        return result;
    }
    public void update(String sql) throws SQLException, DBConnectionException {
    	ensureConnection();
        connection.createStatement().executeUpdate(sql);
    }
}
