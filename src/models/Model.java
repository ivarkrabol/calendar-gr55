package models;

import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.SQLException;

public abstract class Model {

    public abstract void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException;
    public abstract void saveToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException;
    public abstract void insertToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException;


}
