package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import exceptions.DBConnectionException;
import application.Main;
import javafx.scene.image.Image;
import models.Group;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;


public class UserController extends Controller {
	@Override
	public void setApp(Main app) {
		super.setApp(app);
	}

	@FXML
    private Label userNameLabel;
    @FXML
    private Label userName;
	@FXML
    private Label emailLabel;
	@FXML
    private Label phoneLabel;
	@FXML
	private ListView<String> groupList;
    @FXML
    private ImageView image;
    
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
    	displayUser();
    	displayGroups();

    }

    private void displayImage(){
        //JUST BULL for fun! Can't get it to work.. hehe
        Image im = user.getImage(getApplication().getDb(), getApplication().getModelCache());
        //image.setImage(im);
    }
    private void displayUser() {
    	String navn = user.getFirstName() + " " + user.getLastName();
    	userNameLabel.setText(navn);
        userName.setText(user.getFirstName());
    	emailLabel.setText(user.getEmail());
    	phoneLabel.setText(Integer.toString(user.getPhoneNr()));
	}

	public void displayGroups() {
    	ObservableList<Group> groups;
		try {
			groups = Group.getGroupsUserIsPartOf(user.getId(), getApplication().getDb(), getApplication().getModelCache());
			ObservableList<String> groupNames = FXCollections.observableArrayList();
			for (int i = 0; i < groups.size(); i++) {
				groupNames.add(Group.getName(groups.get(i).getId(), getApplication().getDb(), getApplication().getModelCache()));
			}
			groupList.setItems(groupNames);
		} catch (DBConnectionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    

}
