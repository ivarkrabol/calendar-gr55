package controllers;
import application.Main;
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
}
