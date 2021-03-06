package controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import exceptions.DBConnectionException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.*;
import org.controlsfx.dialog.Dialogs;
import javafx.fxml.FXML;
import util.DB;
import util.ModelCache;


public class EditAppointmentController extends Controller{

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dateField;
    @FXML
    private DatePicker endDateField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private ComboBox<Room> roomBox;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button editButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button inviteButton;


    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private LocalDate endDate;
    private Room room = null;
    private Appointment appointmentModel;
    private boolean edit;

    //FXML functions
    @FXML public void titleTextFieldFocusChange() {
        textFieldValid(titleField);
    }
    @FXML public void dateDateFieldFocusChange() {this.date=dateValid(dateField, LocalDate.now());endDateField.setValue(date);}
    @FXML public void endDateFieldFocusChange() {this.endDate=dateValid(endDateField, date);}
    @FXML public void startTimeTextFieldFocusChange() {
        startTimeValid();
    }
    @FXML public void endTimeTextFieldFocusChange() {endTimeValid();}
    @FXML public void handleInviteParticipants(){
//        System.out.println(appointmentModel.getAdministrator().getEmail());
        if(save()) newInvitedStage("/views/InviteUser.fxml", "Invited participants", appointmentModel);
    }


    @FXML public void handleEdit() {
        disableFields(false);
        editButton.setVisible(false);
        cancelButton.setVisible(true);
        deleteButton.setVisible(true);
        saveButton.setVisible(true);}
    @FXML public void handleCancel(){
        getStage().close();
    }
    @FXML public void disableFields(boolean b){
        titleField.setDisable(b);
        descriptionField.setDisable(b);
        dateField.setDisable(b);
        endDateField.setDisable(b);
        startTimeField.setDisable(b);
        endTimeField.setDisable(b);
        inviteButton.setDisable(b);
        roomBox.setDisable(b);}
    @FXML public void handleSave() {
        if(save()) this.getStage().close();
    }

    private boolean save() {
        if (inputValid()) {
            if (this.appointmentModel == null) {
                appointmentModel = new Appointment(titleField.getText(), descriptionField.getText(), date, endDate, startTime, endTime, room, getApplication().getUser());
                try {
                    appointmentModel.insertToDB(getApplication().getDb(), getApplication().getModelCache());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (DBConnectionException e) {
                    e.printStackTrace();
                }
            } else {
                boolean changed = !appointmentModel.getTitle().equals(titleField.getText())
                        || !appointmentModel.getDescription().equals(descriptionField.getText())
                        || !appointmentModel.getStartDate().equals(date)
                        || !appointmentModel.getEndDate().equals(endDate)
                        || !appointmentModel.getStartTime().equals(startTime)
                        || !appointmentModel.getEndTime().equals(endTime)
                        || (room != null && !appointmentModel.getRoom().equals(room));
                if (changed) {
                    try {
                        DB db = getApplication().getDb();
                        ModelCache mc = getApplication().getModelCache();
                        String messageContent = "Changes have been made to " + titleField.getText() + "test.";
                        Map<User, Attendable.Response> participants = appointmentModel.getResponses();
                        for (User recipient : participants.keySet()) {
                            if(participants.get(recipient) == Attendable.Response.HAS_DECLINED) continue;
                            new Message(recipient, getApplication().getUser(), messageContent, false, appointmentModel).insertToDB(db, mc);
                        }
                        appointmentModel.setAppointment(titleField.getText(), descriptionField.getText(), date, endDate, startTime, endTime, room);
                        appointmentModel.saveToDB(db, mc);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (DBConnectionException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    @FXML public void handleDelete() {
        if(appointmentModel != null){
            try{appointmentModel.removeFromDB(getApplication().getDb());}
            catch (SQLException e){e.printStackTrace();}
            catch (DBConnectionException e){e.printStackTrace();}
        }
        this.getStage().close();
    }

    //set the fields to the appointment
    public void setAppointmentModel(Appointment appointmentModel) {
        this.appointmentModel = appointmentModel;
        titleField.setText(appointmentModel.getTitle());
        descriptionField.setText(appointmentModel.getDescription());
        startTimeField.setText(appointmentModel.localTimeFormat(appointmentModel.getStartTime()));
        startTime=appointmentModel.getStartTime();
        endTime = appointmentModel.getEndTime();
        date = appointmentModel.getStartDate();
        endDate = appointmentModel.getEndDate();
        endTimeField.setText(appointmentModel.localTimeFormat(appointmentModel.getEndTime()));
        dateField.setValue(appointmentModel.getStartDate());
        endDateField.setValue(appointmentModel.getEndDate());
        if(date == null) roomBox.setValue(appointmentModel.getRoom());
        startTimeTextFieldFocusChange();
        endTimeTextFieldFocusChange();
        disableFields(true);
    }

    //set the buttons visibilty
    public void setEdit(boolean edit) {
        this.edit = edit;
        if(edit == true){
            editButton.setVisible(true);
            cancelButton.setVisible(true);
            deleteButton.setVisible(false);
            saveButton.setVisible(false);
        }if(appointmentModel.getStartDate().isBefore(LocalDate.now())){
            editButton.setVisible(false);
        }
    }

    //validation for the input from the user
    public boolean inputValid(){
        String error = "";
        if (!textFieldValid(titleField)){error += "The appointment must have a title\n";}
        if (this.date==null){
            error += "The start date have to be a valid date, after todays date\n";
            dateField.setStyle("-fx-background-color: #FFB2B2;");
        }
        if ((this.startTime==null)){
            error += "The appointment must have a valid starttime\n";
            setStyle(startTimeField, false);
        }
        if ((this.endTime==null)){
            error += "The appointment must have a valid endtime, must be after the endtime\n";
            setStyle(endTimeField, false);
        }
        if(error.length()==0){return true;}
        else{
            //can cut this to, maybe not necessary?
            Dialogs.create()
                    .title("Invalid Fields")
                    .masthead("Please correct invalid fields")
                    .message(error)
                    .showError();
            return false;}
    }

    //validate textfields
    private boolean textFieldValid(TextField text){
        if(text.getText().length()==0){
            setStyle(text, false);
            return false;
        }else{
            setStyle(text, true);
            return true;
        }
    }

    //validate starttime
    private boolean startTimeValid(){
        String time = startTimeField.getText();
        try {
            int hours = getHour(time);
            int mins = getMin(time);
            if(checkTime(hours, mins, time)){
                this.startTime = LocalTime.of(hours, mins);
                setStyle(startTimeField, true);
                return true;
            }else{
                setStyle(startTimeField, false);
                this.startTime=null;
                return false;
            }
        } catch (Exception e) {
            setStyle(startTimeField, false);
        }
        return false;
    }

    //validate endtime
    private boolean endTimeValid() {
        String time = endTimeField.getText();
        try {
            int hours = getHour(time);
            int mins = getMin(time);
            this.endTime = LocalTime.of(hours, mins);
            if(checkTime(hours, mins, time) && (LocalDateTime.of(this.date, this.startTime).isBefore(LocalDateTime.of(this.endDate, this.endTime)))){
                setStyle(endTimeField, true);
                setRooms();
                return true;
            }else{
                setStyle(endTimeField, false);
                this.endTime=null;
                return false;
            }
        } catch (Exception e) {
            setStyle(endTimeField, false);

        }
        return false;
    }

    //validate startdate
    private LocalDate dateValid(DatePicker date, LocalDate compare) {
        try {
            if((date!=null) && (date.getValue().compareTo(compare)>=0)){
                date.setStyle("-fx-background-color: #CCFFCC;");
                return date.getValue();
            }else{
                date.setStyle("-fx-background-color: #FFB2B2;");
                return null;
            }
        } catch (Exception e) {
            date.setStyle("-fx-background-color: #FFB2B2;");
        }
        return null;
    }

    //fills the combobox with available rooms
    public void setRooms() {
        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        try{
            roomBox.setItems(Room.getAvailable(start, end, getApplication().getDb(), getApplication().getModelCache()));
        	if(appointmentModel != null){roomBox.setValue(appointmentModel.getRoom());}
        }
        catch (SQLException e){e.printStackTrace();}
        catch (DBConnectionException e){e.printStackTrace();}
        roomBox.setCellFactory((combobox) -> {
            return new ListCell<Room>() {
                @Override
                protected void updateItem(Room item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });
        roomBox.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room room) {
                if (room == null) {
                    return null;
                } else {
                    return room.getName();
                }
            }

            @Override
            public Room fromString(String string) {
                return null;
            }
        });
        roomBox.setOnAction((event) -> {
                    Room selectedRoom = roomBox.getSelectionModel().getSelectedItem();
                    this.room = selectedRoom;
                }
        );
    }



    //private helpmethods

    private int getMin(String time){
        return Integer.parseInt(time.substring(3, 5));
    }
    private int getHour(String time){
        return Integer.parseInt(time.substring(0, 2));
    }
    private boolean checkTime(int hours, int mins, String time){
        if((hours>=0) && (hours<24) && (mins>=0) && (mins<60) && (time.charAt(2)==':')){
            return true;
        }
        else{ return false; }
    }
    private void newInvitedStage(String location, String title, Appointment a){
        Stage currentStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(location));
            AnchorPane root = fxmlLoader.load();
            currentStage.setTitle(title);
            currentStage.setScene(new Scene(root));
            InviteUserController controller = fxmlLoader.getController();
            controller.setApp(getApplication());
            controller.setStage(currentStage);
            controller.setAppointment(a);
            currentStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
