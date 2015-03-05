package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller implements Initializable{

    private Main application;
    private boolean admin;
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
    public boolean getAdmin() {
        return admin;
    }
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @FXML
    public void handleLogout(){
        this.getApplication().login();
        this.getApplication().setUser(null);

    }

    protected void setStyle(TextField t, boolean b){
        if(b){
            t.setStyle("-fx-background-color: #CCFFCC;");
        }else{
            t.setStyle("-fx-background-color: #FFB2B2;");
        }
    }


}

