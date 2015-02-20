package models;

import java.time.LocalTime;
import java.util.List;
public class Room extends Model{
	
	private int ID;
	private String name;
	private String description;
	private List<LocalTime> reserved; //Unsure if this is the best way to represent the reserved times
	
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
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
	public void setReserved(List<LocalTime> reserved) {
		this.reserved = reserved;
	}
	
}
