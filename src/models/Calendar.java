package models;
import java.util.ArrayList;
import java.util.List;

import models.Appointment;


public class Calendar extends Model {
	
	private List<Appointment> calendar;
	
	public Calendar() {
		this.calendar = new ArrayList<Appointment>();
	}
	
	
	public void addAppointment(Appointment appointment){
		this.calendar.add(appointment);
	}

	
	public void removeAppointment(Appointment appointment){
		if(this.calendar.remove(appointment));
	}
	
	
}
