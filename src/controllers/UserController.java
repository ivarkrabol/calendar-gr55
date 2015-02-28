package controllers;

import javafx.fxml.FXML;

public class UserController extends Controller {

    @FXML public void handleBack(){
        try {
            CalendarController calender = (CalendarController) getApplication().replaceSceneContent("/views/ViewCalendar.fxml");
            calender.setApp(getApplication());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}
