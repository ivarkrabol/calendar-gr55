package controllers;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    private ResourceBundle resource;
    private URL url;

    @FXML
    private TableView<Message> inbox;
    @FXML
    private TableColumn<Message, String> from;
    @FXML
    private TableColumn<Message, String> msg;
    @FXML
    private TableColumn<Message, Timestamp> recieved;



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

    public void setInbox() throws SQLException, DBConnectionException {
        inbox.setItems(Message.getInbox(getApplication().getUser().getId(), getApplication().getDb(), getApplication().getModelCache()));
        from.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        msg.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        recieved.setCellValueFactory(cellData -> cellData.getValue().sentTimeProperty());
        inbox.setRowFactory(tv -> {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Message rowData = row.getItem();
                    newMessageStage("/views/ViewMessage.fxml", "Message details", rowData);
                }
            });
            return row;
        });
    }




    public void initialize(URL url, ResourceBundle resource) {

        try {setInbox();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
    }


    public void refreshInbox() {
        initialize(url, resource);

    }




    private void newMessageStage(String location, String title, Message selected){
        Stage currentStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(location));
            AnchorPane root = fxmlLoader.load();
            currentStage.setTitle(title);
            currentStage.setScene(new Scene(root));
            MessageDetailsController controller = fxmlLoader.getController();
            controller.setMessage(selected);
            controller.setApp(getApplication());
            controller.setStage(currentStage);
            currentStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }




}



