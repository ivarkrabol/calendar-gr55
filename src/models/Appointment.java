package models;

import java.time.LocalDate;
import java.time.LocalTime;


//Appointment class
//TODO: all of the functionality!


public class Appointment extends Attendable{

    private int ID;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Room room;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int generateID() {
        // TODO Auto-generated method stub
        return 0;
    }
}

