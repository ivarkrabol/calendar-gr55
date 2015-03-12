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
import java.net.URL;
import java.time.LocalDate;
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
    

    private List<ListView<Appointment>> weekDaysTable;
    private List<Label> weekDays;


    @Override
    public void setApp(Main app){
        super.setApp(app);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       if(getAdminUser()==true){
    	   adminButton.setVisible(true);
    	   }
        year.setText(""+LocalDate.now().getYear());
        setStyle(week, true);setStyle(year, true);
        setList();
    }

    public final void setList(){
        weekDaysTable = new ArrayList<ListView<Appointment>>();
            weekDaysTable.add(sun);
            weekDaysTable.add(mon);
            weekDaysTable.add(tue);
            weekDaysTable.add(wed);
            weekDaysTable.add(thu);
            weekDaysTable.add(fri);
            weekDaysTable.add(sat);
        weekDays = new ArrayList<Label>();
            weekDays.add(sunText);
            weekDays.add(monText);
            weekDays.add(tueText);
            weekDays.add(wedText);
            weekDays.add(thuText);
            weekDays.add(friText);
            weekDays.add(satText);
    }

    @FXML public void handleNewAppoinment() {
        newStage("/views/EditAppointment.fxml", "New Appointment", new EditAppointmentController());}
  
    public void handleEditAppoinment(Appointment a) {
        EditAppointmentController controller = new EditAppointmentController();
        controller.setAppointmentModel(a);
        newStage("/views/EditAppointment.fxml", "Edit Appointment", controller);
    }


    @FXML public void handleNewGroup() {
        newStage("/views/EditGroup.fxml", "New Group", new EditGroupController());
    }

    @FXML public void handlePersons() {
        newStage("/views/SearchUser.fxml", "Persons", new UserController());
    }

    @FXML public void handleGroups(){
        newStage("/views/ViewUserGroups.fxml", "Groups", new Controller());
    }
    @FXML public void handleAddUser() {
        newStage("/views/AddUser.fxml", "Add user", new AddUserController());
    }

    @FXML public void weekTextFieldFocusChange() {
        try{
            int w = Integer.parseInt(week.getText());
            if((w>0) && (w<53)){
                getApplication().getUser().getCalendar().setWeekNumber(w);
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
                getApplication().getUser().getCalendar().setYearNumber(y);
                setStyle(year, true);
            }else{
                setStyle(year, false);
            }
        }
        catch (Exception e){setStyle(week, false);}
    }

    public void emptyWeekDays(){
        for(ListView table : weekDaysTable){
            table.setItems(null);
        }
    }

    public void setWeekDays() {
        List<ObservableList<Appointment>> appointmentsForWeek = getApplication().getUser().getCalendar().appointmentsForWeek();
        int i = 0;
        while(i<7) {
            for (ListView<Appointment> table : weekDaysTable) {
                table.setEditable(false);
                LocalDate day = getApplication().getUser().getCalendar().getDate(i + 1);
                String dayDescription = getDayDescription(day);
                weekDays.get(i).setText(" " + dayDescription);
                if (appointmentsForWeek.size() > 0) {
                    table.setItems(appointmentsForWeek.get(i));
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
                                                           handleEditAppoinment(a);
                                                       }
                                                }

                    });
                }
                i++;
            }
        }
    }

    private String getDayDescription(LocalDate day){
        String name = ""+day.getDayOfWeek();
        return name.substring(0, 3)+" " + day.getDayOfMonth() + "." + day.getMonthValue();
    }



    private void newStage(String location, String title, Controller Controller){
        Stage currentStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(location));
            AnchorPane root = fxmlLoader.load();
            currentStage.setTitle(title);
            currentStage.setScene(new Scene(root));
            Controller controller = fxmlLoader.getController();
            controller.setApp(getApplication());
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


