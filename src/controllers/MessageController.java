package controllers;

import exceptions.DBConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.Message;
import models.User;

import javax.swing.text.html.ListView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by morit_000 on 15.03.2015.
 */
public class MessageController extends UserController{

    @FXML
    private Label showInboxLabel;

    @FXML
    private javafx.scene.control.ListView inboxListView;



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
        showInboxLabel.setText(getApplication().getUser().getFirstName());
        try {
            inboxListView.setItems(Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
    }

    public void refreshInbox() {

    }


}
