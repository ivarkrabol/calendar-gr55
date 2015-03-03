package models;

import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.SQLException;

public abstract class Model {

    public abstract void refresh(DB db, ModelCache mc) throws SQLException, DBConnectionException;
    public abstract void save(DB db) throws SQLException, DBConnectionException;

}
