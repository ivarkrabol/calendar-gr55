package models;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import exceptions.DBConnectionException;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.DB;
import util.ModelCache;


public class Appointment extends Model {

    private int id;
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


    public int getId() {
        return id;
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

    public static Appointment getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        Appointment appointment;
        if(mc.contains(Appointment.class, id)) appointment = mc.get(Appointment.class, id);
        else appointment = new Appointment();
        appointment.refreshFromDB(db, mc);
        mc.put(id, appointment);
        return appointment;
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT StartTime, EndTime, AdministratorID, Description, RoomID\n" +
                "FROM APPOINTMENT\n" +
                "WHERE AppointmentID = " + getId();

        ResultSet results = db.query(sql);
        if(!results.next()) throw new SQLException("No Appointment with ID '" + id + "' found");
        setStartTime(results.getTimestamp("StartTime").toLocalDateTime());
        setEndTime(results.getTimestamp("EndTime").toLocalDateTime());
        setAdministrator(User.getById(results.getInt("AdministratorID"), db, mc));
        setDescription(results.getString("Description"));
        setRoom(Room.getById(results.getInt("RoomName"), db, mc));
        if(results.next()) throw new SQLException("Result not unique");
    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE APPOINTMENT\n" +
                "StartTime = '" + getStartTime() + "',\n" +
                "EndTime = '" + getEndTime() + "',\n" +
                "AdministratorID = " + getAdministrator().getId() + ",\n" +
                "Description = '" + getDescription() + "',\n" +
                "RoomName = " + getRoom().getId() + "\n" +
                "WHERE AppointmentID = " + getId();

        db.query(sql);
    }
}

