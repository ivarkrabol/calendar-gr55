package models;

import exceptions.DBConnectionException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import javax.print.DocFlavor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class Room extends Model{

    private StringProperty nameProperty = new SimpleStringProperty();
    public StringProperty getNameProperty(){
        return nameProperty;
    }
    public String getName() {
        return nameProperty.get();
    }
    public StringProperty namePropertyProperty() {return nameProperty;}

    public void setNameProperty(String nameProperty) {this.nameProperty.set(nameProperty);}
    private int size;
    private int availability;

    public Room(String name) {
        setNameProperty(name);
    }
    
    public Room(String name, int size) {
        setNameProperty(name);
        setSize(size);
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
    public void saveToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "UPDATE ROOM SET\n" +
                "Size = '" + getSize() + "'\n" +
                "WHERE RoomName ='" + getName()+"'";

        db.update(sql);

        refreshFromDB(db, mc);
    }

    @Override
    public void insertToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "INSERT INTO ROOM\n" +
                "(RoomName, Size, Availability)\n" +
                "VALUES (\n" +
                "'" + getName() + "'," + getSize() + ", '1')";
        db.update(sql);

        refreshFromDB(db, mc);
    }
}