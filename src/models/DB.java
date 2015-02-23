package models;

import controllers.Config;
import exceptions.DBException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ivar on 20.02.2015.
 */
public class DB {

    private static DB singleton;

    private String url;
    private String database;
    private String user;
    private String pswd;
    private Class driverClass;
    private Connection connection;

    private DB() throws IOException, DBException {
        url = Config.getProperty("db_url");
        database = Config.getProperty("db_database");
        user = Config.getProperty("db_user");
        pswd = Config.getProperty("db_pswd");
        try {
            driverClass = Class.forName(Config.getProperty("db_driver"));
        } catch(ClassNotFoundException e) {
            throw new DBException(e);
        }

        connection = getConnection();
    }

    private static DB getSingleton() throws IOException, DBException {
        if(singleton == null) singleton = new DB();
        return singleton;
    }

    private Connection getConnection() throws IOException, DBException {
        int dbTimeout = Integer.parseInt(Config.getProperty("db_timeout"));
        try {
            if(connection == null || !connection.isValid(dbTimeout)) {
                driverClass.newInstance();
                connection = DriverManager.getConnection(url + database, user, pswd);
            }
        } catch(Exception e) {
            throw new DBException(e);
        }
        return connection;
    }

    public static ResultSet query(String sql) throws DBException, IOException {
        ResultSet result;
        try {
            result = getSingleton().getConnection().createStatement().executeQuery(sql);
        } catch(SQLException e) {
            throw new DBException(e);
        }
        return result;
    }
}
