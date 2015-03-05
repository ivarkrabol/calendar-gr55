package controllers;

import exceptions.DBConnectionException;
import org.controlsfx.dialog.Dialogs;

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

    private String correctPassword;
    private String adminUser = "admin@mail.no";
    
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
                    .masthead("Wrong username or password, please try again")
                    .showError();
        }
    }


    private boolean validUsername(){
        try{
            DB db = this.getApplication().getDb();
            ResultSet results = db.query("SELECT password FROM USER WHERE EMail = '" + userName.getText() + "'");
            if(results.next()){
                this.correctPassword = results.getString("password");
                
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
        if(validUsername()){
            if(this.correctPassword.equalsIgnoreCase(password.getText())){
                if(userName.getText().equals(adminUser)){
                	setAdmin(true);
                	System.out.println("admin");
                }
                else{
                	System.out.println("bruker");
                }
            	return true;
            }
        }
        setStyle(password, false);
        return false;
    }
    @FXML public void hitEnter() {
    	handleLogin();
    }

}

