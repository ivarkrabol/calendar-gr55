package controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import org.controlsfx.dialog.Dialogs;
import models.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class EditAppointmentController extends Controller{

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField roomField;
    @FXML
    private DatePicker dateField;
    @FXML
    private DatePicker endDateField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private LocalDate endDate;
    private Appointment appointmentModel;




    @FXML public void handleSave() {
        if (inputValid()){

            appointmentModel = new Appointment(titleField.getText());
            appointmentModel.setDescription(descriptionField.getText());
//            appointmentModel.setDate(date);
//            appointmentModel.setStartTime(this.startTime);
//            appointmentModel.setEndTime(this.endTime);
            new Exception("Jeg(Ivar) og Sofia tenkte at det ga mest mening å " +
                    "lagre appointment-tiden med en startTime: DateTime og en endTime: DateTime " +
                    "(Slik at det også er mulig å ha en appointment som går i over 24 timer). " +
                    "Åpent for diskusjon!").printStackTrace();
            this.getStage().close();
        }
    }


    @FXML public void handleDelete() {
        this.getStage().close();
    }




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

    @FXML public void titleTextFieldFocusChange() {
        textFieldValid(titleField);
    }
    @FXML public void roomTextFieldFocusChange() {
        roomValid();
    }
    @FXML public void dateDateFieldFocusChange() {this.date=dateValid(dateField, LocalDate.now());endDateField.setValue(date);}
    @FXML public void endDateFieldFocusChange() {this.endDate=dateValid(endDateField, date);}
    @FXML public void startTimeTextFieldFocusChange() {
        startTimeValid();
    }
    @FXML public void endTimeTextFieldFocusChange() {endTimeValid();}


    private boolean textFieldValid(TextField text){
        if(text.getText().length()==0){
            setStyle(text, false);
            return false;
        }else{
            setStyle(text, true);
            return true;
        }
    }

    //	TODO: fix room!!
    private boolean roomValid(){
        String s = "[a-zA-Z]+-[a-zA-Z]+ [0-9]+";
        if(roomField.getText().matches(s)){
            setStyle(roomField, true);
            return true;
        }else{
            setStyle(roomField, false);
            return false;
        }
    }

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

    private boolean endTimeValid() {
        String time = endTimeField.getText();
        try {
            int hours = getHour(time);
            int mins = getMin(time);
            this.endTime = LocalTime.of(hours, mins);
            if(checkTime(hours, mins, time) && (this.startTime.isBefore(this.endTime))){
                setStyle(endTimeField, true);
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

}

