package controllers;

import javafx.fxml.FXML;

/**
 * Created by kes on 27.02.15.
 */
public class EditGroupController extends Controller{

    @FXML
    public void handleDelete() {
        this.getStage().close();
    }

    @FXML public void handleSave() {
        this.getStage().close();
    }

}
