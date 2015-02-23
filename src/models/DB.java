package models;

import controllers.Config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ivar on 20.02.2015.
 */
public class DB {

    public static class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }

    private static DB singleton;

    private String url;
    private String database;
    private String user;
    private String pswd;
    private Class driverClass;
    private Connection connection;

    private DB() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {
        url = Config.getProperty("db_url");
        database = Config.getProperty("db_database");
        user = Config.getProperty("db_user");
        pswd = Config.getProperty("db_pswd");
        driverClass = Class.forName(Config.getProperty("db_driver"));

        connection = getConnection();
    }

    private static DB getSingleton() throws ClassNotFoundException, SQLException, InstantiationException, IOException, IllegalAccessException {
        if(singleton == null) singleton = new DB();
        return singleton;
    }

    private Connection getConnection() throws IOException, SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        int dbTimeout = Integer.parseInt(Config.getProperty("db_timeout"));
        if(connection == null || !connection.isValid(dbTimeout)) {
            driverClass.newInstance();
            connection = DriverManager.getConnection(url + database, user, pswd);
        }
        return connection;
    }

    public static ResultSet query(String sql) throws DatabaseException, SQLException {
        Connection connection;
        try {
            connection = getSingleton().getConnection();
        } catch(Exception e) {
            throw new DatabaseException(e.toString());
        }
        return  connection.createStatement().executeQuery(sql);
    }



}
