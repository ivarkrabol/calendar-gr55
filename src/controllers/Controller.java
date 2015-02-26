package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class Controller implements Initializable{

    private Main application;

    private Stage stage;

    public Main getApplication() {
        return application;
    }
    public void setApp(Main application){
        this.application = application;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}

