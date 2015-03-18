package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller implements Initializable{


    private static Main application;

    private static boolean adminUser = false;
    private Stage stage;

    public Main getApplication() {
        return application;
    }
    public void setApp(Main application){
        Controller.application = application;
    }


    protected void newStage(String location, String title, Controller Controller){
        Stage currentStage = new Stage();
        //if(Controller.getClass() == (GuestCalendarController.class)){
	        currentStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
	
				@Override
				public void handle(WindowEvent event) {
					getApplication().setUser(SearchController.user);
					
				}
	        	
	        });
        //}
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(location));
            AnchorPane root = fxmlLoader.load();
            currentStage.setTitle(title);
            currentStage.setScene(new Scene(root));
            Controller controller = fxmlLoader.getController();
            controller.setApp(getApplication());
            controller.setStage(currentStage);
            currentStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
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
    	adminUser = b;
    }
    public boolean getAdminUser(){
		return adminUser;
    }

}

