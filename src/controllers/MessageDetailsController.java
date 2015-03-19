package controllers;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Appointment;
import models.Attendable;
import models.Group;
import models.Message;
import util.DB;
import util.ModelCache;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by morit_000 on 16.03.2015.
 */
public class MessageDetailsController extends MessageController{


    @FXML
    private Label fromLabel;
    @FXML
    private Label senderNameLabel;
    @FXML
    private Label receivedLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label descriptionInformationLabel;
    @FXML
    private Label invitationLabel;
    @FXML
    private ComboBox<String> comboBox;
    private String selectedChoice;

    private Message message;
    private ObservableList<String> list = FXCollections.observableArrayList();


    public void setMessage(Message message) {
        this.message = message;
        senderNameLabel.setText(message.getUsername());
        descriptionInformationLabel.setText(message.getDescription());
        timeLabel.setText(message.getSentTime().toString());
        if (!message.isInvitation()) {
            comboBox.setVisible(false);
            invitationLabel.setVisible(false);
        }



    }

    public void handleDeleteButton() throws SQLException, DBConnectionException {
        DB db = getApplication().getDb();
        ModelCache mc = getApplication().getModelCache();
        Message.deleteMessage(message.getId(), db, mc);
        getStage().close();
    }

    public void handleCloseWindow() {
        getStage().close();
    }


    public void initialize(URL url, ResourceBundle resource) {
        DB db = getApplication().getDb();
        ModelCache mc = getApplication().getModelCache();
        fromLabel.setText("From: ");
        receivedLabel.setText("Received: ");;
        descriptionLabel.setText("Descripion: ");
        invitationLabel.setText("Accept invitation: ");
        list.add(0, "Accept");
        list.add(1, "Decline");
        comboBox.setItems(list);
        comboBox.setOnAction((event) -> {
                    selectedChoice = comboBox.getSelectionModel().getSelectedItem();
                    Attendable attendable = message.getAttendable();
                    if(selectedChoice.equals("Accept")) attendable.acceptInvite(message.getRecipient());
                    else if(selectedChoice.equals("Decline")) attendable.declineInvite(message.getRecipient());
                    try {
                        attendable.saveToDB(getApplication().getDb(), getApplication().getModelCache());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (DBConnectionException e) {
                        e.printStackTrace();
                    }
                    this.getStage().close();
                }
        );



    }








}
