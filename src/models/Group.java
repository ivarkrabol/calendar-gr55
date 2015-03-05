package models;

import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.SQLException;

public class Group extends Model{

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {}

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {}
}
