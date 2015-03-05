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
	
	private String name;
    private int size;

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
		return name;
	}

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static Room getByName(String name, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        Room room;
        if(mc.contains(Room.class, name)) room = mc.get(Room.class, name);
        else room = new Room(name);
        mc.put(name, room);
        room.refreshFromDB(db, mc);
        return room;
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT Size\n" +
                "FROM ROOM\n" +
                "WHERE RoomName = " + getName();
        ResultSet results = db.query(sql);
        if (!results.next()) throw new SQLException("No Room with that Name in database");
        setSize(results.getInt("Size"));
        if(results.next()) throw new SQLException("Result not unique");
    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE ROOM\n" +
                "Size = '" + getSize() + "'\n" +
                "WHERE RoomName = " + getName();

        db.query(sql);
    }
}