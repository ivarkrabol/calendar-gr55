package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;




public class CalendarController extends Controller{

    @FXML
    private Button newAppointmentBtn;
	protected AnchorPane root;
    

	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		 
		newAppointmentBtn.setOnAction(new EventHandler<ActionEvent>() {

			   @Override
               public void handle(ActionEvent event) {
            	   try {
            		   //TODO: make dialog, instead of using a new stage, have to add an external lib
	           	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EditAppointment.fxml"));
	           	        Parent root = (Parent) fxmlLoader.load();
	           	        Stage stage = new Stage();
	           	        stage.setTitle("Appointment");
	           	        stage.setScene(new Scene(root));  
	           	        stage.show();
            	   } catch(Exception e) {
            		   e.printStackTrace();
            	   }
                   
               	
            }
        });
    }

}

