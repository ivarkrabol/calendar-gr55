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

    public Appointment(String title){
        setTitle(title);
    }
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

    public static Appointment getByID(int id, DB db, ModelCache modelCache) throws SQLException, DBConnectionException {
        Appointment appointment;
        if(modelCache.contains(Appointment.class, id)) appointment = modelCache.get(Appointment.class, id);
        else appointment = new Appointment("");
        ResultSet results = db.query("" +
                "SELECT StartTime, EndTime, AdministratorID, Description, RoomID\n" +
                "FROM APPOINTMENT\n" +
                "WHERE AppointmentID = " + id);
        if(!results.next()) throw new SQLException("No Appointment with id '" + id + "' found");
        appointment.setStartTime(results.getTimestamp("StartTime").toLocalDateTime());
        appointment.setEndTime(results.getTimestamp("EndTime").toLocalDateTime());
        appointment.setAdministrator(User.getByID(results.getInt("AdministratorID"), db, modelCache));
        appointment.setDescription(results.getString("Description"));
        appointment.setRoom(Room.getByID(results.getInt("RoomID"), db, modelCache));
        if(results.next()) throw new SQLException("Result not unique");
        modelCache.put(id, appointment);
        return appointment;
    }


}

