package models;

import exceptions.DBConnectionException;
import exceptions.DBDriverException;
import exceptions.DBException;
import util.DB;
import util.ModelCache;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public static ArrayList<Room> getAvailable(LocalDateTime start, LocalDateTime end, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT R.RoomName as RoomName\n" +
                "FROM ROOM R, APPOINTMENT A\n" +
                "WHERE R.RoomName = A.RoomName\n" +
                "  AND (\n" +
                "    A.StartTime > " + end + " OR\n" +
                "    A.EndTime < " + start + "\n" +
                "  )\n" +
                "GROUP BY RoomName";

        ArrayList<Room> rooms = new ArrayList<>();
        ResultSet results = db.query(sql);
        while(results.next()) {
            rooms.add(getByName(results.getString("RoomName"), db, mc));
        }
        return rooms;
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