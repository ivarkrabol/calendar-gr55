package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Appointment;
import models.Calendar;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by morit_000 on 17.03.2015.
 */
public class GuestCalendarController extends Controller{

    @FXML
    private TextField week;
    @FXML
    private TextField year;
    @FXML
    private Label monText;
    @FXML
    private Label tueText;
    @FXML
    private Label wedText;
    @FXML
    private Label thuText;
    @FXML
    private Label friText;
    @FXML
    private Label satText;
    @FXML
    private Label sunText;

    @FXML
    private ListView<Appointment> mon;
    @FXML
    private ListView<Appointment> tue;
    @FXML
    private ListView<Appointment> wed;
    @FXML
    private ListView<Appointment> thu;
    @FXML
    private ListView<Appointment> fri;
    @FXML
    private ListView<Appointment> sat;
    @FXML
    private ListView<Appointment> sun;

    private Map<Integer, ListView<Appointment>> weekDaysTable;
    private Map<Integer, Label> weekDays;
    private Calendar calendarModel;


    @FXML
    public void handleBack() throws Exception { 
    	getApplication().setUser(SearchController.user);
        CalendarController calender = null;
        calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
        calender.setWeekDays();
        calender.setApp(getApplication());
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	getApplication().setUser(SearchController.selectedUser);
        year.setText(""+ LocalDate.now().getYear());
        week.setText("" + LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
        setStyle(week, true);
        setStyle(year, true);
        setMaps();
        calendarModel = getApplication().getUser().getCalendar();
        setWeekDays();
    }

    
	public final void setMaps(){
        weekDaysTable = new HashMap<Integer, ListView<Appointment>>();
        weekDaysTable.put(java.util.Calendar.MONDAY, mon);
        weekDaysTable.put(java.util.Calendar.TUESDAY, tue);
        weekDaysTable.put(java.util.Calendar.WEDNESDAY, wed);
        weekDaysTable.put(java.util.Calendar.THURSDAY, thu);
        weekDaysTable.put(java.util.Calendar.FRIDAY, fri);
        weekDaysTable.put(java.util.Calendar.SATURDAY, sat);
        weekDaysTable.put(java.util.Calendar.SUNDAY, sun);
        weekDays = new HashMap<Integer, Label>();
        weekDays.put(java.util.Calendar.MONDAY, monText);
        weekDays.put(java.util.Calendar.TUESDAY, tueText);
        weekDays.put(java.util.Calendar.WEDNESDAY, wedText);
        weekDays.put(java.util.Calendar.THURSDAY, thuText);
        weekDays.put(java.util.Calendar.FRIDAY, friText);
        weekDays.put(java.util.Calendar.SATURDAY, satText);
        weekDays.put(java.util.Calendar.SUNDAY, sunText);
    }


    @FXML public void weekTextFieldFocusChange() {
        try{
            int w = Integer.parseInt(week.getText());
            if((w>0) && (w<53)){
                calendarModel.setWeekNumber(w);
                emptyWeekDays();
                setWeekDays();
                setStyle(week, true);
            }else{
                setStyle(week, false);
            }
        }
        catch (Exception e){setStyle(week, false);}
    }

    @FXML public void yearTextFieldFocusChange() {
        try{
            int y = Integer.parseInt(year.getText());
            if((y>2014) && (y<2025)){
                calendarModel.setYearNumber(y);
                emptyWeekDays();
                setWeekDays();
                setStyle(year, true);
            }else{
                setStyle(year, false);
            }
        }
        catch (Exception e){setStyle(week, false);}
    }

    public void emptyWeekDays(){
        for(ListView table : weekDaysTable.values()){
            table.setItems(null);
        }
    }

    public void setWeekDays() {
        Map<Integer, ObservableList<Appointment>> appointmentsForWeek = calendarModel.appointmentsForWeek();
        for(int day : Calendar.daysOfWeek) {
            ListView<Appointment> table = weekDaysTable.get(day);
            ObservableList<Appointment> appointments = appointmentsForWeek.get(day);
            table.setEditable(false);
            LocalDate date = calendarModel.getDate(day);
            String dayDescription = getDayDescription(date);
            weekDays.get(day).setText("   " + dayDescription);
            if (appointments.size() > 0) {
                table.setItems(appointments);
                table.setCellFactory((list) -> {
                    return new ListCell<Appointment>() {
                        @Override
                        protected void updateItem(Appointment item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
                            } else {
                                setText(item.getCalendarProperty());
                            }
                        }
                    };
                });
            }
        }
    }

    private String getDayDescription(LocalDate day){
        String name = ""+day.getDayOfWeek();
        return name.substring(0, 3)+" " + day.getDayOfMonth() + "." + day.getMonthValue();
    }





}
