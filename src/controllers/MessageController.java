package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by morit_000 on 15.03.2015.
 */
public class MessageController extends UserController{

    @FXML
    private Label showInboxLabel;


    @FXML
    public void handleViewInbox(){

    }

    @FXML public void handleBack(){
        try {
            CalendarController calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
            calender.setApp(getApplication());

        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void initialize(URL url, ResourceBundle resource) {
        showInboxLabel.setText("hei");
    }


}
