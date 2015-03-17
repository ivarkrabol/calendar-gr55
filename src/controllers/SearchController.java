package controllers;

import exceptions.DBConnectionException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import models.User;
import util.DB;
import util.ModelCache;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by morit_000 on 12.03.2015.
 */
public class SearchController extends Controller{

    private DB db;
    private ModelCache mc;
    private User selectedUser;

    @FXML
    private TextField searchUser;

    @FXML
    public ComboBox<User> userList;

    private Boolean inputValid() throws SQLException, DBConnectionException {
     return (!User.searchForUser(searchUser.getText(), db, mc).isEmpty());
    }


    public void initialize(URL url, ResourceBundle rs) {
        try {
            userList.setItems(User.getAllUsers(getApplication().getDb(), getApplication().getModelCache()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
        userList.setCellFactory((combobox) -> {
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
        userList.setConverter(new StringConverter<User>() {
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
        userList.setOnAction((event) -> {
                    selectedUser = userList.getSelectionModel().getSelectedItem();
                    CalendarController calender = null;
                    try {
                        getStage().close();
                        calender.getApplication().setUser(selectedUser);
                        calender.setWeekDays();
                        calender.setApp(getApplication());
                        calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );
    }


}
