package controllers;
import application.Main;
import exceptions.DBConnectionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Room;
import org.controlsfx.dialog.Dialogs;
import util.DB;

import java.sql.SQLException;

public class AddRoomController extends Controller {
	@FXML
	private TextField rname;
	@FXML 
	private TextField rsize;
    
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
            DB db= getApplication().getDb();
            Room room = new Room(rname.getText(), Integer.parseInt(rsize.getText()));
            room.insertToDB(db);
        	this.getStage().close();
    	}
    }
    
	private boolean inputValid() {
		String error = "";
        if (!textFieldValid(rname)){error += "The room must have a valid name\n";}
        if (!textFieldValid(rsize)){error += "The room must have a size\n";}
        
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
}
