package models;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import exceptions.DBConnectionException;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.DB;
import util.ModelCache;


public class Appointment extends Model {

    private int ID;
    private User administrator;
    private StringProperty titleProperty = new SimpleStringProperty();
    private StringProperty descriptionProperty = new SimpleStringProperty();
    private Property<Room> roomProperty =  new ObjectPropertyBase<Room>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "room";
        }
    };

    private Property<LocalDateTime> startTimeProperty = new ObjectPropertyBase<LocalDateTime>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "start time";
        }
    };


    public Appointment(){ }
    public Appointment(String title){
        setTitle(title);
    }


    private Property<LocalDateTime> endTimeProperty = new ObjectPropertyBase<LocalDateTime>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "end time";
        }
    };


    public int getID() {
        return ID;
    }

    public User getAdministrator() {
        return administrator;
    }

    private void setAdministrator(User administrator) {
        this.administrator = administrator;
    }

    public String getTitle() {
        return titleProperty.get();
    }

    public void setTitle(String titleProperty) {
        this.titleProperty.set(titleProperty);
    }

    public StringProperty TitleProperty() {
        return titleProperty;
    }

    public String getDescription() {
        return descriptionProperty.getValue();
    }

    public void setDescription(String formal) {
        descriptionProperty.setValue(formal);
    }

    public StringProperty DescriptionProperty() {
        return descriptionProperty;
    }


    public Room getRoom() {
        return roomProperty.getValue();
    }

    public void setRoom(Room room) {
        roomProperty.setValue(room);
    }

    public Property<Room> RoomProperty() {
        return roomProperty;
    }

    public LocalDateTime getStartTime() {
        return startTimeProperty.getValue();
    }

    public void setStartTime(LocalDateTime startTime) {
        startTimeProperty.setValue(startTime);
    }

    public Property<LocalDateTime> StartTimeProperty() {
        return startTimeProperty;
    }

    public LocalDateTime getEndTime() {
        return endTimeProperty.getValue();
    }

    public void setEndTime(LocalDateTime endTime) {
        endTimeProperty.setValue(endTime);
    }

    public Property<LocalDateTime> EndTimeProperty() {
        return endTimeProperty;
    }

    public static Appointment getByID(int ID, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        Appointment appointment;
        if(mc.contains(Appointment.class, ID)) appointment = mc.get(Appointment.class, ID);
        else appointment = new Appointment();
        appointment.refresh(db, mc);
        mc.put(ID, appointment);
        return appointment;
    }

    @Override
    public void refresh(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT StartTime, EndTime, AdministratorID, Description, RoomID\n" +
                "FROM APPOINTMENT\n" +
                "WHERE AppointmentID = " + getID();

        ResultSet results = db.query(sql);
        if(!results.next()) throw new SQLException("No Appointment with ID '" + ID + "' found");
        setStartTime(results.getTimestamp("StartTime").toLocalDateTime());
        setEndTime(results.getTimestamp("EndTime").toLocalDateTime());
        setAdministrator(User.getByID(results.getInt("AdministratorID"), db, mc));
        setDescription(results.getString("Description"));
        setRoom(Room.getByID(results.getInt("RoomID"), db, mc));
        if(results.next()) throw new SQLException("Result not unique");
    }

    @Override
    public void save(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE APPOINTMENT\n" +
                "StartTime = '" + getStartTime() + "',\n" +
                "EndTime = '" + getEndTime() + "',\n" +
                "AdministratorID = " + getAdministrator().getID() + ",\n" +
                "Description = '" + getDescription() + "',\n" +
                "RoomID = " + getRoom().getID() + "\n" +
                "WHERE AppointmentID = " + getID();

        db.query(sql);
    }
}

