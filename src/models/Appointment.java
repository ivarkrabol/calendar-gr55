package models;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import exceptions.DBConnectionException;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;


public class Appointment extends Model {

    private int id;
    private User administrator;
    private StringProperty titleProperty = new SimpleStringProperty();
    private StringProperty descriptionProperty = new SimpleStringProperty();
    private StringProperty calendarProperty = new SimpleStringProperty();
    public final StringProperty emptyProperty = new SimpleStringProperty("empty", " ");

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

    private Property<LocalDate> startDateProperty = new ObjectPropertyBase<LocalDate>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "start date";
        }
    };

    private Property<LocalTime> startTimeProperty = new ObjectPropertyBase<LocalTime>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "start time";
        }
    };

    private Property<LocalDate> endDateProperty = new ObjectPropertyBase<LocalDate>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "end date";
        }
    };

    private Property<LocalTime> endTimeProperty = new ObjectPropertyBase<LocalTime>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "end time";
        }
    };



    public Appointment(){ }
    public Appointment(String title, String description, LocalDate startDate,
                       LocalDate endDate, LocalTime startTime, LocalTime endTime, Room room, User user){
        setTitle(title);
        setDescription(description);
        setStartDateProperty(startDate);
        setEndDateProperty(endDate);
        setStartTimeProperty(startTime);
        setEndTimeProperty(endTime);
        setRoom(room);
        setAdministrator(user);
    }

    public StringProperty EmptyProperty() {
        return emptyProperty;
    }

    public String getCalendarProperty() {return calendarProperty.get();}

    public void setCalendarProperty(String calendarProperty) {this.calendarProperty.set(calendarProperty);}

    public StringProperty CalendarProperty() {
        return calendarProperty;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
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

    public StringProperty TitleProperty() {return titleProperty;}

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

    public LocalDate getStartDateProperty() {
        return startDateProperty.getValue();
    }

    public Property<LocalDate> startDateProperty() {
        return startDateProperty;
    }

    public void setStartDateProperty(LocalDate startDateProperty) {
        this.startDateProperty.setValue(startDateProperty);
    }

    public LocalTime getStartTimeProperty() {
        return startTimeProperty.getValue();
    }

    public Property<LocalTime> startTimeProperty() {
        return startTimeProperty;
    }

    public void setStartTimeProperty(LocalTime startTimeProperty) {this.startTimeProperty.setValue(startTimeProperty);}

    public LocalDate getEndDateProperty() {
        return endDateProperty.getValue();
    }

    public Property<LocalDate> endDateProperty() {
        return endDateProperty;
    }

    public void setEndDateProperty(LocalDate endDateProperty) {
        this.endDateProperty.setValue(endDateProperty);
    }

    public LocalTime getEndTimeProperty() {
        return endTimeProperty.getValue();
    }

    public Property<LocalTime> endTimeProperty() {
        return endTimeProperty;
    }

    public void setEndTimeProperty(LocalTime endTimeProperty) {
        this.endTimeProperty.setValue(endTimeProperty);
    }

    public boolean canEditApp(User user, DB db) throws DBConnectionException, SQLException {
        int UserID = user.getId();
        List<Object> admin = null;
        ResultSet rs;
        rs = db.query("SELECT AppointmentID FROM APPOINTMENT WHERE AdministratorID = " + UserID + " AND AppointmentID = " + getId());
        while (rs.next()) {
            admin.add(rs.getInt("AppointmentID"));
        }
        return admin.isEmpty();
    }

    public ObservableList<User> isInvitedToApp(DB db, int id, ModelCache mc) throws DBConnectionException, SQLException  {
        return getStatusToApp(db, id, mc, "NotAnswered");
    }

    public ObservableList<User> hasAcceptedToApp(DB db, int id, ModelCache mc) throws DBConnectionException, SQLException  {
        return getStatusToApp(db, id, mc, "HasAccepted");
    }
    public ObservableList<User> hasDeclinedToApp(DB db, int id, ModelCache mc) throws DBConnectionException, SQLException  {
        return getStatusToApp(db, id, mc, "HasDeclined");
    }

    public ObservableList<User> getStatusToApp(DB db, int id, ModelCache mc, String status) throws DBConnectionException, SQLException  {
        ObservableList<User> participants = FXCollections.observableArrayList();
        ResultSet rs;
        rs = db.query("SELECT UserID FROM PARTICIPANTS WHERE AppointmentID = " + id +"AND response ='"+status+"'");
        while (rs.next()) {
            User user = User.getById(rs.getInt("UserID"), db, mc);
            participants.add(user);
        }
        return participants;
    }

    public static Appointment getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        Appointment appointment;
        if(mc.contains(Appointment.class, id)) appointment = mc.get(Appointment.class, id);

        else appointment = new Appointment();
        appointment.setId(id);
        appointment.refreshFromDB(db, mc);
        mc.put(id, appointment);
        return appointment;
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT Title, StartTime, EndTime, AdministratorID, Description, RoomName\n" +
                "FROM APPOINTMENT\n" +
                "WHERE AppointmentID = " + getId();
        ResultSet results = db.query(sql);
        if(!results.next()) throw new SQLException("No Appointment with ID '" + id + "' found");
        setTitle(results.getString("Title"));
        setStartDateProperty(results.getTimestamp("StartTime").toLocalDateTime().toLocalDate());
        setStartTimeProperty(results.getTimestamp("StartTime").toLocalDateTime().toLocalTime());
        setEndDateProperty(results.getTimestamp("EndTime").toLocalDateTime().toLocalDate());
        setEndTimeProperty(results.getTimestamp("EndTime").toLocalDateTime().toLocalTime());
        setAdministrator(User.getById(results.getInt("AdministratorID"), db, mc));
        setCalendarProperty("" + localTimeFormat(getStartTimeProperty()) + "-" + localTimeFormat(getEndTimeProperty()) + "\n" + getTitle());
        setDescription(results.getString("Description"));
        String room=results.getString("RoomName");
        if(room == null){
            setRoom(null);
        }else{
            setRoom(Room.getByName(room, db, mc));
        }
        if(results.next()) throw new SQLException("Result not unique");
    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE APPOINTMENT\n" +
                "StartTime = " + LocalDateTime.from(getStartDateProperty()).with(getStartTimeProperty()) + ",\n" +
                "EndTime = " + LocalDateTime.from(getEndDateProperty()).with(getEndTimeProperty()) + ",\n" +
                "AdministratorID = " + getAdministrator().getId() + ",\n" +
                "Description = '" + getDescription() + "',\n" +
                "RoomName = " + getRoom().getName() + "\n" +
                "WHERE AppointmentID = " + getId();
        db.query(sql);
    }

    public void insertToDB(DB db, Appointment appointment){
        try{String sql = "INSERT INTO `APPOINTMENT` "
                + "(`Title`, `StartTime`, `EndTime`, `AdministratorID`, "
                + "`Description`) VALUES ('"+ appointment.getTitle() +"', "
                + "'"+ LocalDateTime.of(appointment.getStartDateProperty(), appointment.getStartTimeProperty()) + "', "
                + "'" + LocalDateTime.of(appointment.getEndDateProperty(), appointment.getEndTimeProperty()) + "', "
                + "'" + appointment.getAdministrator().getId() +"', "
                + "'" + appointment.getDescription() + "')";
            db.insert(sql);
            String sql2 = "select max(AppointmentID) from APPOINTMENT";
            ResultSet result = db.query(sql2);
            if(result.next()){
                appointment.setId(result.getInt(1));
            }
            String sql3 = "INSERT INTO `PARTICIPANTS` (`UserID`, `AppointmentID`, `Response`) VALUES ('"+ appointment.getAdministrator().getId() + "', '"
                    + appointment.getId() + "', 'HasAccepted')"
                    ;
            db.insert(sql3);
        }catch(SQLException e){e.printStackTrace();
        }catch (DBConnectionException e){e.printStackTrace();}
    }

    public String localTimeFormat(LocalTime time){
        String res = "";
        if(time.getHour()<10){
            res += "0"+time.getHour()+":";
        }else{
            res += time.getHour()+":";
        }  if(time.getMinute()<10){
            res += "0"+time.getMinute();
        }else{
            res += time.getMinute();
        }
        return res;
    }

    @Override
    public int compareTo(Appointment appointment) {
        if(this.getStartTimeProperty().isBefore(appointment.getStartTimeProperty())){
            return -1;
        }if(this.getStartTimeProperty().isAfter(appointment.getStartTimeProperty())){
            return 1;
        }else{ return 0;}
    }



}
