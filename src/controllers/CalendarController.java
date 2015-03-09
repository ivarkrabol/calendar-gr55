package controllers;


import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Appointment;



import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;


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
    @FXML
    private Menu adminButton;
    
    private LocalDate today = LocalDate.now();
    private int weekNumber;
    private int yearNumber;


    @Override
    public void setApp(Main app){
        super.setApp(app);
        mon.setItems(this.getApplication().getUser().getCalendar().getAppointments());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       weekNumber = getWeekNumber(this.today); week.setText(""+weekNumber); year.setText(""+today.getYear()); setStyle(week, true); setStyle(year, true);
       monCol.setCellValueFactory(cellData -> cellData.getValue().CalendarProperty());
       if(getAdminUser()==true){
    	   adminButton.setVisible(true);
    	   }
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
                this.weekNumber=w;
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
                this.yearNumber=y;
                setStyle(year, true);
            }else{
                setStyle(year, false);
            }
        }
        catch (Exception e){setStyle(week, false);}
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

    public int getWeekNumber(LocalDate today){
        WeekFields fields = WeekFields.of(Locale.getDefault());
        return today.get(fields.weekOfWeekBasedYear());
    }
}


