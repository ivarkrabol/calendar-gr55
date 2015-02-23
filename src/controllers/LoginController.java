package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController implements Initializable{

    @FXML
    private Button loginBtn;
    
    @Override
   	public void initialize(URL location, ResourceBundle resources) {
   		 
   		loginBtn.setOnAction(new EventHandler<ActionEvent>() {

               @Override
               public void handle(ActionEvent event) {
            	   try {
            		    Stage stage = (Stage) loginBtn.getScene().getWindow();
            		    stage.close();
            		   
            	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ViewCalendar.fxml"));
            	        Parent root = (Parent) fxmlLoader.load();
            	        Stage stage2 = new Stage();
            	        stage2.setTitle("Calendar");
            	        stage2.setScene(new Scene(root));  
            	        stage2.show();
            	   } catch(Exception e) {
            		   e.printStackTrace();
            	   }
                   
               	
               }
           });
       }
    

}
