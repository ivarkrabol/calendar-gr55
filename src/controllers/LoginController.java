package controllers;

import org.controlsfx.dialog.Dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController extends Controller{

    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;


    @FXML public void handleLogin() {
        if(inputValid()){
            try {
                CalendarController calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
                calender.setApp(getApplication());
            }catch(Exception e) {
                e.printStackTrace();
            }
        }else{
            Dialogs.create()
                    .title("Invalid Fields")
                    .masthead("Wrong username or passwors, please try again")
                    .showError();
        }
    }


    private boolean validUsername(){
        // TODO: have the database connection, then check if there is a username like this in the system.
        // get the user, and set the user in the Main application.
        return true;
    }

    private boolean inputValid() {
//		add check for validPassword and user things here, just now for testing
        return true;
    }



}

