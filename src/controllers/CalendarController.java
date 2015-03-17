package controllers;


import application.Main;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.*;
import models.Calendar;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;


public class CalendarController extends Controller{


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
    @FXML
    private Label titleField;
    @FXML
    private Menu adminButton;


    private Map<Integer, ListView<Appointment>> weekDaysTable;
    private Map<Integer, Label> weekDays;
    private Calendar calendarModel;


    @Override
    public void setApp(Main app){
        super.setApp(app);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(getAdminUser()){
            adminButton.setVisible(true);
        }
        year.setText(""+LocalDate.now().getYear());
        week.setText("" + LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
        setStyle(week, true);
        setStyle(year, true);
        setMaps();
        calendarModel = getApplication().getUser().getCalendar();
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

    @FXML public void handleNewAppoinment() {
        newStage("/views/EditAppointment.fxml", "New Appointment", new EditAppointmentController());
    }

    public void handleEditAppoinment(Appointment a) {
        newAppointmentStage("/views/EditAppointment.fxml", "Edit Appointment", a);
    }


    @FXML public void handleNewGroup() {
        newStage("/views/EditGroup.fxml", "New Group", new EditGroupController());
    }
    @FXML public void handlePersons() {
        newStage("/views/SearchUser.fxml", "Persons", new SearchController());
    }
    @FXML public void handleAddUser() {
        newStage("/views/AddUser.fxml", "Add user", new AddUserController());
    }
    @FXML public void handleAddRoom() {
        newStage("/views/AddRoom.fxml", "Add room", new AddRoomController());
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
                table.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent click) {
                        if (click.getClickCount() == 2) {
                            Appointment a = table.getSelectionModel()
                                    .getSelectedItem();
                            if(a!=null) handleEditAppoinment(a);
                        }
                    }
                });
            }
        }
    }

    private String getDayDescription(LocalDate day){
        String name = ""+day.getDayOfWeek();
        return name.substring(0, 3)+" " + day.getDayOfMonth() + "." + day.getMonthValue();
    }



    private void newAppointmentStage(String location, String title, Appointment a){
        Stage currentStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(location));
            AnchorPane root = fxmlLoader.load();
            currentStage.setTitle(title);
            currentStage.setScene(new Scene(root));
            EditAppointmentController controller = fxmlLoader.getController();
            controller.setApp(getApplication());
            controller.setStage(currentStage);
            controller.setAppointmentModel(a);
            controller.setEdit(true);
            currentStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @FXML public void handleViewUser(){
        try {
            UserController userController = (UserController) getApplication().replaceSceneContent("/views/ViewUser.fxml");
            userController.setApp(getApplication());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleViewInbox() {
        try {
            UserController userController = (UserController) getApplication().replaceSceneContent("/views/ViewInbox.fxml");
            userController.setApp(getApplication());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}


