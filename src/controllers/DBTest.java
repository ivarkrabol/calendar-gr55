package controllers;

import exceptions.DBException;
import models.DB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTest {

    public static void main(String[] args) throws DBException, SQLException, IOException {
        ResultSet res = DB.query("SELECT * FROM TEST");
        while(res.next()) {
            System.out.println(res.getInt("id"));
        }
    }
}
