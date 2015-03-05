package models;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Calendar extends Model {

	private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public void setAppointments(int id, DB db, ModelCache modelCache, String owner){
        ResultSet results;
        try {
            String query = "SELECT  AppointmentID \n" +
                    "FROM  PARTICIPANTS \n" +
                    "WHERE "+owner+" ='"+id+"'";
            results = db.query(query);
            while(results.next()) {
                Appointment a = Appointment.getById(results.getInt("AppointmentID"), db, modelCache);
                appointments.add(a);
            }
        }catch (SQLException e){
            System.out.println("Exception:" + e);
        }catch (DBConnectionException e){
            System.out.println("Exception:" + e);
        }
    }

    public void setUser(int user, DB db, ModelCache modelCache){
        setAppointments(user, db, modelCache, "UserID");
    }


    public void setGroup(Group group, DB db, ModelCache modelCache) {
        setAppointments(group.getId(), db, modelCache, "GroupID");
    }

    public void addAppointment(Appointment appointment){
		this.appointments.add(appointment);
	}

	public void removeAppointment(Appointment appointment) {
		this.appointments.remove(appointment);
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

}