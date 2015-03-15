package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class UserController extends Controller {
	@Override
	public void setApp(Main app) {
		super.setApp(app);
	}

	@FXML
    private Label userNameLabel;
	@FXML
    private Label emailLabel;
	@FXML
    private Label phoneLabel;

	
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
    	User user = getApplication().getUser();
    	String navn = user.getFirstName() + " " + user.getLastName();
    	emailLabel.setText(user.getEmail());
    	phoneLabel.setText(Integer.toString(user.getPhoneNr()));
    	
    }
    

}
