package controllers;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import models.Message;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * Created by morit_000 on 15.03.2015.
 */
public class MessageController extends UserController {

    private Message message;

    @FXML
    private Label showInboxLabel;
    @FXML
    private Label showInboxLabel2;
    @FXML
    private Label showInboxLabel3;

    private ResourceBundle resource;
    private URL url;


    @FXML
    private ListView<String> inboxListView;

    @FXML
    private ListView<String> inboxListView2;

    @FXML
    private ListView<Timestamp> inboxListView3;

    public ListView<String> getInboxListView() {
        return inboxListView;
    }

    public ListView<String> getInboxListView2() {
        return inboxListView2;
    }


    @FXML
    public void handleViewInbox() {

    }

    @FXML
    public void handleBack() {
        try {
            CalendarController calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
            calender.setApp(getApplication());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> getSenderName() throws SQLException, DBConnectionException {
        ObservableList<Message> inbox = FXCollections.observableArrayList();
        ObservableList<String> SenderList = FXCollections.observableArrayList();
        inbox.addAll(Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache()));
        int i = 0;
        while (i < inbox.size()) {
            String str = inbox.get(i).getSender().getFirstName() + " " + inbox.get(i).getSender().getLastName();
            SenderList.add(str);
            i++;
        }

        return SenderList;
    }

    public ObservableList<String> getDescription() throws SQLException, DBConnectionException {
        ObservableList<Message> inbox = FXCollections.observableArrayList();
        ObservableList<String> DescriptionList = FXCollections.observableArrayList();
        inbox.addAll(Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache()));
        int i = 0;
        while (i < inbox.size()) {
            String str = inbox.get(i).getDescription();
            DescriptionList.add(str);
            i++;
        }

        return DescriptionList;
    }

    public ObservableList<Timestamp> getDate() throws SQLException, DBConnectionException {
        ObservableList<Message> inbox = FXCollections.observableArrayList();
        ObservableList<Timestamp> timeList = FXCollections.observableArrayList();
        inbox.addAll(Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache()));
        int i = 0;
        while (i < inbox.size()) {
            Timestamp str = inbox.get(i).getSentTime();
            timeList.add(str);
            i++;
        }

        return timeList;
    }


    public void initialize(URL url, ResourceBundle resource) {
        showInboxLabel.setText("From:");
        showInboxLabel2.setText("Description:");
        showInboxLabel3.setText("Received:");

        try {
            inboxListView.setItems(getSenderName());
            inboxListView2.setItems(getDescription());
            inboxListView3.setItems(getDate());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
    }


    public void refreshInbox() { //vet ikke om denne funker?
        initialize(url, resource);

    }

    public String getSelectedSenderName() {
        String str = inboxListView.getSelectionModel().getSelectedItem();
        return str;
    }

    public void handleMouseClick(MouseEvent click) {
        if (click.getClickCount() == 2) {
            if (inboxListView.getSelectionModel().getSelectedItem() != null || inboxListView2.getSelectionModel().getSelectedItem() != null) {
                newStage("/views/ViewMessage.fxml", "Message details", new MessageDetailsController());
                System.out.print(getSelectedSenderName());//denne her funker ikke??
            }
        }
    }



}



