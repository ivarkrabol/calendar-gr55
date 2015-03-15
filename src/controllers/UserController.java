package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import exceptions.DBConnectionException;
import application.Main;
import models.Appointment;
import models.Group;
import models.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


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
	@FXML
    private javafx.scene.control.ListView<Group> groupList;
	
	private User user = getApplication().getUser();
	
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
    	
    	String navn = user.getFirstName() + " " + user.getLastName();
    	userNameLabel.setText(navn);
    	emailLabel.setText(user.getEmail());
    	phoneLabel.setText(Integer.toString(user.getPhoneNr()));
    	displayGroups();
    	
    }
    
    public void displayGroups() {
    	ObservableList<Group> groups;
		try {
			groups = Group.getGroupsUserIsPartOf(user.getId(), getApplication().getDb(), getApplication().getModelCache());
			groupList.setItems(groups);
		} catch (DBConnectionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	
    }
    

}
