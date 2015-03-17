package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

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

    private String string;


    public void setSel(String string) {
        this.string = string;
        senderNameLabel.setText(string);
    }

    public String getSel() {
        return string;
    }


    public void initialize(URL url, ResourceBundle resource) {
        //System.out.print(senderName);
        fromLabel.setText("From:");
        //navnet til den du trykker på, klarer ikke å få den fra MessageControllerklassen
        receivedLabel.setText("Received:");
        timeLabel.setText("");
        descriptionLabel.setText("Description:");
        descriptionInformationLabel.setText("");
        invitationLabel.setText("Accept Invitation: ");
        //TODO have to initialize combobox
    }








}
