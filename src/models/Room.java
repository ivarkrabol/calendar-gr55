package models;

import exceptions.DBConnectionException;
import exceptions.DBDriverException;
import exceptions.DBException;
import util.DB;
import util.ModelCache;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

public class Room extends Model{
	
	private int ID;
//	private String name;
//	private String description;
//	private List<LocalTime> reserved; //Unsure if this is the best way to represent the reserved times

	public int getID() {
		return ID;
	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	public List<LocalTime> getReserved() {
//		return reserved;
//	}

    public static Room getByID(int ID, DB db, ModelCache modelCache) throws SQLException, DBConnectionException {
        throw new DBConnectionException("Not yet implemented");
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {

    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {

    }
}