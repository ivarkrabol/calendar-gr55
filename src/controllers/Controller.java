package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.Initializable;

public class Controller implements Initializable{
	
    private Main application;
 
    public Main getApplication() {
		return application;
	}
	public void setApp(Main application){
        this.application = application;
    }
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
