    package controllers;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import models.Appointment;
import models.Room;
import models.User;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.controlsfx.dialog.Dialog.*;

    /**
 * Created by kes on 15.03.15.
 */
public class InviteUserController extends Controller {

    @FXML public ListView<User> invited;
    @FXML public ListView<User> declined;
    @FXML public ListView<User> accepted;
    @FXML public ComboBox<User> searchPerson;

    @FXML public void handleSearchPerson(ActionEvent actionEvent) {}
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            try {
                searchPerson.setItems(User.getUsers(getApplication().getDb(), getApplication().getModelCache()));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (DBConnectionException e) {
                e.printStackTrace();
            }
            searchPerson.setCellFactory((combobox) -> {
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
            searchPerson.setConverter(new StringConverter<User>() {
                @Override
                public String toString(User item) {
                    if (item == null) {
                        return null;
                    } else {
                        return item.getEmail();
                    }
                }

                @Override
                public User fromString(String string) {
                    return null;
                }
            });
            searchPerson.setOnAction((event) -> {
                        User selectedUser = searchPerson.getSelectionModel().getSelectedItem();
                        //Invite user
                    }
            );
        }

    private Appointment appointment;
    public void setAppointment(Appointment appointment) {

        this.appointment = appointment;
        if(appointment!=null){
            try {
                appointment.setParticipants(getApplication().getDb(), appointment.getId(), getApplication().getModelCache());
            } catch (DBConnectionException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            setListOfPerson(invited, appointment.getInvitedParticipants(), Color.BLUE);
            setListOfPerson(accepted, appointment.getAcceptedParticipants(), Color.GREEN);
            setListOfPerson(declined, appointment.getDeclinedParticipants(), Color.CORAL);
        }
    }
    public Appointment getAppointment() {
        return appointment;
    }

    public void setListOfPerson(ListView<User> listOfPerson, ObservableList<User> persons, Color color){
        if(appointment!=null){
            listOfPerson.setItems(persons);
            listOfPerson.setCellFactory((list) -> {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setTextFill(color);
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
                            Action response = Dialogs.create()
                                    .title("Remove person?")
                                    .message("Do want to remove this person from the appointment?")
                                    .showConfirm();
                            if (response == Actions.YES) {
                                listOfPerson.getItems().remove(user);
                                //add method to remove participant from the participant table, and send message
                            }
                        }
                    }
                }
            });

        }
    }

    @FXML public void handleCancel(ActionEvent actionEvent) {
        getStage().close();
    }

    @FXML public void handleSave(ActionEvent actionEvent) {

    }
}
