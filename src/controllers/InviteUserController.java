package controllers;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import models.Appointment;
import models.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by kes on 15.03.15.
 */
public class InviteUserController extends Controller {

    @FXML public ListView<User> listOfPerson;
    @FXML public void handleSearchPerson(ActionEvent actionEvent) {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setListOfPerson();
    }

    private Appointment appointment;

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    public Appointment getAppointment() {
        return appointment;
    }

    public void setListOfPerson(){
        if(appointment!=null){
            ObservableList<User> invitedparticipants = FXCollections.observableArrayList();
            ObservableList<User> acceptedparticipants = FXCollections.observableArrayList();
            ObservableList<User> declinedparticipants = FXCollections.observableArrayList();
            try{
                invitedparticipants.addAll(appointment.isInvitedToApp(getApplication().getDb(), appointment.getId(), getApplication().getModelCache()));
                acceptedparticipants.addAll(appointment.hasAcceptedToApp(getApplication().getDb(), appointment.getId(), getApplication().getModelCache()));
                declinedparticipants.addAll(appointment.hasDeclinedToApp(getApplication().getDb(), appointment.getId(), getApplication().getModelCache()));
            }
            catch (SQLException e){e.printStackTrace();}
            catch (DBConnectionException e){e.printStackTrace();}
            listOfPerson.setItems(invitedparticipants);
            listOfPerson.setCellFactory((list) -> {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getEmail());
                        }
                    }
                };
            });
            listOfPerson.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent click) {

                    if (click.getClickCount() == 2) {
                        User user = listOfPerson.getSelectionModel().getSelectedItem();
                        if (user == null) {
                            return;
                        } else {
                            //TODO: add functionality to appointment class to hold participants and so on.
                            System.out.println("user = " + user);
                        }
                    }
                }
            });

        }
    }

}
