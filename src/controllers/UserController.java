package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import application.Main;
import models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class UserController extends Controller {

	@FXML
    private Label userNameLabel;

    @FXML public void handleBack(){
        try {
            CalendarController calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
            calender.setApp(getApplication());
            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       userNameLabel.setText(getApplication().getUser().getFirstName());
    }
    

}
