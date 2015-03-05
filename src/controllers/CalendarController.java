package controllers;


import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Appointment;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private TableView<Appointment> weekTable;

    @FXML
    private Label titleField;

    @Override
    public void setApp(Main app){
        super.setApp(app);
        weekTable.setItems(this.getApplication().getUser().getCalendar().getAppointments());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       monCol.setCellValueFactory(cellData -> cellData.getValue().DescriptionProperty());
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


