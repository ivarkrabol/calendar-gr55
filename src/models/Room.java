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
    private StringProperty nameProperty = new SimpleStringProperty();
    public String getName() {
        return nameProperty.get();
    }
    public StringProperty namePropertyProperty() {return nameProperty;}

    public void setNameProperty(String nameProperty) {this.nameProperty.set(nameProperty);}
    private int size;

    public Room(String name) {
        setNameProperty(name);
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static ObservableList<Room> getAvailable(LocalDateTime start, LocalDateTime end, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        String sql = "SELECT r.RoomName\n" +
                "FROM ROOM as r \n" +
                "WHERE r.Availability = 1  and r.roomname not in\n" +
                "(select a.roomname from APPOINTMENT as a where \n" +
                "a.StartTime = '" +start+"'\n" +
                "AND a.EndTime = '" +end+"')";
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
                "WHERE RoomName ='" + getName()+"'";
        ResultSet results = db.query(sql);
        if (!results.next()) throw new SQLException("No Room with that Name in database");
        setSize(results.getInt("Size"));
        if(results.next()) throw new SQLException("Result not unique");
    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE ROOM\n" +
                "Size = '" + getSize() + "'\n" +
                "WHERE RoomName ='" + getName()+"'";

    }
}