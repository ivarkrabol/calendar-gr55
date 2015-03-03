package models;

import exceptions.DBConnectionException;
import util.DB;

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
    public void refresh(DB db) throws SQLException, DBConnectionException {

    }

    @Override
    public void save(DB db) throws SQLException, DBConnectionException {

    }
}
