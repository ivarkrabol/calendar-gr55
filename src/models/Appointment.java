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


public class Appointment extends Attendable implements Comparable<Appointment>  {

    private int id;
//    ObservableList<User> invitedParticipants;
//    ObservableList<User> acceptedParticipants;
//    ObservableList<User> declinedParticipants;

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



    private Appointment(){
        super();
    }
    public Appointment(String title, String description, LocalDate startDate,
                       LocalDate endDate, LocalTime startTime, LocalTime endTime, Room room, User administrator){
        super();
        setTitle(title);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
        setStartTime(startTime);
        setEndTime(endTime);
        setRoom(room);
        setAdministrator(administrator);
    }

    public void setAppointment(String title, String description, LocalDate startDate,
                       LocalDate endDate, LocalTime startTime, LocalTime endTime, Room room){
        setTitle(title);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
        setStartTime(startTime);
        setEndTime(endTime);
        setRoom(room);
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

    @Override
    protected String getInvitationText() {
        return "You have been invited to an appointment by " + administrator.getEmail() + "\n" +
                "Title: " + getTitle() + "\n" +
                "Time: " + LocalDateTime.of(getStartDate(), getStartTime()) +
                " to " + LocalDateTime.of(getEndDate(), getEndTime()) + "\n" +
                (getRoom() != null ? "Room: " + getRoom().getName() + "\n\n" : "\n") +
                "Use the button below to choose whether or not you wish to attend.";
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

    public LocalDate getStartDate() {
        return startDateProperty.getValue();
    }

    public void setStartDate(LocalDate startDateProperty) {
        this.startDateProperty.setValue(startDateProperty);
    }

    public Property<LocalDate> StartDateProperty() {
        return startDateProperty;
    }

    public LocalTime getStartTime() {
        return startTimeProperty.getValue();
    }

    public Property<LocalTime> StartTimeProperty() {
        return startTimeProperty;
    }

    public void setStartTime(LocalTime startTimeProperty) {this.startTimeProperty.setValue(startTimeProperty);}

    public LocalDate getEndDate() {
        return endDateProperty.getValue();
    }

    public Property<LocalDate> EndDateProperty() {
        return endDateProperty;
    }

    public void setEndDate(LocalDate endDateProperty) {
        this.endDateProperty.setValue(endDateProperty);
    }

    public LocalTime getEndTime() {
        return endTimeProperty.getValue();
    }

    public Property<LocalTime> EndTimeProperty() {
        return endTimeProperty;
    }

    public void setEndTime(LocalTime endTimeProperty) {
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

    public ObservableList<User> getAcceptedParticipants() {
        ObservableList<User> acceptedParticipants = FXCollections.observableArrayList();
        acceptedParticipants.addAll(getByResponse(Response.HAS_ACCEPTED));
        return acceptedParticipants;
    }

    public ObservableList<User> getInvitedParticipants() {
        ObservableList<User> invitedParticipants = FXCollections.observableArrayList();
        invitedParticipants.addAll(getByResponse(Response.NOT_ANSWERED));
        return invitedParticipants;
    }

    public ObservableList<User> getDeclinedParticipants() {
        ObservableList<User> declinedParticipants = FXCollections.observableArrayList();
        declinedParticipants.addAll(getByResponse(Response.HAS_DECLINED));
        return declinedParticipants;
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
    protected String[] getIdPair() {
        return new String[] {"AppointmentID", ""+getId()};
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
        setStartDate(results.getTimestamp("StartTime").toLocalDateTime().toLocalDate());
        setStartTime(results.getTimestamp("StartTime").toLocalDateTime().toLocalTime());
        setEndDate(results.getTimestamp("EndTime").toLocalDateTime().toLocalDate());
        setEndTime(results.getTimestamp("EndTime").toLocalDateTime().toLocalTime());
        setAdministrator(User.getById(results.getInt("AdministratorID"), db, mc));
        setCalendarProperty("" + localTimeFormat(getStartTime()) + "-" + localTimeFormat(getEndTime()) + "\n" + getTitle());
        setDescription(results.getString("Description"));
        String room=results.getString("RoomName");
        if(room == null){
            setRoom(null);
        }else{
            setRoom(Room.getByName(room, db, mc));
        }
        if(results.next()) throw new SQLException("Result not unique");

        super.refreshFromDB(db, mc);
    }

    @Override
    public void saveToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "UPDATE APPOINTMENT\n SET Title ='" +getTitle()+
                "', StartTime ='" + LocalDateTime.of(getStartDate(), getStartTime())+ "',\n" +
                "EndTime ='" + LocalDateTime.of(getEndDate(), getEndTime()) + "',\n" +
                "AdministratorID ='" + getAdministrator().getId() + "',\n" +
                "Description ='" + getDescription() + "'\n"+
                "WHERE AppointmentID =" + getId();
        if(getRoom()!=null){
            String room =  "UPDATE APPOINTMENT SET RoomName = '"+getRoom().getName() + "' WHERE AppointmentID =  '"+getId() + "'";
            db.update(room);
        }
        db.update(sql);

        super.saveToDB(db, mc);

        refreshFromDB(db, mc);
    }

    @Override
    public void insertToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "INSERT INTO `APPOINTMENT` "
            + "(`Title`, `StartTime`, `EndTime`, `AdministratorID`, "
            + "`Description`) VALUES ('"+ getTitle() +"', "
            + "'"+ LocalDateTime.of(getStartDate(), getStartTime()) + "', "
            + "'" + LocalDateTime.of(getEndDate(), getEndTime()) + "', "
            + "'" + getAdministrator().getId() +"', "
            + "'" + getDescription() + "')";
        db.update(sql);

        String sql2 = "SELECT MAX(AppointmentID) AS ID FROM APPOINTMENT";
        ResultSet results = db.query(sql2);
        if (!results.next()) throw new SQLException("This shouldn't happen. Sooo...");
        setId(results.getInt("ID"));

        if(getRoom()!=null){
            String room =  "UPDATE APPOINTMENT SET RoomName = '"+getRoom().getName()+"' WHERE AppointmentID =  '"+getId()+"'";
            db.update(room);
        }

        super.insertToDB(db, mc);

        refreshFromDB(db, mc);
    }

    public void removeFromDB(DB db) throws SQLException, DBConnectionException {
        String sql = "DELETE FROM APPOINTMENT WHERE AppointmentID ="+getId();
        db.update(sql);
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
        if(this.getStartTime().isBefore(appointment.getStartTime())){
            return -1;
        }if(this.getStartTime().isAfter(appointment.getStartTime())){
            return 1;
        }else{ return 0;}
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + getId() +
                ", title=" + getTitle() +
                ", room=" + getRoom() +
                ", startDate=" + getStartDate() +
                ", startTime=" + getStartTime() +
                ", endDate=" + getEndDate() +
                ", endTime=" + getEndTime() +
                '}';
    }
}
