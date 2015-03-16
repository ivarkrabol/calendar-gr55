package models;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;


public class Calendar extends Model {

    public enum OwnerType {
        user ("UserID"), group ("GroupID");
        private final String idFieldName;
        private OwnerType(String s) { idFieldName = s; }
        @Override public String toString(){ return idFieldName;}
    }

    public static int[] daysOfWeek = new int[]{
            java.util.Calendar.MONDAY,
            java.util.Calendar.TUESDAY,
            java.util.Calendar.WEDNESDAY,
            java.util.Calendar.THURSDAY,
            java.util.Calendar.FRIDAY,
            java.util.Calendar.SATURDAY,
            java.util.Calendar.SUNDAY,
    };

    private int id;
    private int weekNumber;
    private int yearNumber;
    private java.util.Calendar calendar = java.util.Calendar.getInstance();
    private DB db;
    private OwnerType ownerType;
    private ModelCache modelCache;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public Calendar(int id, DB db, ModelCache modelCache, OwnerType ownerType){
        this.id=id;
        LocalDate today = LocalDate.now();
        this.yearNumber = today.getYear();
        this.weekNumber = getWeekNumber(today);
        this.db=db;
        this.modelCache=modelCache;
        this.ownerType=ownerType;
        this.calendar.setFirstDayOfWeek(java.util.Calendar.MONDAY);
        loadAppointments();
    }

    public int getId() {
         return id;
     }
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        loadAppointments();
    }

    public void setYearNumber(int yearNumber) {
        this.yearNumber = yearNumber;
        loadAppointments();
    }

    public void loadAppointments(){
        LocalDate startWeekDate = getDate(java.util.Calendar.MONDAY);
        LocalDate endWeekDate = getDate(java.util.Calendar.SUNDAY).plusDays(1);
//        System.out.println(startWeekDate);
//        System.out.println(endWeekDate);
        loadAppointments(startWeekDate, endWeekDate);
    }

    public void loadAppointments(LocalDate startdate, LocalDate enddate){
        appointments.removeAll(appointments);
        ResultSet results;
        try {
            String query = "SELECT AppointmentID\n" +
                            "FROM PARTICIPANTS\n" +
                            "WHERE "+ownerType+" ='"+id+"'"+
                            "AND Response ='HasAccepted' AND AppointmentID\n" +
                            "IN (\n" +
                            "\n" +
                            "SELECT Appointmentid\n" +
                            "FROM APPOINTMENT\n" +
                            "WHERE starttime >=  '"+localTimeFormat(startdate)+"'\n" +
                            "AND endtime < '"+localTimeFormat(enddate)+"'\n" +
                            ")\n" +
                            "LIMIT 0 , 30";
            results = db.query(query);
            while(results.next()) {
                int appointmentId = results.getInt("AppointmentID");
                Appointment appointment = Appointment.getById(appointmentId, db, modelCache);
                appointments.add(appointment);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (DBConnectionException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Appointment> getAppointmentsByDate(LocalDate localdate){
        ObservableList<Appointment> dayAppointments = FXCollections.observableArrayList();
        for(Appointment a : appointments){
            if(a.getStartDateProperty().isEqual(localdate)){
                dayAppointments.add(a);
            }
        }
        Collections.sort(dayAppointments);
        return dayAppointments;
    }

    public Map<Integer, ObservableList<Appointment>> appointmentsForWeek(){
        Map<Integer, ObservableList<Appointment>> appointmentsForWeek = new HashMap<>();
        for (int day : daysOfWeek){
            LocalDate date = getDate(day);
            ObservableList<Appointment> appointmentsForDay = getAppointmentsByDate(date);
            appointmentsForWeek.put(day, appointmentsForDay);
        }
        return appointmentsForWeek;
    }

    public LocalDate getDate(int day) {
        calendar.setWeekDate(yearNumber, weekNumber, day);
        return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {
        throw new SQLException("Calendar is not a DB object and should not be saved");
    }

    @Override
    public void insertToDB(DB db) throws SQLException, DBConnectionException {
        throw new SQLException("Calendar is not a DB object");
    }

    public int getWeekNumber(LocalDate day){
        WeekFields fields = WeekFields.of(Locale.getDefault());
        return day.get(fields.weekOfWeekBasedYear());
    }

    public String localTimeFormat(LocalDate time){
        String res = "" + time.getYear();
        if(time.getMonthValue()<10){
            res += "0"+time.getMonthValue();
        }else{
            res += time.getMonthValue();
        }
        if(time.getDayOfMonth()<10){
            res += "0"+time.getDayOfMonth();
        }else{
            res += time.getDayOfMonth();
        }
        return res;
    }


}