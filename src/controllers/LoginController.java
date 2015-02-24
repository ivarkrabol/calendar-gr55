package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class LoginController extends Controller{

    @FXML
    private Button loginBtn;
    

    
    @Override
   	public void initialize(URL location, ResourceBundle resources) {
   		 
   		loginBtn.setOnAction(new EventHandler<ActionEvent>() {

               @Override
               public void handle(ActionEvent event) {
            	   try {
            		    CalendarController calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
            		    calender.setApp(getApplication());
            	   } catch(Exception e) {
            		   e.printStackTrace();
            	   }
                   
               	
               }
           });
       }
    

}
