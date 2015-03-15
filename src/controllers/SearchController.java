package controllers;

import exceptions.DBConnectionException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import models.User;
import org.controlsfx.dialog.Dialogs;
import util.DB;
import util.ModelCache;

import java.sql.SQLException;

/**
 * Created by morit_000 on 12.03.2015.
 */
public class SearchController extends Controller{

    private DB db;
    private ModelCache mc;

    @FXML
    private TextField searchUser;

    @FXML
    void enterIsPressed(ActionEvent event) throws SQLException, DBConnectionException {
        handleSearch();
    }

    @FXML
    public void handleSearch() throws SQLException, DBConnectionException {
        if (inputValid()) {
            try {
                //lage view for å velge hvor users skal vises
            }
            catch (Exception e) {

            }

        }
        else {
            Dialogs.create()
                    .title("Invalid Input")
                    .masthead("No such user in the database")
                    .showError();
        }
    }

    private static ObjectProperty<Node> content;

    public final void setContent(Node value) throws SQLException, DBConnectionException {
        contentProperty().set((Node) User.searchForUser(searchUser.getText(), db, mc));
    }

    public static Node getContent() {
        return content == null ? null : content.get();
    }

    public final ObjectProperty<Node> contentProperty() {
        if (content == null) {
            content = new SimpleObjectProperty<Node>(this, "content");
        }
        return content;
    }

    private Boolean inputValid() throws SQLException, DBConnectionException {
     return (!User.searchForUser(searchUser.getText(), db, mc).isEmpty());
    }



}
