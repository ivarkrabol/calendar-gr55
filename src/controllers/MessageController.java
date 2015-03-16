package controllers;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Message;

import java.net.URL;
import java.sql.SQLException;
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

    private ResourceBundle resource;
    private URL url;


    @FXML
    private ListView<String> inboxListView;

    @FXML
    private ListView<String> inboxListView2;



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
        ObservableList<String> SenderList = FXCollections.observableArrayList();
        inbox = Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache());
        int i = 0;
        while (!inbox.isEmpty()) {
            String str = inbox.get(i).getSender().getFirstName();
            SenderList.add(str);
            i++;
            System.out.print(str);
        }

        return SenderList;
    }


    public void initialize(URL url, ResourceBundle resource) {
        showInboxLabel.setText("From");
        showInboxLabel2.setText("Description");

        try {
            inboxListView.setItems(convert());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
    }




    public void refreshInbox() {
        initialize(url, resource);

        

    }


}
