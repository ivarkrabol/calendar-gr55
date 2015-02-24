package models;

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
	private String name;
	private String description;
	private List<LocalTime> reserved; //Unsure if this is the best way to represent the reserved times

	public int getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<LocalTime> getReserved() {
		return reserved;
	}

    public static Room getByID(int id) throws DBException, IOException, SQLException {
        Room room;
        if(ModelCache.contains(Room.class, id)) room = ModelCache.get(Room.class, id);
        else room = new Room();
        ResultSet results;
        results = DB.query("SELECT `name`, `description` FROM `TEST` WHERE `id` = " + id);
        if(results.next()) {
            room.setName(results.getString("name"));
            room.setDescription(results.getString("description"));
            ModelCache.put(id, room);
            return room;
        }
        throw new NoSuchElementException("SQL result set was empty.");
    }

}