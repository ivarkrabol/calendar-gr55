package controllers;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.User;
import org.controlsfx.dialog.Dialogs;

import util.DB;
import exceptions.DBConnectionException;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddUserController extends Controller {
	@FXML
    private TextField fname;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField lname;

    @FXML
    private TextField password;

    @FXML
    private TextField phonenumber;

    @FXML
    private Button saveButton;

    @FXML
    private TextField email;
    
    @Override
	public void setApp(Main app) {
		super.setApp(app);
	}
    
    
    @FXML
    void handleCancel(ActionEvent event) {
    	this.getStage().close();
    }

    @FXML
    void handleSave(ActionEvent event) throws DBConnectionException, SQLException {
    	if (inputValid()){
            User user = new User(email.getText(), lname.getText(), fname.getText(), phonenumber.getText(), password.getText());
            user.insertToDB(getApplication().getDb(), getApplication().getModelCache());
        	this.getStage().close();
    	}
    }
    
	private boolean inputValid() {
		String error = "";
        if (!textFieldValid(fname)){error += "The user must have a first name\n";}
        if (!textFieldValid(lname)){error += "The user must have a last name\n";}
        if (!emailValid()){error += "Please enter a valid email address\n";}
        if (!phonenumberValid()){error += "Please enter a valid phone number\n";}
        if (!textFieldValid(password)){error += "The user must have a password\n";}
        
        if(error.length()==0){return true;}
        else{
            //can cut this to, maybe not necessary?
            Dialogs.create()
                    .title("Invalid Fields")
                    .masthead("Please correct invalid fields")
                    .message(error)
                    .showError();
            return false;}
	}
    private boolean textFieldValid(TextField text){
        if(text.getText().length()==0){
            setStyle(text, false);
            return false;
        }else{
            setStyle(text, true);
            return true;
        }
    }

    private boolean phonenumberValid() {
        if(textFieldValid(phonenumber)) {
            try { Integer.parseInt(phonenumber.getText()); }
            catch (NumberFormatException e) {
                setStyle(phonenumber, false);
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean emailValid() {
        if(textFieldValid(email)) {
            if(email.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")) {
                return true;
            }
        }
        setStyle(email, false);
        return false;
    }
}
