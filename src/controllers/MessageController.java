package controllers;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.Appointment;
import models.Message;
import models.User;

import javax.swing.text.html.ListView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by morit_000 on 15.03.2015.
 */
public class MessageController extends UserController{

    private Message message;

    @FXML
    private Label showInboxLabel;

    @FXML
    private Label showInboxLabel2;


    @FXML
    private javafx.scene.control.ListView inboxListView;

    @FXML
    private javafx.scene.control.ListView inboxListView2;



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

    public ObservableList<String> convert() throws SQLException, DBConnectionException {
        ObservableList<Message> inbox = FXCollections.observableArrayList();
        ObservableList<String> DescriptionList = FXCollections.observableArrayList();
        inbox.addAll(Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache()));
        int i = 0;
        while (!inbox.isEmpty()) {
            String str = inbox.get(i).getSender().getFirstName() + " " + inbox.get(i).getSender().getLastName();
            DescriptionList.add(str);
            i++;
            System.out.print(str);
        }

        return DescriptionList;
    }


    public void initialize(URL url, ResourceBundle resource) {
        showInboxLabel.setText("From");
        showInboxLabel2.setText("Description");

        try {
            inboxListView.setItems(convert());
            inboxListView2.setItems(Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
    }




    public void refreshInbox() {

    }


}
