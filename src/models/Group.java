package models;

import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.SQLException;

public class Group extends Model{

    private int ID;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public void refresh(DB db, ModelCache mc) throws SQLException, DBConnectionException {

    }

    @Override
    public void save(DB db) throws SQLException, DBConnectionException {

    }
}
