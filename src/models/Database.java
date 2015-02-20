package models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivar on 20.02.2015.
 */
public class Database {

    public static Map<String, String> query(String queryStr) {
        return  new HashMap<String, String>();
    }

}

//    public Connection DBConnection() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
//        String url = "jdbc:mysql://mysql.stud.ntnu.no/";
//        String dbName = "peterhol_calendardb";
//        String driver = "com.mysql.jdbc.Driver";
//        Class.forName(driver).newInstance();
//        Connection conn = DriverManager.getConnection(url+dbName,userName,password);
//        return conn;
//    }