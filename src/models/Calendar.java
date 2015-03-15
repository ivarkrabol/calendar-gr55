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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class Calendar extends Model {

    private LocalDate today;
    private int weekNumber;
    private int yearNumber;
    private int id;
    private LocalDate startWeekDate;
    private LocalDate endWeekDate;
    private java.util.Calendar calendar = java.util.Calendar.getInstance();
    private DB db;
    private String owner;
    private ModelCache modelCache;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();


    public Calendar(int id, DB db, ModelCache modelCache, String owner){
        this.id=id;
        this.today = LocalDate.now();
        this.yearNumber = today.getYear();
        this.weekNumber = getWeekNumber(today);
        this.db=db;
        this.modelCache=modelCache;
        this.owner=owner;
        setCalendar();
    }

    public int getId() {
         return id;
     }
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        setCalendar();
    }

    public void setYearNumber(int yearNumber) {
        this.yearNumber=yearNumber;
    }


    public ObservableList<Appointment> getAppointments(LocalDate startdate, LocalDate enddate){
        appointments.removeAll(appointments);
        ResultSet results;
        try {
            String query = "SELECT AppointmentID\n" +
                            "FROM PARTICIPANTS\n" +
                            "WHERE "+owner+" ='"+id+"'"+
                            "AND response ='HasAccepted' AND AppointmentID\n" +
                            "IN (\n" +
                            "\n" +
                            "SELECT Appointmentid\n" +
                            "FROM APPOINTMENT\n" +
                            "WHERE starttime >=  '"+localTimeFormat(startdate)+"'\n" +
                            "AND endtime < '"+localTimeFormat(enddate)+"'\n" +
                            ")\n" +
                            "LIMIT 0 , 30";
            results = db.query(query);
            List<Integer> test = new ArrayList<>();
            while(results.next()) {
                int res = results.getInt("AppointmentID");
                test.add(res);
                Appointment a = Appointment.getById(res, db, modelCache);
                appointments.add(a);
            }
        }catch (SQLException e){
            System.out.println("Exception:" + e);
        }catch (DBConnectionException e){
            System.out.println("Exception:" + e);
        }
        return appointments;
    }

    public ObservableList<Appointment> getAppointmentsForDay(LocalDate localdate){
        ObservableList<Appointment> dayAppointments = FXCollections.observableArrayList();
        for(Appointment a:this.appointments){
            if(a.getStartDateProperty().isEqual(localdate)){
                dayAppointments.add(a);
            }
        }
        Collections.sort(dayAppointments);
        return dayAppointments;
    }

    public void setCalendar(){
        startWeekDate = getDate(1);
        endWeekDate = getEndDate(0);
        this.appointments = getAppointments(startWeekDate, endWeekDate);
    }

    public List<ObservableList<Appointment>> appointmentsForWeek(){
        List<ObservableList<Appointment>> appointmentsForWeek = new ArrayList<ObservableList<Appointment>>();
        for (int i = 1; i<8; i++){
            ObservableList<Appointment> appointmentsForDay = FXCollections.observableArrayList();
            LocalDate date = getDate(i);
            appointmentsForDay = getAppointmentsForDay(date);
            appointmentsForWeek.add(appointmentsForDay);
        }
        return appointmentsForWeek;
    }

    public LocalDate getDate(int day) {
        calendar.set(java.util.Calendar.YEAR, yearNumber);
        calendar.set(java.util.Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(java.util.Calendar.DAY_OF_WEEK, day);
        LocalDate date = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }
    public LocalDate getEndDate(int day) {
        calendar.set(java.util.Calendar.YEAR, yearNumber);
        calendar.set(java.util.Calendar.WEEK_OF_YEAR, weekNumber+1);
        calendar.set(java.util.Calendar.DAY_OF_WEEK, day);
        LocalDate date = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }


    public ObservableList<Appointment> getAppointments() {
        return appointments;
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