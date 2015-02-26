package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;




public class CalendarController extends Controller{


    @FXML public void handleNewAppoinment() {
        Stage currentStage = new Stage();
        try {
            //TODO: make dialog, instead of using a new stage, have to add an external lib
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EditAppointment.fxml"));
            AnchorPane root = fxmlLoader.load();
            currentStage.setTitle("New Appointment");
            currentStage.setScene(new Scene(root));
            EditAppointmentController controller = fxmlLoader.getController();
            controller.setStage(currentStage);
            currentStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}


