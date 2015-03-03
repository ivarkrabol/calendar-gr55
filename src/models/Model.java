package models;

import exceptions.DBConnectionException;
import util.DB;

import java.sql.SQLException;

public abstract class Model {

    public abstract void refresh(DB db) throws SQLException, DBConnectionException;
    public abstract void save(DB db) throws SQLException, DBConnectionException;

}
