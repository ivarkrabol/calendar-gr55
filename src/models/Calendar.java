package models;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Calendar extends Model {

    private User user;
    private Group group;

	private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

	public Calendar() {
    }

    private void setAppointments(int id, DB db, ModelCache modelCache, String owner){
        ResultSet results;
        try {
            results = db.query("SELECT `AppointmentID` FROM `PARTICIPANTS` WHERE `"+owner+"` = " + id);
            if (results.next()) {
                //TODO: add methods for adding the appointments to observable list
            }
        }catch (SQLException e){
            System.out.println("Exception:" + e);
        }catch (DBConnectionException e){
            System.out.println("Exception:" + e);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user, DB db, ModelCache modelCache){
        this.user = user;
        setAppointments(user.getId(), db, modelCache, "UserID");
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group, DB db, ModelCache modelCache) {
        this.group = group;
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
