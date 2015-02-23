package controllers;

import models.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws DB.DatabaseException, SQLException {
        ResultSet res = DB.query("SELECT * FROM TEST");
        while(res.next()) {
            System.out.println(res.getInt("id"));
        }
    }
}
