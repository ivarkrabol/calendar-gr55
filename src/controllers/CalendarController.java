package controllers;


import application.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;


import java.net.URL;
import java.time.LocalDate;
import java.util.*;


public class CalendarController extends Controller{

    @FXML
    private TableColumn<Appointment, String> monCol;
    @FXML
    private TableColumn<Appointment, String> tueCol;
    @FXML
    private TableColumn<Appointment, String> wedCol;
    @FXML
    private TableColumn<Appointment, String> thuCol;
    @FXML
    private TableColumn<Appointment, String> friCol;
    @FXML
    private TableColumn<Appointment, String> satCol;
    @FXML
    private TableColumn<Appointment, String> sunCol;
    @FXML
    private TextField week;
    @FXML
    private TextField year;
    @FXML
    private TableView<Appointment> mon;
    @FXML
    private TableView<Appointment> tue;
    @FXML
    private TableView<Appointment> wed;
    @FXML
    private TableView<Appointment> thu;
    @FXML
    private TableView<Appointment> fri;
    @FXML
    private TableView<Appointment> sat;
    @FXML
    private TableView<Appointment> sun;
    @FXML
    private Label titleField;

    private List<TableView<Appointment>> weekDaysTable;
    private List<TableColumn<Appointment, String>> weekDaysCol;


    @Override
    public void setApp(Main app){
        super.setApp(app);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        year.setText(""+LocalDate.now().getYear());
        setStyle(week, true);setStyle(year, true);
        setList();
    }

    public final void setList(){
        weekDaysTable = new ArrayList<TableView<Appointment>>();
            weekDaysTable.add(sun);
            weekDaysTable.add(mon);
            weekDaysTable.add(tue);
            weekDaysTable.add(wed);
            weekDaysTable.add(thu);
            weekDaysTable.add(fri);
            weekDaysTable.add(sat);
        weekDaysCol = new ArrayList<TableColumn<Appointment, String>>();
            weekDaysCol.add(sunCol);
            weekDaysCol.add(monCol);
            weekDaysCol.add(tueCol);
            weekDaysCol.add(wedCol);
            weekDaysCol.add(thuCol);
            weekDaysCol.add(friCol);
            weekDaysCol.add(satCol);
    }

    @FXML public void handleNewAppoinment() {newStage("/views/EditAppointment.fxml", "New Appointment", new EditAppointmentController());}

    @FXML public void handleNewGroup() {
        newStage("/views/EditGroup.fxml", "New Group", new EditGroupController());
    }

    @FXML public void handlePersons() {
        newStage("/views/SearchUser.fxml", "Persons", new UserController());
    }

    @FXML public void handleGroups(){
        newStage("/views/ViewUserGroups.fxml", "Groups", new Controller());
    }

    @FXML public void weekTextFieldFocusChange() {
        try{
            int w = Integer.parseInt(week.getText());
            if((w>0) && (w<53)){
                getApplication().getUser().getCalendar().setWeekNumber(w);
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
                getApplication().getUser().getCalendar().setYearNumber(y);
                setStyle(year, true);
            }else{
                setStyle(year, false);
            }
        }
        catch (Exception e){setStyle(week, false);}
    }

    public void setWeekDays() {
        List<ObservableList<Appointment>> appointmentsForWeek = getApplication().getUser().getCalendar().appointmentsForWeek();
        int i = 0;
        while(i<7){
            for(TableView<Appointment> table : weekDaysTable){
                table.setPlaceholder(new Text(""));
                LocalDate day = getApplication().getUser().getCalendar().getDate(i + 1);
                weekDaysCol.get(i).setText(""+day.getDayOfWeek()+" "+day.getDayOfMonth());
                if (appointmentsForWeek.get(0).size()>0){
                    table.setItems(appointmentsForWeek.get(i));
                    weekDaysCol.get(i).setCellValueFactory((cellData -> cellData.getValue().CalendarProperty()));
                }
                i++;
            }
        }
    }

    private void newStage(String location, String title, Controller Controller){
        Stage currentStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(location));
            AnchorPane root = fxmlLoader.load();
            currentStage.setTitle(title);
            currentStage.setScene(new Scene(root));
            Controller controller = fxmlLoader.getController();
            controller.setStage(currentStage);
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



}


