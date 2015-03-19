package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Message;

import java.net.URL;
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

    private Message message;


    public void setMessage(Message message) {
        this.message = message;
        senderNameLabel.setText(message.getUsername());
        descriptionInformationLabel.setText(message.getDescription());
        timeLabel.setText(message.getSentTime().toString());
    }

    public void handleCloseWindow() {
        getStage().close();
    }


    public void initialize(URL url, ResourceBundle resource) {
        fromLabel.setText("From: ");
        receivedLabel.setText("Received: ");;
        descriptionLabel.setText("Descripion: ");
        invitationLabel.setText("Accept invitation: ");

    }








}
