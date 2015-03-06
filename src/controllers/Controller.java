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
    private static boolean adminUser = false;
    private Stage stage;

    public Main getApplication() {
        return application;
    }
    public void setApp(Main application){
        this.application = application;
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
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
    public void setAdminUser(boolean b){
    	this.adminUser = b;
    }
    public boolean getAdminUser(){
		return adminUser;
    }

}

