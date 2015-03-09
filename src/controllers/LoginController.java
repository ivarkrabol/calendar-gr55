package controllers;

import exceptions.DBConnectionException;
import models.User;

import org.controlsfx.dialog.Dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.DB;

import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginController extends Controller{


	
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    private int id;
    private String correctPassword;
    
    

    @FXML
    void enterIsPressed(ActionEvent event) {
    	handleLogin();
    }

    @FXML public void handleLogin() {
        if(inputValid()){

            try {
                CalendarController calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
                calender.setApp(getApplication());
                calender.setWeekDays();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }else{
            Dialogs.create()
                    .title("Invalid Fields")
                    .masthead("Wrong username or password, please try again")
                    .showError();
        }
    }


    private boolean validUsername(){
        try{
            DB db = this.getApplication().getDb();
            ResultSet results = db.query("SELECT password, userid FROM USER WHERE EMail = '" + userName.getText() + "'");
            if(results.next()){
                this.correctPassword = results.getString("password");
                this.id = results.getInt("userid");
                setStyle(userName, true);
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();

        } catch (DBConnectionException e){
            e.printStackTrace();
        }

        setStyle(userName, false);
        return false;
    }

    private boolean inputValid() {
        try{if(validUsername()){
            if(this.correctPassword.equalsIgnoreCase(password.getText())){
                this.getApplication().setUser(User.getById(id, this.getApplication().getDb(), this.getApplication().getModelCache()));
				if(id == 5){
					setAdminUser(true);
                }
				else{
					setAdminUser(false);
				}
                return true;
            }
        }} catch (SQLException e){
            e.printStackTrace();

        } catch (DBConnectionException e){
            e.printStackTrace();
        }
        setStyle(password, false);
        return false;
    }

}

